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


package org.apache.isis.alternatives.remoting.common.data;

import org.apache.isis.alternatives.remoting.common.data.common.CollectionData;
import org.apache.isis.alternatives.remoting.common.data.common.EncodableObjectData;
import org.apache.isis.alternatives.remoting.common.data.common.IdentityData;
import org.apache.isis.alternatives.remoting.common.data.common.NullData;
import org.apache.isis.alternatives.remoting.common.data.common.ObjectData;
import org.apache.isis.alternatives.remoting.common.data.common.ReferenceData;
import org.apache.isis.alternatives.remoting.common.exchange.AuthorizationResponse;
import org.apache.isis.core.metamodel.adapter.oid.Oid;
import org.apache.isis.core.metamodel.adapter.version.Version;


/**
 * Create serializable objects that are used to carry messages across the network. This assumes that the Oid
 * and Version implementations are also serializable.
 */
public interface DataFactory {

    CollectionData createCollectionData(
            String collectionType,
            String elementType,
            Oid oid,
            ReferenceData[] elements,
            boolean hasAllElements,
            Version version);

    NullData createNullData(String type);

    ObjectData createObjectData(String type, Oid oid, boolean hasCompleteData, Version version);

    IdentityData createIdentityData(String type, Oid oid, Version version);

    EncodableObjectData createValueData(String fullName, String encodedValue);

	
}