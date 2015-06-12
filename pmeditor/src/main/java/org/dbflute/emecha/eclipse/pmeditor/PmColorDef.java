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
package org.dbflute.emecha.eclipse.pmeditor;

import org.dbflute.emecha.eclipse.text.TextAttributeDefinition;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.RGB;

/**
 * PmEditor Text Font Color Definition
 * @author schatten
 */
public enum PmColorDef implements TextAttributeDefinition {

    DEFAULT(null, "default"),
    HEADER(new RGB(0, 0, 255), "header"),
    PARAMETER_COMMENT(new RGB(0, 128, 0), "parameterComment"),
    PROPERTY_COMMENT(new RGB(128, 128, 255), "propertyComment"),
    SEPARATOR(new RGB(0, 0, 255), "separator", SWT.BOLD),
    META_MARK(new RGB(0, 0, 200), "metaMark", SWT.BOLD | SWT.ITALIC);

    private RGB _foreground;
    private RGB _background = null;
    private String _key;
    private int _style = SWT.NORMAL;

    private PmColorDef(RGB foreground, String key) {
        this._foreground = foreground;
        this._key = key;
    }
    private PmColorDef(RGB foreground, String key, int style) {
        this._foreground = foreground;
        this._style = style;
        this._key = key;
    }

    /**
     * {@inheritDoc}
     * @see org.dbflute.emecha.eclipse.text.TextAttributeDefinition#getForeground()
     */
    @Override
    public RGB getForeground() {
        return _foreground;
    }

    public void setForeground(RGB _foreground) {
        this._foreground = _foreground;
    }

    /**
     * {@inheritDoc}
     * @see org.dbflute.emecha.eclipse.text.TextAttributeDefinition#getBackground()
     */
    @Override
    public RGB getBackground() {
        return _background;
    }

    public void setBackground(RGB _background) {
        this._background = _background;
    }

    /**
     * {@inheritDoc}
     * @see org.dbflute.emecha.eclipse.text.TextAttributeDefinition#getStyle()
     */
    @Override
    public int getStyle() {
        return _style;
    }

    public void setStyle(int _style) {
        this._style = _style;
    }

    /**
     * {@inheritDoc}
     * @see org.dbflute.emecha.eclipse.text.TextAttributeDefinition#getForegroundKey()
     */
    @Override
    public String getForegroundKey() {
        return "pmeditor.color." + _key + ".foreground";
    }

    /**
     * {@inheritDoc}
     * @see org.dbflute.emecha.eclipse.text.TextAttributeDefinition#getBackgroundKey()
     */
    @Override
    public String getBackgroundKey() {
        return "pmeditor.color." + _key + ".background";
    }

    /**
     * {@inheritDoc}
     * @see org.dbflute.emecha.eclipse.text.TextAttributeDefinition#getStyleKey()
     */
    @Override
    public String getStyleKey() {
        return "pmeditor.color." + _key + ".style";
    }
}
