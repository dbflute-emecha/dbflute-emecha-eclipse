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
 * Map entry model.
 * <p>
 * map:{
 *    key = map:{}
 *    key = list:{}
 *    key = value
 * }
 * </p>
 */
public class MapEntryModel extends AbstractModel implements NamedModel {

    protected String _propertyName;
    protected int _length = 0;

    public MapEntryModel() {
        super("MapEntry");
    }

    public void setNameText(String name) {
        _propertyName = name;
    }

    public String getNameText() {
        return _propertyName;
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

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append(getToStringPrefix());
        str.append(getNameText());
        str.append(" = ");
        for (DFPropModel child : _child) {
            str.append(child.toString());
        }
        str.append(getToStringSuffix());
        return str.toString();
    }
}
