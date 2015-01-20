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

import java.util.ArrayList;
import java.util.List;

/**
 * DB2 のSqlを取得する。
 * @author
 */
public class DB2SqlTemplateProcessor extends DefaultSqlTemplateProcessor {
    /**
     * {@inheritDoc}
     * @see org.dbflute.emecha.eclipse.emsql.template.ISqlTemplateProcessor#getSelectPagingSqlTemplate()
     */
    public String getSelectPagingSqlTemplate() {
        List<String> list = new ArrayList<String>();
        list.add("/*IF pmb.isPaging()*/");
        list.add("select *");
        list.add("  from (");
        list.add("plain.*, row_number() over() as rn");
        list.add("  from (");
        list.add("select ...");
        list.add("-- ELSE select count(*)");
        list.add("/*END*/");
        list.add("  from ...");
        list.add(" where ...");
        list.add("/*IF pmb.isPaging()*/");
        list.add(" order by ...");
        list.add("       ) plain");
        list.add("       ) ext");
        list.add(" where ext.rn > /*pmb.pageStartIndex*/80");
        list.add("   and ext.rn <= /*pmb.pageEndIndex*/100");
        list.add("/*END*/");
        return joinList(list, getLineSeparator());
    }

}
