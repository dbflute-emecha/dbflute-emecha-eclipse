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
 * References Mapping Model
 * @author schatten
 */
public class DFPropReferenceModel implements NamedModel {

    private final NamedModel _local;
    private final NamedModel _reference;

    public DFPropReferenceModel(NamedModel local, NamedModel reference) {
        this._local = local;
        this._reference = reference;
    }

    /**
     * Get parent model of local element.
     * @see org.dbflute.emecha.eclipse.dfeditor.dfmodel.DFPropModel#getParent()
     */
    @Override
    public DFPropModel getParent() {
        return getSource().getParent();
    }

    /**
     * Get file path of local element.
     * @see org.dbflute.emecha.eclipse.dfeditor.dfmodel.DFPropModel#getFilePath()
     */
    @Override
    public String getFilePath() {
        return getParent().getFilePath();
    }

    /**
     * Get local element.
     * @return local element
     */
    public NamedModel getSource() {
        return _local;
    }

    /**
     * Get references element.
     * @return references element
     */
    public NamedModel getReferences() {
        return _reference;
    }

    /**
     * Get child  model of references element.
     * @see org.dbflute.emecha.eclipse.dfeditor.dfmodel.DFPropModel#getChild()
     */
    @Override
    public DFPropModel[] getChild() {
        return getReferences().getChild();
    }

    /**
     * Get offset of local element.
     * @see org.dbflute.emecha.eclipse.dfeditor.dfmodel.DFPropModel#getOffset()
     */
    @Override
    public int getOffset() {
        return getSource().getOffset();
    }

    /**
     * Get length of local element.
     * @see org.dbflute.emecha.eclipse.dfeditor.dfmodel.DFPropModel#getLength()
     */
    @Override
    public int getLength() {
        return getSource().getLength();
    }

    /**
     * Get line number of local element.
     * @see org.dbflute.emecha.eclipse.dfeditor.dfmodel.DFPropModel#getLineNumber()
     */
    @Override
    public int getLineNumber() {
        return getSource().getLineNumber();
    }

    /**
     * Whether or not a references of local element.
     * @see org.dbflute.emecha.eclipse.dfeditor.dfmodel.DFPropModel#isReferences()
     */
    @Override
    public boolean isReferences() {
        return getSource().isReferences();
    }

    /**
     * {@inheritDoc}
     * @see org.dbflute.emecha.eclipse.dfeditor.dfmodel.NamedModel#getNameText()
     */
    @Override
    public String getNameText() {
        return getSource().getNameText();
    }

}
