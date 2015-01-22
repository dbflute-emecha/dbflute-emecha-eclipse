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
package org.dbflute.emecha.eclipse.dfassist.jdt.derived;

public final class DerivedFieldInfo {
    private static final String LINE_SEPARATER = System.getProperty("line.separator", "\n"); //$NON-NLS-1$ //$NON-NLS-2$
    private static final String ALIAS_PREFIX = "ALIAS_"; //$NON-NLS-1$
    private static final String SPLIT_REGEX = "_|(?<=[A-Z])(?=[A-Z][a-z])|(?<=[a-z])(?=[A-Z])|(?<=[A-Za-z][0-9])(?=[A-Z])"; //$NON-NLS-1$
    private String _targetTypeName;
    private Class<?> _propertyType;
    private String _propertyTypeName;
    private String _propertyTypeSimpleName;
    private String _constantFieldName;
    private String _propertyName;
    private String _columnName;

    public String getTargetTypeName() {
        return _targetTypeName;
    }

    public void setTargetTypeName(String typeName) {
        _targetTypeName = typeName;
    }

    public String getPropertyTypeFullPackageName() {
        return _propertyType != null ? _propertyType.getName() : _propertyTypeName;
    }

    public String getPropertyTypeName() {
        return _propertyType != null ? _propertyType.getSimpleName() : _propertyTypeSimpleName;
    }

    public void setPropertyType(Class<?> type) {
        _propertyType = type;
        this._propertyTypeName = null;
        this._propertyTypeSimpleName = null;
    }

    public void setPropertyType(String propertyTypeName) {
        if (propertyTypeName != null) {
            this._propertyTypeName = propertyTypeName;
            this._propertyTypeSimpleName = propertyTypeName.substring(propertyTypeName.lastIndexOf(".") + 1);
        } else {
            this._propertyTypeName = null;
            this._propertyTypeSimpleName = null;
        }
        _propertyType = null;
    }

    public String getConstantFieldName() {
        return _constantFieldName;
    }

    public void setConstantFieldName(String fieldName) {
        _constantFieldName = fieldName;
        if (fieldName != null) {
            String property = fieldName;
            if (fieldName.startsWith(ALIAS_PREFIX)) {
                property = fieldName.substring(ALIAS_PREFIX.length());
            }
            String[] splitFieldName = property.split(SPLIT_REGEX);
            StringBuilder propName = new StringBuilder();
            StringBuilder columnName = new StringBuilder();
            int upper = 0;
            for (String str : splitFieldName) {
                if (upper > 0) {
                    propName.append(str.substring(0, upper).toUpperCase());
                    columnName.append("_");
                }
                propName.append(str.substring(upper).toLowerCase());
                columnName.append(str.toUpperCase());
                upper = 1;
            }
            _propertyName = propName.toString();
            _columnName = columnName.toString();
        }
    }

    public String getPropertyName() {
        return _propertyName;
    }

    public String getColumnName() {
        return _columnName;
    }

    /**
     * 作成する定数のソースコードを取得する。
     * @return 定数コード
     */
    protected String getConstantFieldSource() {
        StringBuilder disp = new StringBuilder();
        disp.append("/** ").append(this.getColumnName()).append(": Derived Referrer Alias. */").append(LINE_SEPARATER); //$NON-NLS-1$ //$NON-NLS-2$

        disp.append("public static final String "); //$NON-NLS-1$
        disp.append(this.getConstantFieldName());
        disp.append(" = \""); //$NON-NLS-1$
        disp.append(this.getColumnName());
        disp.append("\";"); //$NON-NLS-1$
        return disp.toString();
    }

    /**
     * 作成するフィールドのソースコードを取得する。
     * @return フィールドコード
     */
    protected String getPropertyFieldSource() {
        StringBuilder disp = new StringBuilder();
        disp.append("/** ").append(this.getColumnName()).append(": (Derived Referrer) */").append(LINE_SEPARATER); //$NON-NLS-1$ //$NON-NLS-2$

        disp.append("protected "); //$NON-NLS-1$
        disp.append(this.getPropertyTypeName());
        disp.append(" _").append(this.getPropertyName()).append(";"); //$NON-NLS-1$ //$NON-NLS-2$
        return disp.toString();
    }

    /**
     * 作成するGetterのソースコードを取得する。
     * @return Getterコード
     */
    protected String getGetterMethodSource() {
        StringBuilder disp = new StringBuilder();
        disp.append("/**").append(LINE_SEPARATER); //$NON-NLS-1$
        disp.append(" * [get] ").append(this.getColumnName()).append(": (Derived Referrer)").append(LINE_SEPARATER); //$NON-NLS-1$ //$NON-NLS-2$
        disp.append(" * @return The value of the column '").append(this.getColumnName()).append("'. (NullAllowed)").append(LINE_SEPARATER); //$NON-NLS-1$ //$NON-NLS-2$
        disp.append(" */").append(LINE_SEPARATER); //$NON-NLS-1$

        disp.append("public "); //$NON-NLS-1$
        disp.append(this.getPropertyTypeName());
        disp.append(" get"); //$NON-NLS-1$
        disp.append(String.valueOf(this.getPropertyName().charAt(0)).toUpperCase());
        disp.append(this.getPropertyName().substring(1));
        disp.append("() {").append(LINE_SEPARATER); //$NON-NLS-1$
        disp.append("    return _").append(this.getPropertyName()); //$NON-NLS-1$
        disp.append(";").append(LINE_SEPARATER).append("}"); //$NON-NLS-1$ //$NON-NLS-2$
        return disp.toString();
    }

    /**
     * 作成するSetterのソースコードを取得する。
     * @return Setterコード
     */
    protected String getSetterMethodSource() {
        StringBuilder disp = new StringBuilder();
        disp.append("/**").append(LINE_SEPARATER); //$NON-NLS-1$
        disp.append(" * [set] ").append(this.getColumnName()).append(": (Derived Referrer)").append(LINE_SEPARATER); //$NON-NLS-1$ //$NON-NLS-2$
        disp.append(" * @param ").append(this.getPropertyName()).append(" The value of the column '").append(this.getColumnName()) //$NON-NLS-1$ //$NON-NLS-2$
                .append("'. (NullAllowed)").append(LINE_SEPARATER); //$NON-NLS-1$
        disp.append(" */").append(LINE_SEPARATER); //$NON-NLS-1$

        disp.append("public void set"); //$NON-NLS-1$
        disp.append(String.valueOf(this.getPropertyName().charAt(0)).toUpperCase());
        disp.append(this.getPropertyName().substring(1));
        disp.append("("); //$NON-NLS-1$
        disp.append(this.getPropertyTypeName());
        disp.append(" ").append(this.getPropertyName()).append(") {").append(LINE_SEPARATER); //$NON-NLS-1$ //$NON-NLS-2$
        disp.append("    _").append(this.getPropertyName()); //$NON-NLS-1$
        disp.append(" = ").append(this.getPropertyName()).append(";").append(LINE_SEPARATER); //$NON-NLS-1$ //$NON-NLS-2$
        disp.append("}"); //$NON-NLS-1$
        return disp.toString();
    }

    public DerivedFieldInfo copy(Class<?> propertyType) {
        DerivedFieldInfo info = new DerivedFieldInfo();
        info._targetTypeName = this._targetTypeName;
        info._propertyType = propertyType;
        info._constantFieldName = this._constantFieldName;
        info._propertyName = this._propertyName;
        info._columnName = this._columnName;
        return info;
    }

    public DerivedFieldInfo copy(String propertyTypeName) {
        DerivedFieldInfo info = new DerivedFieldInfo();
        info._targetTypeName = this._targetTypeName;
        info._propertyTypeName = propertyTypeName;
        info._propertyTypeSimpleName = propertyTypeName.substring(propertyTypeName.lastIndexOf(".") + 1);
        info._constantFieldName = this._constantFieldName;
        info._propertyName = this._propertyName;
        info._columnName = this._columnName;
        return info;
    }
}
