package org.dbflute.emecha.eclipse.kernel;

import org.dbflute.emecha.eclipse.AbstractEMechaPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class EMechaKernelPlugin extends AbstractEMechaPlugin {

    // The plug-in ID
    public static final String PLUGIN_ID = "org.dbflute.emecha"; //$NON-NLS-1$

    // The shared instance
    public static AbstractEMechaPlugin plugin;

    /**
     * The constructor
     */
    public EMechaKernelPlugin() {
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

}
