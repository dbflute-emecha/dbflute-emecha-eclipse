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
package org.dbflute.emecha.eclipse.synchronizer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.dbflute.emecha.eclipse.AbstractEMechaPlugin;
import org.dbflute.emecha.eclipse.synchronizer.handler.RefreshHandler;
import org.dbflute.emecha.eclipse.synchronizer.preferences.PreferenceConstants;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IStartup;
import org.osgi.framework.BundleContext;

import com.sun.net.httpserver.HttpServer;

/**
 * The activator class controls the plug-in life cycle
 * @author schatten
 * @author jflute
 */
public class EMSynchronizer extends AbstractEMechaPlugin implements IStartup {

    // ===================================================================================
    //                                                                          Definition
    //                                                                          ==========
    /** The plug-in ID */
    public static final String PLUGIN_ID = "org.dbflute.emecha.synchronizer"; //$NON-NLS-1$

    /** The shared instance */
    public static EMSynchronizer plugin;

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    // only used in shared instance
    private HttpServer server;
    private ExecutorService threadPool;
    private int port;

    // ===================================================================================
    //                                                                         Constructor
    //                                                                         ===========
    public EMSynchronizer() {
    }

    // ===================================================================================
    //                                                                  Plug-in Start/Stop
    //                                                                  ==================
    /**
     * {@inheritDoc}
     * @see org.eclipse.ui.IStartup#earlyStartup()
     */
    @Override
    public void earlyStartup() {
        serverStart(); // Startup plugin with Server
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
        serverStop();
        plugin = null;
        super.stop(context);
    }

    /**
     * {@inheritDoc}}
     * @see org.dbflute.emecha.eclipse.AbstractEMechaPlugin#getPluginId()
     */
    @Override
    public String getPluginId() {
        return PLUGIN_ID;
    }

    // ===================================================================================
    //                                                                   Server Start/Stop
    //                                                                   =================
    // -----------------------------------------------------
    //                                                 Start
    //                                                 -----
    public void serverStart() {
        final int serverPort = getPreferencePort();
        final String hostname = "localhost";
        try {
            final HttpServer server = doServerStart(serverPort, hostname);
            info("Synchronizer server was started at Port: " + serverPort);
            getDefault().server = server;
        } catch (IOException e) {
            handleServerStartError(serverPort, hostname, e);
        }
    }

    protected int getPreferencePort() {
        return getDefault().getPreferenceStore().getInt(PreferenceConstants.P_LISTEN_PORT);
    }

    protected HttpServer doServerStart(int serverPort, String hostname) throws IOException {
        getDefault().port = serverPort;
        final HttpServer server = HttpServer.create(newInetSocketAddress(serverPort, hostname), 0);
        getDefault().threadPool = Executors.newFixedThreadPool(1);
        server.setExecutor(plugin.threadPool);
        server.createContext("/", newRefreshHandler(serverPort));
        server.createContext("/refresh", newRefreshHandler(serverPort));
        server.start();
        return server;
    }

    protected InetSocketAddress newInetSocketAddress(int serverPort, String hostname) {
        return new InetSocketAddress(hostname, serverPort);
    }

    protected RefreshHandler newRefreshHandler(int serverPort) {
        return new RefreshHandler(serverPort);
    }

    protected void handleServerStartError(int serverPort, String hostname, IOException originalEx) {
        if (isPrimaryPort(serverPort)) {
            try {
                retryBySecondaryPort(hostname);
            } catch (IOException ignored) {
                try {
                    retryByTertiaryPort(hostname);
                } catch (IOException iignored) {
                    try {
                        retryByQuaternaryPort(hostname);
                    } catch (IOException iiignored) {
                        closeError(serverPort, originalEx);
                    }
                }
            }
        } else {
            closeError(serverPort, originalEx);
        }
    }

    protected boolean isPrimaryPort(int serverPort) {
        return serverPort == PreferenceConstants.PRIMARY_LISTEN_PORT;
    }

    protected void retryBySecondaryPort(String hostname) throws IOException {
        doRetryByQuaternaryPort(hostname, "secondary", PreferenceConstants.SECONDARY_LISTEN_PORT);
    }

    protected void retryByTertiaryPort(String hostname) throws IOException {
        doRetryByQuaternaryPort(hostname, "tertiary", PreferenceConstants.TERTIARY_LISTEN_PORT);
    }

    protected void retryByQuaternaryPort(String hostname) throws IOException {
        doRetryByQuaternaryPort(hostname, "quaternary", PreferenceConstants.QUATERNARY_LISTEN_PORT);
    }

    protected void doRetryByQuaternaryPort(String hostname, String title, int retryPort) throws IOException {
        final HttpServer retryServer = doServerStart(retryPort, hostname);
        info("Synchronizer server was started at " + title + " Port: " + retryPort);
        getDefault().server = retryServer;
    }

    protected void closeError(int primaryPort, IOException originalEx) {
        error("Synchronizer server was not started by error. (Port:" + primaryPort + ")");
        getDefault().server = null;
    }

    // -----------------------------------------------------
    //                                                  Stop
    //                                                  ----
    public void serverStop() throws InterruptedException {
        final EMSynchronizer synchronizer = getDefault();
        if (synchronizer.server != null) {
            synchronizer.server.stop(10);
        }
        if (synchronizer.threadPool != null && !synchronizer.threadPool.isShutdown()) {
            synchronizer.threadPool.shutdownNow();
            synchronizer.threadPool.awaitTermination(60, TimeUnit.SECONDS);
        }
        synchronizer.threadPool = null;
        synchronizer.server = null;
    }

    // ===================================================================================
    //                                                                        Small Helper
    //                                                                        ============
    protected void info(String msg) {
        getLog().log(new Status(IStatus.INFO, PLUGIN_ID, msg));
    }

    protected void error(String msg) {
        getLog().log(new Status(IStatus.ERROR, PLUGIN_ID, msg));
    }

    // ===================================================================================
    //                                                                       Static Facade
    //                                                                       =============
    public static void serverRestart() {
        final EMSynchronizer synchronizer = getDefault();
        try {
            synchronizer.serverStop();
            synchronizer.serverStart();
        } catch (InterruptedException e) {
            synchronizer.getLog().log(new Status(IStatus.ERROR, PLUGIN_ID, "Synchronizer server stop error.", e));
        }
    }

    public static int getServerPort() {
        return getDefault().port;
    }

    /**
     * Returns the shared instance
     * @return the static-cached instance
     */
    public static EMSynchronizer getDefault() {
        return plugin;
    }

    /**
     * Returns an image descriptor for the image file at the given plug-in relative path
     * @param path the path
     * @return the image descriptor
     */
    public static ImageDescriptor getImageDescriptor(String path) {
        return imageDescriptorFromPlugin(getDefault().getPluginId(), path);
    }
}
