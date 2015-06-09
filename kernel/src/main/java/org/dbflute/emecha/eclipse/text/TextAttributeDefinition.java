/*
 * Copyright 2015 the original author or authors.
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
package org.dbflute.emecha.eclipse.text;

import org.eclipse.swt.graphics.RGB;

/**
 * Text attribute definition interface
 * @author schatten
 */
public interface TextAttributeDefinition {

    /**
     * Get Foreground RGB
     * @return Foreground RGB
     */
    RGB getForeground();

    /**
     * Get Background RGB
     * @return Background RGB
     */
    RGB getBackground();

    /**
     * Get Font Style
     * @return Font Style
     * @see org.eclipse.swt.SWT
     */
    int getStyle();

    /**
     * Get Foreground store key
     * @return Foreground store key
     */
    String getForegroundKey();

    /**
     * Get Background store key
     * @return Background store key
     */
    String getBackgroundKey();

    /**
     * Get Font style store key
     * @return Font style store key
     */
    String getStyleKey();

}
