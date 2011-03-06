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


package org.apache.isis.alternatives.objectstore.xml.internal.data;

import java.util.HashMap;
import java.util.Map;

import org.apache.isis.alternatives.objectstore.xml.internal.version.FileVersion;
import org.apache.isis.core.commons.exceptions.IsisException;
import org.apache.isis.core.metamodel.adapter.ObjectAdapter;
import org.apache.isis.core.metamodel.facets.collections.modify.CollectionFacet;
import org.apache.isis.core.metamodel.facets.collections.modify.CollectionFacetUtils;
import org.apache.isis.core.metamodel.spec.ObjectSpecification;
import org.apache.isis.runtimes.dflt.runtime.persistence.oidgenerator.simple.SerialOid;

/**
 * A logical collection of elements of a specified type
 */
public class ObjectData extends Data {
	private final Map<String, Object> fieldById;

	public ObjectData(
			final ObjectSpecification noSpec, 
			final SerialOid oid,
			final FileVersion version) {
		super(noSpec, oid, version);
		fieldById = new HashMap<String, Object>();
	}

	public Iterable<String> fields() {
		return fieldById.keySet();
	}

	//////////////////////////////////////////////////////////
	// id
	//////////////////////////////////////////////////////////

	public String id(final String fieldId) {
		final Object field = get(fieldId);
		return field == null ? null : "" + ((SerialOid) field).getSerialNo();
	}

	
	//////////////////////////////////////////////////////////
	// value
	//////////////////////////////////////////////////////////

	public void set(final String fieldId, final String value) {
		fieldById.put(fieldId, value);
	}

	public void saveValue(final String fieldId, final boolean isEmpty,
			final String encodedString) {
		if (isEmpty) {
			fieldById.remove(fieldId);
		} else {
			fieldById.put(fieldId, encodedString);
		}
	}

	public String value(final String fieldId) {
		return (String) get(fieldId);
	}

	
	//////////////////////////////////////////////////////////
	// reference
	//////////////////////////////////////////////////////////

	public Object get(final String fieldId) {
		return fieldById.get(fieldId);
	}

	public void set(final String fieldId, final Object oid) {
		if (oid == null) {
			fieldById.remove(fieldId);
		} else {
			fieldById.put(fieldId, oid);
		}
	}

	//////////////////////////////////////////////////////////
	// collection
	//////////////////////////////////////////////////////////

	public void initCollection(final String fieldId) {
		fieldById.put(fieldId, new ReferenceVector());
	}

	public void addElement(final String fieldId, final SerialOid elementOid) {
		if (!fieldById.containsKey(fieldId)) {
			throw new IsisException("Field " + fieldId
					+ " not found  in hashtable");
		}

		final ReferenceVector v = (ReferenceVector) fieldById.get(fieldId);
		v.add(elementOid);
	}

	public ReferenceVector elements(final String fieldId) {
		return (ReferenceVector) fieldById.get(fieldId);
	}

	public void addAssociation(final ObjectAdapter fieldContent,
			final String fieldId, final boolean ensurePersistent) {
		final boolean notAlreadyPersistent = fieldContent != null
				&& fieldContent.isTransient();
		if (ensurePersistent && notAlreadyPersistent) {
			throw new IllegalStateException(
					"Cannot save an object that is not persistent: "
							+ fieldContent);
		}
		// LOG.debug("adding reference field " + fieldId +" " + fieldContent);
		set(fieldId, fieldContent == null ? null : fieldContent.getOid());
	}

	public void addInternalCollection(final ObjectAdapter collection,
			final String fieldId, final boolean ensurePersistent) {
		/*
		 * if (ensurePersistent && collection != null && collection.getOid() ==
		 * null) { throw new
		 * IllegalStateException("Cannot save a collection that is not persistent: "
		 * + collection); }
		 */

		initCollection(fieldId);

		// int size = collection.size();

		final CollectionFacet facet = CollectionFacetUtils
				.getCollectionFacetFromSpec(collection);
		for (ObjectAdapter element : facet.iterable(collection)) {
			// LOG.debug("adding element to internal collection field " +
			// fieldId +" " + element);
			final Object elementOid = element.getOid();
			if (elementOid == null) {
				throw new IllegalStateException("Element is not persistent "
						+ element);
			}

			addElement(fieldId, (SerialOid) elementOid);
		}

		/*
		 * while (e.hasMoreElements()) { ObjectAdapter element = (ObjectAdapter)
		 * e.nextElement(); //
		 * LOG.debug("adding element to internal collection field " + fieldId
		 * +" " + element); Object elementOid = element.getOid(); if (elementOid
		 * == null) { throw new IllegalStateException("Element is not persistent
		 * "+element); }
		 * 
		 * addElement(fieldId, (SimpleOid) elementOid); }
		 */}


	//////////////////////////////////////////////////////////
	// toString
	//////////////////////////////////////////////////////////

	@Override
	public String toString() {
		return "ObjectData[type=" + getTypeName() + ",oid=" + getOid()
				+ ",fields=" + fieldById + "]";
	}


}