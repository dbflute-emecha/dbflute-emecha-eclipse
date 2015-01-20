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
package org.dbflute.emecha.eclipse.emsql;

import org.dbflute.emecha.eclipse.AbstractEMechaPlugin;
import org.dbflute.emecha.eclipse.emsql.preferences.EMSqlPreferences;
import org.dbflute.emecha.eclipse.emsql.preferences.impl.EMSqlPreferencesImpl;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.resource.ImageDescriptor;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class EMSqlPlugin extends AbstractEMechaPlugin {

    // The plug-in ID
    public static final String PLUGIN_ID = "org.dbflute.emecha.emsql";

    // The shared instance
    public static AbstractEMechaPlugin plugin;

    /**
     * The constructor
     */
    public EMSqlPlugin() {
    }

    /*
     * (non-Javadoc)
     * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
     */
    public void start(BundleContext context) throws Exception {
        super.start(context);
        plugin = this;
    }

    /*
     * (non-Javadoc)
     * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
     */
    public void stop(BundleContext context) throws Exception {
        plugin = null;
        super.stop(context);
    }

    /**
     * Returns the shared instance
     *
     * @return the shared instance
     */
    public static AbstractEMechaPlugin getDefault() {
        return plugin;
    }

    /**
     * {@inheritDoc}}
     * @see org.dbflute.emecha.eclipse.AbstractEMechaPlugin#getPluginId()
     */
    @Override
    public String getPluginId() {
        return PLUGIN_ID;
    }

    /**
     * Returns an image descriptor for the image file at the given
     * plug-in relative path
     *
     * @param path the path
     * @return the image descriptor
     */
    public static ImageDescriptor getImageDescriptor(String path) {
        return imageDescriptorFromPlugin(plugin.getPluginId(), path);
    }

    /**
     * @param project
     * @return PreferenceStore (by the ScopedPreferenceStore with project scope)
     */
    public static EMSqlPreferences getProjectPreferences(IProject project) {
        return new EMSqlPreferencesImpl(project, plugin.getPluginId());
    }
}
