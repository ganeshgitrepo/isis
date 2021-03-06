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

package org.apache.isis.core.progmodel.facets.object.membergroups;

import java.util.Properties;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;

import org.apache.isis.applib.annotation.MemberGroupLayout.ColumnSpans;
import org.apache.isis.core.metamodel.facetapi.FacetHolder;

public class MemberGroupLayoutFacetProperties extends MemberGroupLayoutFacetAbstract {

    public MemberGroupLayoutFacetProperties(
            final Properties properties,
            FacetHolder holder) {
        super(asColumnSpans(properties), 
                asListWithDefaultGroup(asGroupList(properties, "left")), 
                asList(asGroupList(properties, "middle")), 
                asList(asGroupList(properties, "right")), 
                holder);
    }

    static ColumnSpans asColumnSpans(Properties properties) {
        String columnSpansStr = properties.getProperty("columnSpans");
        
        if(columnSpansStr == null) {
            return null;
        }
        try {
            return ColumnSpans.valueOf(columnSpansStr.trim());
        } catch(IllegalArgumentException ex) {
            return null;
        }
    }

    static String[] asGroupList(Properties properties, final String key) {
        final String property = properties.getProperty(key);
        if(property == null) {
            return new String[0];
        }
        final Iterable<String> split = Splitter.on(',').split(property);
        return Iterables.toArray(
                    Iterables.filter(
                        Iterables.transform(
                                split, 
                                new Function<String,String>(){
                                    @Override
                                    public String apply(String input) {
                                        return input==null?"":input.trim();
                                    }
                                }), 
                        new Predicate<String>() {
                            @Override
                            public boolean apply(String input) {
                                return input!=null && !input.isEmpty();
                            }
                        }), 
                    String.class);
    }
}
