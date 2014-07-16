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

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Basic class working as an interface to plug in any database.
 * This class is compliance to the Bridge design pattern.
 */

public abstract class DataModelImpl {

    /**
     * Creates a new database with given name.
     * @param dbName Name for the database to be created.
     * @throws Exception
     */
    public abstract void createDatabase(String dbName) throws Exception;

    /**
     * Creates a new table inside currently activated database. If no database
     * is active, will throw an exception.
     *
     * @param tableName Name of the table to be created.
     * @throws Exception
     */
    public abstract void createTable(String tableName) throws Exception;

    /**
     * Creates a new table inside given database.
     *
     * @param dbName Name of the database/keyspace the table/columnfamily is to be created.
     * @param tableName Name of the table/columnfamily
     * @throws Exception
     */
    public abstract void createTable(String dbName, String tableName)
            throws Exception;

    /**
     * Inserts a record to a table. Assuming working with a RDBMS database.
     *
     * @param dbName Name of the database/keyspace.
     * @param tableName Name of the table/columnfamily.
     * @param columnName Column name or key
     * @param value The value to be stored in the record.
     * @throws Exception
     */
    public abstract void insertItem(String dbName, String tableName,
                                    String columnName, String value) throws Exception;

    /**
     * Inserts a record to a table. Assuming working with a NoSQL database.
     *
     * @param dbName Name of the keyspace.
     * @param tableName Name of the columnfamily.
     * @param rowID Key for the row.
     * @param columnName Key for the column.
     * @param value Value for the record.
     * @throws Exception
     */
    public abstract void insertItem(String dbName, String tableName,
                                    String rowID, String columnName, String value) throws Exception;

    /**
     * Inserts several items at a single time.
     *
     * @param dbName Name of the keyspace.
     * @param tableName Name of the columnfamily.
     * @param items Array of items to be included rowkey, columnkey and columnvalue as consecutive
     * 		        items. Size of the array must always multiplier of 3.
     * @throws Exception Hector Exception
     */
    public abstract void insertItems(String dbName, String tableName, String[] items) throws Exception;

    /**
     * Inserts several items at a single time by using given list.
     *
     * @param dbName Name of the keyspace.
     * @param tableName Name of the columnfamily.
     * @param items List of items to be included rowkey, columnkey and columnvalue as consecutive
     * 		        items. Size of the list must always multiplier of 3.
     * @throws Exception Hector Exception
     */
    public abstract void insertItems(String dbName, String tableName, List<String> items) throws Exception;

    /**
     * Inserts items for a single row at once.
     *
     * @param dbName Name of the keyspace.
     * @param tableName Name of the columnfamily.
     * @param rowName ID of the row which items should be inserted
     * @param items map containing all the items.
     * @throws Exception Hector Exception
     */
    public abstract void insertRowItems(String dbName, String tableName, String rowName, Map<String, String> items) throws Exception;

    /**
     * Inserts several items into different column families at a single time as indicated in
     * given array. Array size must always be multiple of 4.
     *
     * @param dbName Name of the keyspace
     * @param items Name of the column family
     * @throws Exception String array of items to be included column-family, rowkey, columnkey and columnvalue
     * as consecutive items. Size must be multiple of 4.
     * @throws Exception Hector Exception
     */
    public abstract void insertItemsSparse(String dbName, String[] items) throws Exception;

    /**
     * Inserts several items into different column families at a single time as indicated in
     * given list. List size must always be multiple of 4.
     *
     * @param dbName Name of the keyspace
     * @param items Name of the column family
     * @throws Exception List of items to be included column-family, rowkey, columnkey and columnvalue
     * as consecutive items. Size must be multiple of 4.
     * @throws Exception Hector Exception
     */
    public abstract void insertItemsSparse(String dbName, List<String> items) throws Exception;

    /**
     * Deletes a single column from a table/columnfamily. Assuming it is working
     * in a NoSQL database.
     *
     * @param dbName Name of the keyspace.
     * @param tableName Name of the columnfamily.
     * @param rowID the row key. This must be exist.
     * @param columnName the column key. This also should be exist.
     * @throws Exception
     */
    public abstract void removeCellItem(String dbName, String tableName,
                                        String rowID, String columnName) throws Exception;

    /**
     * Deletes a single column from a table/columnfamily. Assuming it is working
     * in a NoSQL database.
     *
     * @param tableName Name of the columnfamily.
     * @param rowID the row key. This must be exist.
     * @param columnName the column key. This also should be exist.
     * @throws Exception
     */
    public abstract void removeCellItem(String tableName,
                                        String rowID, String columnName) throws Exception;

    /**
     * Removes bulk of items in different column families at once by given in the array of
     * strings.
     *
     * @param dbName Name of the keyspace.
     * @param items String array of items to be included column-family, rowkey, columnkey and columnvalue
     * as consecutive items. Size must be multiple of 3.
     * @throws Exception Hector Exception
     */
    public abstract void removeCellItemsSparse(String dbName, String[] items) throws Exception;

    /**
     * Removes bulk of items in different column families at once by given in the list of
     * strings.
     *
     * @param dbName Name of the keyspace.
     * @param items List of items to be included column-family, rowkey, columnkey and columnvalue
     * as consecutive items. Size must be multiple of 3.
     * @throws Exception Hector Exception
     */
    public abstract void removeCellItemsSparse(String dbName, List<String> items) throws Exception;

    /**
     * Fetches all columns in a single row in a NoSQL database.
     *
     * @param dbName Name of the database.
     * @param tableName Name of the column family.
     * @param rowID key of the row to be fetched.
     * @return Map<String, String> Key is the column key and value is the column value.
     * @throws Exception
     */
    public abstract Map<String, String> getValuesInSingleRow(String dbName,
                                                             String tableName, String rowID) throws Exception;

    /**
     * Fetches all columns in a single row in a NoSQL database.
     *
     * @param tableName Name of the column family.
     * @param rowID key of the row to be fetched.
     * @return Map<String, String> Key is the column key and value is the column value.
     * @throws Exception
     */
    public abstract Map<String, String> getValuesInSingleRow(String tableName,
                                                             String rowID) throws Exception;

    /**
     * Fetches all columns in a single row that falls in the given range (including the range margins) in a NoSQL 	database.
     *
     * @param dbName Name of the keyspace.
     * @param tableName Name of the column family.
     * @param rowID key of the row to be fetched.
     * @param rangeValue1 starting value of the range.
     * @param rangeValue2 ending value of the range.
     * @return Map<String, String> Key is the column key and value is the column value.
     * @throws Exception
     */
    public abstract Map<String, String> getValuesInSingleRowWithRange(String dbName,
                                                                      String tableName, String rowID, String rangeValue1, String rangeValue2) throws Exception;

    /**
     * Fetches all columns in a single row that falls in the given range (including the range margins) in a NoSQL 	database.
     *
     * @param tableName Name of the column family.
     * @param rowID key of the row to be fetched.
     * @param rangeValue1 starting value of the range.
     * @param rangeValue2 ending value of the range.
     * @return Map<String, String> Key is the column key and value is the column value.
     * @throws Exception
     */
    public abstract Map<String, String> getValuesInSingleRowWithRange(String tableName,
                                                                      String rowID, String rangeValue1,String rangeValue2) throws Exception;

    /**
     * Fetches all column keys in a single row in a NoSQL database.
     *
     * @param dbName Name of the keyspace.
     * @param tableName Name of the column family.
     * @param rowID key of the row to be fetched.
     * @return List<String> List of column keys.
     * @throws Exception
     */
    public abstract List<String> getColumnKeysInSingleRow(String dbName, String tableName,
                                                          String rowID) throws Exception;

    /**
     * Fetches all column keys in a single row in a NoSQL database.
     *
     * @param tableName Name of the column family.
     * @param rowID key of the row to be fetched.
     * @return List<String> List of column keys.
     * @throws Exception
     */
    public abstract List<String> getColumnKeysInSingleRow(String tableName,
                                                          String rowID) throws Exception;

    /**
     * Fetches given number of columns in a single row that falls in the given range either from beginning or end in a NoSQL database.
     *
     * @param dbName Name of the keyspace.
     * @param tableName Name of the column family.
     * @param rowID key of the row to be fetched.
     * @param range1 starting value of the range.
     * @param range2 ending value of the range.
     * @param numberOfColumns required number of columns.
     * @param isFirst wants from the start of the row or from the end of the row.
     * @return Map<String, String> Key is the column key and value is the column value.
     * @throws Exception
     */
    public abstract Map<String,String> getXnumberOfColumnsInSingleRow(String dbName, String tableName, String rowID,
                                                                      String range1, String range2, int numberOfColumns, boolean isFirst) throws Exception;

    /**
     * Fetches given number of columns in a single row that falls in the given range either from beginning or end in a NoSQL database.
     *
     * @param tableName Name of the column family.
     * @param rowID key of the row to be fetched.
     * @param range1 starting value of the range.
     * @param range2 ending value of the range.
     * @param numberOfColumns required number of columns.
     * @param isFirst wants from the start of the row or from the end of the row.
     * @return Map<String, String> Key is the column key and value is the column value.
     * @throws Exception
     */
    public abstract Map<String, String> getXnumberOfColumnsInSingleRow(String tableName,String rowID, String range1,
                                                                       String range2,int numberOfColumns,boolean isFirst) throws Exception;

    /**
     * Fetches given number of column keys in a single row that falls in the given range either from beginning or end in a NoSQL database.
     *
     * @param dbName Name of the keyspace.
     * @param tableName Name of the column family.
     * @param rowID key of the row to be fetched.
     * @param range1 starting value of the range.
     * @param range2 ending value of the range.
     * @param numberOfColumns required number of columns.
     * @param isFirst wants from the start of the row or from the end of the row.
     * @return List<String> List of column keys
     * @throws Exception
     */
    public abstract List <String> getXnumberOfColumnsKeysInSingleRow(String dbName, String tableName, String rowID,
                                                                     String range1, String range2, int numberOfColumns, boolean isFirst) throws Exception;

    /**
     * Fetches given number of column keys in a single row that falls in the given range either from beginning or end in a NoSQL database.
     *
     * @param tableName Name of the column family.
     * @param rowID key of the row to be fetched.
     * @param range1 starting value of the range.
     * @param range2 ending value of the range.
     * @param numberOfColumns required number of columns.
     * @param isFirst wants from the start of the row or from the end of the row.
     * @return List<String> List of column keys
     * @throws Exception
     */
    public abstract List<String> getXnumberOfColumnsKeysInSingleRow(String tableName,String rowID, String range1,
                                                                    String range2,int numberOfColumns,boolean isFirst) throws Exception;

    /**
     * Count the number of columns in a single row that falls in the given range in a NoSQL database.
     *
     * @param dbName Name of the keyspace.
     * @param tableName Name of the column family.
     * @param rowID key of the row to be fetched.
     * @param range1 starting value of the range.
     * @param range2 ending value of the range.
     * @return int number of columns
     * @throws Exception
     */
    public abstract int CountColumnsInSingleRow(String dbName,String tableName, String rowID,
                                                String range1, String range2) throws Exception;

    /**
     * Count the number of columns in a single row that falls in the given range in a NoSQL database.
     *
     * @param tableName Name of the column family.
     * @param rowID key of the row to be fetched.
     * @param range1 starting value of the range.
     * @param range2 ending value of the range.
     * @return int number of columns
     * @throws Exception
     */
    public abstract int CountColumnsInSingleRow(String tableName, String rowID,
                                                String range1, String range2) throws Exception;


    /**
     * Fetches a single column from a NoSQL database.
     *
     * @param dbName Name of the keyspace.
     * @param tableName Name of the column family.
     * @param rowID key of the row which contains the column to be fetched.
     * @param columnID column key to be fetched.
     * @return String the content of the specified column
     * @throws Exception
     */
    public abstract String getValueInCell(String dbName, String tableName,
                                          String rowID, String columnID) throws Exception;

    /**
     * Fetches a single column from a NoSQL database.
     *
     * @param tableName Name of the column family.
     * @param rowID key of the row which contains the column to be fetched.
     * @param columnID column key to be fetched.
     * @return String the content of the specified column
     * @throws Exception
     */
    public abstract String getValueInCell(String tableName, String rowID,
                                          String columnID) throws Exception;

    /**
     * Fetches a sequence of column values specified within given range assuming
     * it is working within a NoSQL database.
     *
     * @param dbName Name of the keyspace.
     * @param tableName Name of the column family.
     * @param rowID key of the row.
     * @param columnIDRangeStart column key for the start of range.
     * @param columnIDRangeEnd column key for the end of range.
     * @return List list of string containing all the values of columns are being fetching.
     * @throws Exception
     */
    public abstract List<String> getValuesInCellRange(String dbName,
                                                      String tableName, String rowID, String columnIDRangeStart,
                                                      String columnIDRangeEnd) throws Exception;

    /**
     * Fetches a sequence of column values specified within given range assuming
     * it is working within a NoSQL database.
     *
     * @param tableName Name of the column family.
     * @param rowID key of the row.
     * @param columnIDRangeStart column key for the start of range.
     * @param columnIDRangeEnd column key for the end of range.
     * @return List list of string containing all the values of columns are being fetching.
     * @throws Exception
     */
    public abstract List<String> getValuesInCellRange(String tableName,
                                                      String rowID, String columnIDRangeStart, String columnIDRangeEnd)
            throws Exception;

    /**
     * Fetches the last value in a given row in NoSQL database.
     *
     * @param dbName Name of the keyspace.
     * @param tableName Name of the column family.
     * @param rowID key for the row to be fetched.
     * @return String column value of the last column in the given row.
     * @throws Exception
     */
    public abstract String getValueInLastCell(String dbName, String tableName,
                                              String rowID) throws Exception;

    /**
     * Fetches the last value in a given row in NoSQL database.
     *
     * @param tableName Name of the column family.
     * @param rowID key for the row to be fetched.
     * @return String column value of the last column in the given row.
     * @throws Exception
     */
    public abstract String getValueInLastCell(String tableName, String rowID)
            throws Exception;

    /**
     * Fetches the last key in a given row in NoSQL database.
     *
     * @param dbName Name of the Keyspace
     * @param tableName Name of the column family.
     * @param rowID key for the row to be fetched.
     * @return String column key of the last column in the given row.
     * @throws Exception
     */
    public abstract String getKeyInLastCell(String dbName, String tableName, String rowID)
            throws Exception;

    /**
     * Fetches the last key in a given row in NoSQL database.
     *
     * @param tableName Name of the column family.
     * @param rowID key for the row to be fetched.
     * @return String column key of the last column in the given row.
     * @throws Exception
     */
    public abstract String getKeyInLastCell(String tableName,
                                            String rowID) throws Exception;

    /**
     * Fetches the first value in a given row in NoSQL database.
     *
     * @param dbName Name of the keyspace.
     * @param tableName Name of the column family.
     * @param rowID key for the row to be fetched.
     * @return String column value of the first column in the given row.
     * @throws Exception
     */
    public abstract String getValueInFirstCell(String dbName, String tableName,
                                               String rowID) throws Exception;

    /**
     * Fetches the first value in a given row in NoSQL database.
     *
     * @param tableName Name of the column family.
     * @param rowID key for the row to be fetched.
     * @return String column value of the first column in the given row.
     * @throws Exception
     */
    public abstract String getValueInFirstCell(String tableName, String rowID)
            throws Exception;


    /**
     * Fetches set of column keys, splitting by a given delimiter in the key in a NoSQL database.
     *
     * @param dbName Name of the keyspace.
     * @param tableName Name of the column family.
     * @param rowID key of the row.
     * @param delimiter string to be split from.
     * @return Set<String> Set of column keys
     * @throws Exception
     */
    public abstract Set<String> getColumnKeysBySplit (String dbName, String tableName,
                                                      String rowID, String delimiter) throws Exception;

    /**
     * Fetches set of column keys, splitting by a given delimiter in the key in a NoSQL database.
     *
     * @param tableName Name of the column family.
     * @param rowID key of the row.
     * @param delimiter string to be split from.
     * @return Set<String> Set of column keys
     * @throws Exception
     */
    public abstract Set<String> getColumnKeysBySplit(String tableName,String rowID,
                                                     String delimiter) throws Exception;

    /**
     * Fetches all row ID's of a given Column Family in a NoSQL database.
     *
     * @param dbName Name of the keyspace
     * @param tableName Name of the column family
     * @return List<String> List of row ID's
     * @throws Exception
     */
    public abstract List<String> getAllRowsOfAGivenCF (String dbName, String tableName)
            throws Exception;

    /**
     * Fetches all row ID's of a given Column Family in a NoSQL database.
     *
     * @param tableName Name of the column family
     * @return List<String> List of row ID's
     * @throws Exception
     */
    public abstract List<String> getAllRowsOfAGivenCF (String tableName) throws Exception;

}