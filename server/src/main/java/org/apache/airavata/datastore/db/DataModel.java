/*
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
 */

package org.apache.airavata.datastore.db;

/**
 * Basic class for all data entities in the Data Store.
 * Any column family related classes must be extended from
 * this class.
 */

public class DataModel {

    /**
     * Delimiter is being used to separate two parts of a string
     */
    public static String PART_DELIMETER = "::";

    protected DataModelImpl dataModelImpl = null;

    public DataModel(DataModelImpl dbImpl) {
        this.dataModelImpl = dbImpl;
    }

    public DataModelImpl getDataModelImpl() {
        return dataModelImpl;
    }

}
