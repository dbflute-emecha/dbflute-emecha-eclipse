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
package org.dbflute.emecha.eclipse;

import org.dbflute.emecha.eclipse.kernel.log.EmLogger;
import org.dbflute.emecha.eclipse.log.EmLog;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.ui.plugin.AbstractUIPlugin;

public abstract class AbstractEMechaPlugin extends AbstractUIPlugin {

    public abstract String getPluginId();

    protected static EmLog getLogger(Plugin plugin) {
        return new EmLogger(plugin);
    }
}
