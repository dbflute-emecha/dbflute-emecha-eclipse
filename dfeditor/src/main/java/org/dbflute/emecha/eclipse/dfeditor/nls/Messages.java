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
package org.dbflute.emecha.eclipse.dfeditor.nls;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
    public static final String BUNDLE_NAME = "org.dbflute.emecha.eclipse.dfeditor.nls.messages"; //$NON-NLS-1$
    public static String DFPropPreferencePage_description;
    public static String DFPropPreferencePage_CommentStyle;
    public static String DFPropPreferencePage_LiteralMarkStyle;
    public static String DFPropPreferencePage_SearchMarkStyle;
    public static String DFPropPreferencePage_AliasMarkStyle;
    public static String DFPropPreferencePage_ValiableStyle;
    public static String DFPropPreferencePage_SQLCommentStyle;

    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages() {
    }
}
