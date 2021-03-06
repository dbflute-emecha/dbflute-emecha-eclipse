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
package org.dbflute.emecha.eclipse.dfeditor;

/**
 * Definition of dfprop partitioning and its partitions.
 * @since 3.1
 */
public interface DFPropPartitions {

    /** The identifier of the single-line end comment partition content type. */
    String DFP_COMMENT = "__dfp_comment";

    /** The identifier of the dfprop partitioning. */
    String DFP_PARTITIONING = "__dfp_partitioning";

    /** The identifier of the dfprop partitioning. */
    String DFP_LITERAL = "__dfp_literal";

}
