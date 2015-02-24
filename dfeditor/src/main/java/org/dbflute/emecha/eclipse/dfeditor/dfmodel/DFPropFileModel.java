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
package org.dbflute.emecha.eclipse.dfeditor.dfmodel;

/**
 * DFProp file element model.
 */
public class DFPropFileModel extends AbstractModel implements NamedModel {

    protected String _fileName;
    private String _path;
    protected int _length = 0;
    protected boolean _missingHierarchy = false;
    private boolean _references = false;

    public DFPropFileModel() {
        super("DFPropFile");
    }

    public void setFileName(String name) {
        _fileName = name;
    }

    /**
     * @see org.dbflute.emecha.eclipse.dfeditor.dfmodel.NamedModel#getNameText()
     */
    public String getNameText() {
        return _fileName;
    }

    /**
     * Get file path.
     * @return file path
     */
    @Override
    public String getFilePath() {
        return _path;
    }

    /**
     * Set file path.
     * @param path file path
     */
    public void setFilePath(String path) {
        this._path = path;
    }

    public void setLength(int length) {
        _length = length;
    }

    /**
     * @see org.dbflute.emecha.eclipse.dfeditor.dfmodel.DFPropModel#getLength()
     */
    public int getLength() {
        return _length;
    }

    public boolean isMissingHierarchy() {
        return _missingHierarchy;
    }

    public void setMissingHierarchy(boolean status) {
        _missingHierarchy = status;
    }

    /**
     * {@inheritDoc}
     * @see org.dbflute.emecha.eclipse.dfeditor.dfmodel.DFPropModel#isReferences()
     */
    @Override
    public boolean isReferences() {
        return _references;
    }

    /**
     * 参照モードを設定する。
     * @param references 参照モード
     */
    public void setReferences(boolean references) {
        this._references = references;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("[").append(_stateName).append("]");
        String separator = "\n";
        for (DFPropModel child : _child) {
            str.append(separator);
            str.append(child.toString());
        }
        str.append(separator);
        str.append("[/").append(_stateName).append("]");
        return str.toString();
    }
}
