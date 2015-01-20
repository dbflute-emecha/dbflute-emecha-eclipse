/*
 * Copyright 2014-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.dbflute.emecha.eclipse.synchronizer.nls;

import org.eclipse.osgi.util.NLS;

/**
 * see Messages.properties
 * @author schatten
 * @author jflute
 */
public class Messages extends NLS {

    static {
        final Class<Messages> clazz = Messages.class;
        NLS.initializeMessages(clazz.getName(), clazz);
    }

    public static String MSG_REFRESH_RESOURCE;
}
