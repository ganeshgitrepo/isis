/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package org.apache.isis.viewer.restfulobjects.tck.domainservice.serviceId.action.invoke;

import static org.apache.isis.viewer.restfulobjects.tck.RestfulMatchers.hasProfile;
import static org.apache.isis.viewer.restfulobjects.tck.RestfulMatchers.isLink;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import org.apache.isis.viewer.restfulobjects.applib.JsonRepresentation;
import org.apache.isis.viewer.restfulobjects.applib.LinkRepresentation;
import org.apache.isis.viewer.restfulobjects.applib.Rel;
import org.apache.isis.viewer.restfulobjects.applib.RestfulHttpMethod;
import org.apache.isis.viewer.restfulobjects.applib.client.RestfulClient;
import org.apache.isis.viewer.restfulobjects.applib.client.RestfulResponse;
import org.apache.isis.viewer.restfulobjects.applib.client.RestfulResponse.Header;
import org.apache.isis.viewer.restfulobjects.applib.domainobjects.ActionResultRepresentation;
import org.apache.isis.viewer.restfulobjects.applib.domainobjects.DomainServiceResource;
import org.apache.isis.viewer.restfulobjects.applib.domainobjects.ObjectActionRepresentation;
import org.apache.isis.viewer.restfulobjects.applib.util.UrlEncodingUtils;
import org.apache.isis.viewer.restfulobjects.tck.IsisWebServerRule;
import org.apache.isis.viewer.restfulobjects.tck.Util;

public class DomainServiceTest_req_safe_arg_bad_malformed {

    @Rule
    public IsisWebServerRule webServerRule = new IsisWebServerRule();

    private RestfulClient client;

    private DomainServiceResource serviceResource;

    @Before
    public void setUp() throws Exception {
        client = webServerRule.getClient();
        serviceResource = client.getDomainServiceResource();
    }


    @Test
    public void usingClientFollow_whenImplicitlySetToNull() throws Exception {

        // given
        final JsonRepresentation givenAction = Util.givenAction(client, "ActionsEntities", "subListWithOptionalRange");
        final ObjectActionRepresentation actionRepr = givenAction.as(ObjectActionRepresentation.class);

        final LinkRepresentation invokeLink = actionRepr.getInvoke();

        assertThat(invokeLink, isLink(client)
                                    .rel(Rel.INVOKE)
                                    .httpMethod(RestfulHttpMethod.GET)
                                    .href(Matchers.endsWith(":39393/services/ActionsEntities/actions/subListWithOptionalRange/invoke"))
                                    .build());
        
        // when
        JsonRepresentation args = JsonRepresentation.newMap();
        args.mapPut("from", JsonRepresentation.newMap());
        args.mapPut("to.value", (Integer)null);

        final RestfulResponse<ActionResultRepresentation> restfulResponse = client.followT(invokeLink, args);
        
        // then
        then(restfulResponse);
    }

    
    @Test
    public void usingResourceProxy_whenExplicitSetToNull() throws Exception {

        // given, when
        JsonRepresentation args = JsonRepresentation.newMap();
        args.mapPut("from", JsonRepresentation.newMap());
        args.mapPut("to.value", (Integer)null);

        Response response = serviceResource.invokeActionQueryOnly("ActionsEntities", "subListWithOptionalRange", UrlEncodingUtils.urlEncode(args));
        RestfulResponse<ActionResultRepresentation> restfulResponse = RestfulResponse.ofT(response);
        
        // then
        then(restfulResponse);
    }

    
    private static void then(RestfulResponse<ActionResultRepresentation> restfulResponse) throws JsonParseException, JsonMappingException, IOException {
        assertThat(restfulResponse.getHeader(Header.CONTENT_TYPE), hasProfile(MediaType.APPLICATION_JSON));
        final JsonRepresentation errorRepr = restfulResponse.wraps(JsonRepresentation.class).getEntity();

        assertThat(errorRepr.getString("from.invalidReason"), is("No 'value' key"));
    }

}
