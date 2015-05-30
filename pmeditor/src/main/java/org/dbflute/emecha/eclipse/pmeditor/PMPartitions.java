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

/**
 * パーティション定数
 * @author schatten
 */
public interface PMPartitions {

    /** parameter comment partition */
    static String PM_PARAMETER = "__pm_parameter_comment";

    /** meta separate */
    static String PM_SEPARATE = "__pm_separate";

    /** head meta partition */
    static String PM_HEADER = "__pm_header";

    /** meta area separator */
    static String DEFAULT_SEPARATOR_TEXT = ">>>";

}
