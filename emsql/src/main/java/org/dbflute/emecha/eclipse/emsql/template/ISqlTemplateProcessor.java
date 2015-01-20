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

public interface ISqlTemplateProcessor {

    /**
     * 改行コードを設定する。
     * @param lineSeparator 改行コード
     */
    public abstract void setLineSeparator(String lineSeparator);

    /**
     * Select文の雛型を取得する。
     * @return Select Sql.
     */
    public abstract String getSelectSqlTemplate();

    /**
     * ページングを行うSelect文の雛型を取得する。
     * @return Select Paging Sql.
     */
    public abstract String getSelectPagingSqlTemplate();

    /**
     * Insert文の雛型を取得する。
     * @return Insert Sql.
     */
    public abstract String getInsertSqlTemplate();

    /**
     * Delete文の雛型を取得する。
     * @return Delete Sql.
     */
    public abstract String getDeleteSqlTemplate();

    /**
     * Update文の雛型を取得する。
     * @return Update Sql.
     */
    public abstract String getUpdateSqlTemplate();

}