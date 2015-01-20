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
package org.dbflute.emecha.eclipse.emsql;

import java.util.HashMap;
import java.util.Map;

/**
 * サポートしているデータベースの列挙
 * @author schatten
 */
public enum SupportDatabase {
    //-- Main Support
    MySQL("mysql"), PostgreSQL("postgresql"), Oracle("oracle"), DB2("db2"), SQLServer("mssql"), H2Database("h2"), ApacheDerby("derby"),
    //-- sub supported
    SQLite("sqlite"), MSAccess("msaccess"),
    //-- a-little-bit supported
    Sybase("sybase"), ;
    private final String dbName;
    private static final Map<String, SupportDatabase> databaseMap;
    static {
        databaseMap = new HashMap<String, SupportDatabase>();
        for (SupportDatabase database : SupportDatabase.values()) {
            databaseMap.put(database.getDbName().toLowerCase(), database);
        }
    }

    private SupportDatabase(String dbName) {
        this.dbName = dbName;
    }

    public String getDbName() {
        return this.dbName;
    }

    public static SupportDatabase nameOf(String name) {
        if (name == null) {
            return null;
        }
        return databaseMap.get(name.toLowerCase());
    }
}
