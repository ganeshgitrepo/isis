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


package org.apache.isis.viewer.scimpi.dispatcher.context;

import org.apache.isis.core.metamodel.adapter.ObjectAdapter;
import org.apache.isis.viewer.scimpi.dispatcher.context.RequestContext.Scope;
import org.apache.isis.viewer.scimpi.dispatcher.debug.DebugView;
import org.apache.isis.viewer.scimpi.dispatcher.processor.Request;


public interface ObjectMapping {

    void endSession();

    void reloadIdentityMap();

    // TODO this is only useed to clear REQUEST mappings, so change name and remove parameter to reflect that
    void clear(Scope scope);

    void unmapObject(ObjectAdapter object, Scope scope);

    void appendMappings(Request content);

    ObjectAdapter mappedObject(String id);

    String mapObject(ObjectAdapter obj, Scope scope);

    void append(DebugView view);

}