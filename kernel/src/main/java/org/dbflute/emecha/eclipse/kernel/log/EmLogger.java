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
package org.dbflute.emecha.eclipse.kernel.log;

import org.dbflute.emecha.eclipse.log.EmLog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;

/**
 * EMecha Logger
 * @author schatten
 */
public final class EmLogger implements EmLog {

    private Plugin plugin;

    public EmLogger(Plugin plugin) {
        this.plugin = plugin;
    }

    public static void log(Plugin plugin, Throwable throwable) {
        String message = throwable.getMessage();
        if (message == null) {
            message = throwable.getClass().getName();
        }
        printLog(plugin, IStatus.ERROR, IStatus.ERROR, message, throwable);
    }

    public static boolean isDebug() {
        // TODO debug mode handling
        return true;
    }

    /* (非 Javadoc)
     * @see org.dbflute.emecha.eclipse.log.EmLog#debug(java.lang.String)
     */
    @Override
    public void debug(String message) {
        debug(message, IStatus.OK);
    }

    /* (非 Javadoc)
     * @see org.dbflute.emecha.eclipse.log.EmLog#debug(java.lang.String, int)
     */
    @Override
    public void debug(String message, int code) {
        if (isDebug()) {
            info(message, code);
        }
    }

    /* (非 Javadoc)
     * @see org.dbflute.emecha.eclipse.log.EmLog#info(java.lang.String)
     */
    @Override
    public void info(String message) {
        info(message, IStatus.OK);
    }

    /* (非 Javadoc)
     * @see org.dbflute.emecha.eclipse.log.EmLog#info(java.lang.String, int)
     */
    @Override
    public void info(String message, int code) {
        printLog(IStatus.INFO, code, message, null);
    }

    /* (非 Javadoc)
     * @see org.dbflute.emecha.eclipse.log.EmLog#warn(java.lang.String)
     */
    @Override
    public void warn(String message) {
        warn(message, null, IStatus.WARNING);
    }

    /* (非 Javadoc)
     * @see org.dbflute.emecha.eclipse.log.EmLog#warn(java.lang.String, java.lang.Throwable)
     */
    @Override
    public void warn(String message, Throwable throwable) {
        warn(message, throwable, IStatus.WARNING);
    }

    /* (非 Javadoc)
     * @see org.dbflute.emecha.eclipse.log.EmLog#warn(java.lang.String, int)
     */
    @Override
    public void warn(String message, int code) {
        warn(message, null, code);
    }

    /* (非 Javadoc)
     * @see org.dbflute.emecha.eclipse.log.EmLog#warn(java.lang.String, java.lang.Throwable, int)
     */
    @Override
    public void warn(String message, Throwable throwable, int code) {
        printLog(IStatus.WARNING, code, message, throwable);
    }

    /* (非 Javadoc)
     * @see org.dbflute.emecha.eclipse.log.EmLog#error(java.lang.String)
     */
    @Override
    public void error(String message) {
        error(message, null, IStatus.ERROR);
    }

    /* (非 Javadoc)
     * @see org.dbflute.emecha.eclipse.log.EmLog#error(java.lang.Throwable)
     */
    @Override
    public void error(Throwable throwable) {
        String message = throwable.getMessage();
        if (message == null) {
            message = throwable.getClass().getName();
        }
        error(message, throwable, IStatus.ERROR);
    }

    /* (非 Javadoc)
     * @see org.dbflute.emecha.eclipse.log.EmLog#error(java.lang.String, java.lang.Throwable)
     */
    @Override
    public void error(String message, Throwable throwable) {
        error(message, throwable, IStatus.ERROR);
    }

    /* (非 Javadoc)
     * @see org.dbflute.emecha.eclipse.log.EmLog#error(java.lang.String, int)
     */
    @Override
    public void error(String message, int code) {
        error(message, null, code);
    }

    /* (非 Javadoc)
     * @see org.dbflute.emecha.eclipse.log.EmLog#error(java.lang.Throwable, int)
     */
    @Override
    public void error(Throwable throwable, int code) {
        String message = throwable.getMessage();
        if (message == null) {
            message = throwable.getClass().getName();
        }
        error(message, throwable, code);
    }

    /* (非 Javadoc)
     * @see org.dbflute.emecha.eclipse.log.EmLog#error(java.lang.String, java.lang.Throwable, int)
     */
    @Override
    public void error(String message, Throwable throwable, int code) {
        printLog(IStatus.ERROR, code, message, throwable);
    }

    private void printLog(int severity, int code, String message, Throwable throwable) {
        printLog(plugin, severity, code, message, throwable);
    }
    private static void printLog(Plugin plugin, int severity, int code, String message, Throwable throwable) {
        if (plugin != null) {
            plugin.getLog().log(new Status(severity, plugin.getBundle().getSymbolicName(), code, message, throwable));
        }
    }

}
