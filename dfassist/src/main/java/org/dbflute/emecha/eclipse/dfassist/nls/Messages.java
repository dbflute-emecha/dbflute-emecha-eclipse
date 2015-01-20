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
package org.dbflute.emecha.eclipse.dfassist.nls;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
    private static final String BUNDLE_NAME = "org.dbflute.emecha.eclipse.dfassist.nls.messages"; //$NON-NLS-1$
    // ===================================================================================
    //                                                                          Hyper Link
    //                                                                          ==========
    public static String HYPERLINK_CUSTOMIZE_ENTITY;
    public static String HYPERLINK_PARAMETER_BEAN;
    public static String HYPERLINK_SQL_FILE;
    public static String QUICK_FIX_DERIVED_FIELD_PROPERTY;
    // ===================================================================================
    //                                                                  New Project Wizard
    //                                                                  ==================
    public static String NewProjectWizardTitle;
    public static String NewProjectWizardLocationTitle;
    public static String NewProjectWizardLocationDescription;
    public static String NewProjectWizardArtifactTitle;
    public static String NewProjectWizardArtifactDescription;
    public static String NewProjectWizardClientTitle;
    public static String NewProjectWizardClientDescription;
    public static String NewProjectWizardLocationPage_useDefaultLocation;
    public static String NewProjectWizardLocationPage_location;
    public static String NewProjectWizardLocationPage_locationBrowse;
    public static String NewProjectWizardLocationPage_dialog_location;
    public static String WorkingSetGroup_WorkingSets_group;
    public static String NewProjectWizardArtifact_artifactGroupTitle;
    public static String NewProjectWizardArtifact_groupId;
    public static String NewProjectWizardArtifact_artifactId;
    public static String NewProjectWizardArtifact_version;
    public static String NewProjectWizardArtifact_packaging;
    public static String NewProjectWizardArtifact_artifactName;
    public static String NewProjectWizardArtifact_artifactDescription;
    public static String NewProjectWizardArtifact_parentArtifactGroupTitle;
    public static String NewProjectWizardClient_dbfluteVersion;
    public static String NewProjectWizardClient_clientGroupTitle;
    public static String NewProjectWizardClient_clientProject;
    public static String NewProjectWizardClient_latestVersion;
    public static String NewProjectWizardClient_packageBase;
    public static String NewProjectWizardLocationPage_validatorProjectLocation;
    public static String NewProjectWizardLocationPage_validatorInvalidLocation;
    public static String NewProjectWizardPage_validatorRequired;
    public static String NewProjectWizardPage_validatorNospace;
    public static String NewProjectWizardPage_validatorInvalid;
    public static String NewProjectWizard_existsProjectTitle;
    public static String NewProjectWizard_existsProjectMessage;
    public static String NewProjectWizard_ProgressTitle;
    public static String NewProjectWizardTask_createProject;
    public static String NewProjectWizardTask_cancel;
    public static String NewProjectWizardClient_runtimeGroup;
    public static String NewProjectWizardClient_javaVersion;

    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages() {
    }
}
