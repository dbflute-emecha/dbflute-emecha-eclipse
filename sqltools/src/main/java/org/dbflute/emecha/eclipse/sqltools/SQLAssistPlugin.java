package org.dbflute.emecha.eclipse.sqltools;

import org.dbflute.emecha.eclipse.AbstractEMechaPlugin;
import org.eclipse.jface.resource.ImageDescriptor;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class SQLAssistPlugin extends AbstractEMechaPlugin {

    // The plug-in ID
    public static final String PLUGIN_ID = "org.dbflute.emecha.sqltools"; //$NON-NLS-1$

    // The shared instance
    private static SQLAssistPlugin plugin;

    /**
     * The constructor
     */
    public SQLAssistPlugin() {
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
    public static SQLAssistPlugin getDefault() {
        return plugin;
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

    @Override
    public String getPluginId() {
        return PLUGIN_ID;
    }
}
