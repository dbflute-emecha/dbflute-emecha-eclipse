/*
 * Copyright 2015 the original author or authors.
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
package org.dbflute.emecha.eclipse.pmeditor.nls;

import org.eclipse.osgi.util.NLS;

/**
 * PMEditor Messages
 * @author schatten
 */
public class Messages extends NLS {
    public static final String BUNDLE_NAME = "org.dbflute.emecha.eclipse.pmeditor.nls.messages"; //$NON-NLS-1$
    public static String PmPreferencePage_description;
    public static String PmPreferencePage_HeaderStyle;
    public static String PmPreferencePage_TitleStyle;
    public static String PmPreferencePage_MetaMarkStyle;
    public static String PmPreferencePage_PropertyCommentStyle;
    public static String PmPreferencePage_SeparatorStyle;
    public static String PmPreferencePage_ParameterCommentStyle;

    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages() {
    }
}
