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

package org.apache.isis.core.commons.lang;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.apache.isis.core.metamodel.exceptions.MetaModelException;

public class MethodExtensions {

    private MethodExtensions() {
    }

    public static boolean isPublic(final Method method) {
        return Modifier.isPublic(method.getModifiers());
    }

    public static boolean isStatic(final Method method) {
        return Modifier.isStatic(method.getModifiers());
    }

    // //////////////////////////////////////

    public static Object invoke(final Method method, final Object object) {
        final Object[] parameters = MethodExtensions.getNullOrDefaultArgs(method);
        return MethodExtensions.invoke(method, object, parameters);
    }

    public static Object invoke(final Method method, final Object object, final Object[] parameters) {
        try {
            return method.invoke(object, parameters);
        } catch (final IllegalArgumentException e) {
            throw e;
        } catch (final InvocationTargetException e) {
            ThrowableExtensions.throwWithinIsisException(e, "Exception executing " + method);
            return null;
        } catch (final IllegalAccessException e) {
            throw new MetaModelException("illegal access of " + method, e);
        }
    }

    public static Object invokeStatic(final Method method, final Object[] parameters) {
        return invoke(method, null, parameters);
    }

    public static Object invokeStatic(final Method method) {
        return invoke(method, null, MethodExtensions.getNullOrDefaultArgs(method));
    }

    // //////////////////////////////////////

    
    public static Object[] getNullOrDefaultArgs(final Method method) {
        final Class<?>[] paramTypes = method.getParameterTypes();
        final Object[] parameters = new Object[paramTypes.length];
        for (int i = 0; i < parameters.length; i++) {
            parameters[i] = ClassExtensions.getNullOrDefault(paramTypes[i]);
        }
        return parameters;
    }


}
