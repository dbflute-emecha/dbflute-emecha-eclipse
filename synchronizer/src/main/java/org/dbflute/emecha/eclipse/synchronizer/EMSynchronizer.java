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
    // The plug-in ID
    public static final String PLUGIN_ID = "org.dbflute.emecha.synchronizer"; //$NON-NLS-1$

    // The shared instance
    public static EMSynchronizer plugin;

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    private HttpServer server;

    private ExecutorService threadPool;

    private int port;

    // ===================================================================================
    //                                                                         Constructor
    //                                                                         ===========
    /**
     * The constructor
     */
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
        plugin = null;
        serverStop();
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
        final int primaryPort = getPreferencePort();
        final String hostname = "localhost";
        try {
            final HttpServer server = doServerStart(primaryPort, hostname);
            info("Synchronizer server is started at Port: " + primaryPort);
            this.server = server;
        } catch (IOException e) {
            handlePrimaryPortError(primaryPort, hostname, e);
        }
    }

    protected int getPreferencePort() {
        return plugin.getPreferenceStore().getInt(PreferenceConstants.P_LISTEN_PORT);
    }

    protected HttpServer doServerStart(int serverPort, String hostname) throws IOException {
        plugin.port = serverPort;
        final HttpServer server = HttpServer.create(new InetSocketAddress(hostname, serverPort), 0);
        threadPool = Executors.newFixedThreadPool(1);
        server.setExecutor(threadPool);
        server.createContext("/", new RefreshHandler());
        server.createContext("/refresh", new RefreshHandler());
        server.start();
        return server;
    }

    protected boolean isDefalutPort(int serverPort) {
        return serverPort == PreferenceConstants.DEFAULT_LISTEN_PORT;
    }

    protected int getSecondaryPort() {
        return PreferenceConstants.SECONDARY_LISTEN_PORT;
    }

    protected void handlePrimaryPortError(int primaryPort, String hostname, IOException originalEx) {
        if (isDefalutPort(primaryPort)) {
            try {
                retryBySecondaryPort(hostname);
            } catch (IOException ignored) {
                closeError(primaryPort);
            }
        } else {
            closeError(primaryPort);
        }
    }

    protected void retryBySecondaryPort(String hostname) throws IOException {
        final int secondaryPort = getSecondaryPort();
        final HttpServer retryServer = doServerStart(secondaryPort, hostname);
        info("Synchronizer server is started at Secondary Port: " + secondaryPort);
        this.server = retryServer;
    }

    protected void closeError(int primaryPort) {
        error("Synchronizer server is not started by error. (Port:" + primaryPort + ")");
        this.server = null;
    }

    // -----------------------------------------------------
    //                                                  Stop
    //                                                  ----
    public void serverStop() throws InterruptedException {
        if (server != null) {
            server.stop(10);
        }
        if (threadPool != null && !threadPool.isShutdown()) {
            threadPool.shutdownNow();
            threadPool.awaitTermination(60, TimeUnit.SECONDS);
        }
        threadPool = null;
        server = null;
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
        EMSynchronizer synchronizer = getDefault();
        try {
            synchronizer.serverStop();
            synchronizer.serverStart();
        } catch (InterruptedException e) {
            synchronizer.getLog().log(new Status(IStatus.ERROR, PLUGIN_ID, "Synchronizer server stop error.", e));
        }
    }

    public static int getServerPort() {
        return plugin.port;
    }

    /**
     * Returns the shared instance
     * @return the shared instance
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
        return imageDescriptorFromPlugin(plugin.getPluginId(), path);
    }
}
