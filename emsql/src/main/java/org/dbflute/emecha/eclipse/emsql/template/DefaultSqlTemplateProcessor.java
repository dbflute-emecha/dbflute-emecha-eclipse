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
package org.dbflute.emecha.eclipse.emsql.template;

import java.util.List;

/**
 * Default Sql Template Processor.
 * @author Schatten
 */
public class DefaultSqlTemplateProcessor implements ISqlTemplateProcessor {
    /** 改行コード */
    private String lineSeparator = System.getProperty("line.separator", "\n");

    /**
     * 改行コードを取得する。
     * @return 改行コード
     */
    protected String getLineSeparator() {
        return lineSeparator;
    }

    /**
     * {@inheritDoc}
     * @see org.dbflute.emecha.eclipse.emsql.template.ISqlTemplateProcessor#setLineSeparator(java.lang.String)
     */
    public void setLineSeparator(String lineSeparator) {
        this.lineSeparator = lineSeparator;
    }

    /**
     * {@inheritDoc}
     * @see org.dbflute.emecha.eclipse.emsql.template.ISqlTemplateProcessor#getSelectSqlTemplate()
     */
    public String getSelectSqlTemplate() {
        StringBuilder sql = new StringBuilder();
        sql.append("select ...");
        sql.append(getLineSeparator());
        sql.append("  from ...");
        sql.append(getLineSeparator());
        sql.append(" where ...");
        sql.append(getLineSeparator());
        sql.append(" order by ...");
        sql.append(getLineSeparator());
        return sql.toString();
    }

    /**
     * {@inheritDoc}
     * @see org.dbflute.emecha.eclipse.emsql.template.ISqlTemplateProcessor#getSelectPagingSqlTemplate()
     */
    public String getSelectPagingSqlTemplate() {
        StringBuilder sql = new StringBuilder();
        sql.append("/*IF pmb.isPaging()*/");
        sql.append(getLineSeparator());
        sql.append("select ...");
        sql.append(getLineSeparator());
        sql.append("-- ELSE select count(*)");
        sql.append(getLineSeparator());
        sql.append("/*END*/");
        sql.append(getLineSeparator());
        sql.append("  from ...");
        sql.append(getLineSeparator());
        sql.append(" where ...");
        sql.append(getLineSeparator());
        sql.append("/*IF pmb.isPaging()*/");
        sql.append(getLineSeparator());
        sql.append(" order by ...");
        sql.append(getLineSeparator());
        sql.append("/*END*/");
        sql.append(getLineSeparator());
        return sql.toString();
    }

    /**
     * {@inheritDoc}
     * @see org.dbflute.emecha.eclipse.emsql.template.ISqlTemplateProcessor#getInsertSqlTemplate()
     */
    public String getInsertSqlTemplate() {
        StringBuilder sql = new StringBuilder();
        sql.append("insert into ...");
        sql.append(getLineSeparator());
        return sql.toString();
    }

    /**
     * {@inheritDoc}
     * @see org.dbflute.emecha.eclipse.emsql.template.ISqlTemplateProcessor#getDeleteSqlTemplate()
     */
    public String getDeleteSqlTemplate() {
        StringBuilder sql = new StringBuilder();
        sql.append("delete from ...");
        sql.append(getLineSeparator());
        sql.append(" where ...");
        sql.append(getLineSeparator());
        return sql.toString();
    }

    /**
     * {@inheritDoc}
     * @see org.dbflute.emecha.eclipse.emsql.template.ISqlTemplateProcessor#getUpdateSqlTemplate()
     */
    public String getUpdateSqlTemplate() {
        StringBuilder sql = new StringBuilder();
        sql.append("update ...");
        sql.append(getLineSeparator());
        sql.append("   set ...");
        sql.append(getLineSeparator());
        sql.append(" where ...");
        sql.append(getLineSeparator());
        return sql.toString();
    }

    protected static String joinList(List<String> list, String separator) {
        StringBuilder sql = new StringBuilder();
        for (String string : list) {
            sql.append(string).append(separator);
        }
        return sql.toString();
    }
}
