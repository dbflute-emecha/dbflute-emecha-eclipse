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
package org.dbflute.emecha.eclipse.synchronizer.handler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.dbflute.emecha.eclipse.synchronizer.nls.Messages;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.resources.WorkspaceJob;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubProgressMonitor;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

/**
 * Refresh Handler
 * @author schatten
 * @author jflute
 */
public class RefreshHandler implements HttpHandler {

    // ===================================================================================
    //                                                                          Definition
    //                                                                          ==========
    protected static final Map<String, Integer> STRING_TO_DEPTH = new HashMap<String, Integer>();
    static {
        STRING_TO_DEPTH.put("ZERO", IResource.DEPTH_ZERO); //$NON-NLS-1$
        STRING_TO_DEPTH.put("ONE", IResource.DEPTH_ONE); //$NON-NLS-1$
        STRING_TO_DEPTH.put("INFINITE", IResource.DEPTH_INFINITE); //$NON-NLS-1$
    }
    protected static final String NOT_FOUND_MARK = "*NotFound";

    // ===================================================================================
    //                                                                         Handle HTTP
    //                                                                         ===========
    /**
     * {@inheritDoc}
     * @see com.sun.net.httpserver.HttpHandler#handle(com.sun.net.httpserver.HttpExchange)
     */
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        final PrintWriter writer = new PrintWriter(new OutputStreamWriter(output, "UTF-8")); //$NON-NLS-1$
        try {
            final List<RefreshTask> list = new LinkedList<RefreshTask>();
            final IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
            final String query = exchange.getRequestURI().getQuery();
            boolean notFound = false;
            if (query != null) {
                final String[] params = query.split("&"); //$NON-NLS-1$
                for (String param : params) {
                    final int index = param.indexOf("="); //$NON-NLS-1$
                    final String name;
                    final String val;
                    if (index > -1) {
                        name = param.substring(0, index);
                        val = param.substring(index + 1);
                    } else {
                        name = param;
                        val = null;
                    }
                    final IResource resource = root.findMember(name);
                    if (resource != null && resource.exists()) {
                        list.add(createRefreshTask(resource, toDepth(val)));
                        writer.println(resource.getFullPath());
                    } else { // not found
                        writer.println(name + " " + NOT_FOUND_MARK);
                        notFound = true;
                    }
                }
            }
            if (list.isEmpty() && !notFound) { // all projects (means no specified)
                list.add(createRefreshTask(root, IResource.DEPTH_INFINITE));
                writer.println(Messages.MSG_ALL_RESOURCE);
            }
            new WorkspaceJob(Messages.MSG_REFRESH_RESOURCE) {
                @Override
                public IStatus runInWorkspace(IProgressMonitor monitor) throws CoreException {
                    monitor.beginTask(Messages.MSG_REFRESH_RESOURCE, list.size());
                    for (RefreshTask rt : list) {
                        rt.run(new SubProgressMonitor(monitor, 1));
                    }
                    monitor.done();
                    return Status.OK_STATUS;
                }
            }.schedule();
        } finally {
            writer.flush();
            exchange.sendResponseHeaders(200, output.size());
            final OutputStream response = exchange.getResponseBody();
            response.write(output.toByteArray());
            response.flush();
            response.close();
            exchange.close();
        }
    }

    protected int toDepth(String depthKey) {
        if (depthKey == null || depthKey.length() < 1) {
            return IResource.DEPTH_ONE;
        }
        final Integer depthInt = STRING_TO_DEPTH.get(depthKey.toUpperCase());
        return depthInt != null ? depthInt : IResource.DEPTH_ONE;
    }

    protected RefreshTask createRefreshTask(IResource resource, final int depth) {
        return new RefreshTask(resource, depth);
    }

    private class RefreshTask {
        protected final IResource resource;
        protected final int depth;

        public RefreshTask(IResource resource, int depth) {
            this.resource = resource;
            this.depth = depth;
        }

        public void run(IProgressMonitor monitor) throws CoreException {
            resource.refreshLocal(depth, monitor);
        }
    }
}
