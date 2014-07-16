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

import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.service.ColumnSliceIterator;
import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.beans.ColumnSlice;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.beans.OrderedRows;
import me.prettyprint.hector.api.beans.Row;
import me.prettyprint.hector.api.ddl.ColumnFamilyDefinition;
import me.prettyprint.hector.api.ddl.ComparatorType;
import me.prettyprint.hector.api.ddl.KeyspaceDefinition;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.mutation.Mutator;
import me.prettyprint.hector.api.query.ColumnQuery;
import me.prettyprint.hector.api.query.QueryResult;
import me.prettyprint.hector.api.query.RangeSlicesQuery;
import me.prettyprint.hector.api.query.SliceQuery;

import java.util.*;

/**
 * Concrete implementation for supporting Apache Cassandra database activities.
 * 
 */
public class CassandraDataModelImpl extends DataModelImpl {

	private Cluster cluster = null;
	public Keyspace keyspace = null;
	
	public CassandraDataModelImpl() {
        // Connect to the Cluster
		cluster = HFactory.getOrCreateCluster(ModelConfig.CASSANDRA_CLUSTER, ModelConfig.CLUSTER_PORT);

        // Define the (existing) Keyspace
		keyspace = HFactory.createKeyspace(ModelConfig.KEYSPACE, cluster);
	}

	@Override
	public void createDatabase(String dbName) throws Exception {
		KeyspaceDefinition newKeyspaceDef = HFactory
				.createKeyspaceDefinition(dbName);

		if ((cluster.describeKeyspace(dbName)) == null) {
			cluster.addKeyspace(newKeyspaceDef, true);
		}
	}

	@Override
	public void createTable(String tableName) throws Exception {
		this.createTable(ModelConfig.KEYSPACE, tableName);
	}

	@Override
	public void createTable(String dbName, String tableName) throws Exception {
		ColumnFamilyDefinition cfDef = HFactory.createColumnFamilyDefinition(
				dbName, tableName, ComparatorType.BYTESTYPE);
		
		Boolean isAlreadyExists = false;
		KeyspaceDefinition keyspaceDef = cluster.describeKeyspace(dbName);
		List<ColumnFamilyDefinition> columnFamilyList = keyspaceDef.getCfDefs();
		Iterator<ColumnFamilyDefinition> iterator = columnFamilyList.iterator();
		while (iterator.hasNext()) {
			if (iterator.next().getName().equals(cfDef.getName())) {
				isAlreadyExists = true;
				break;
			}
		}
		if (!isAlreadyExists) {
			cluster.addColumnFamily(cfDef);
		}
	}

	@Deprecated
	@Override
	public void insertItem(String dbName, String tableName, String columnName,
			String value) throws Exception {
		throw new Exception("Method not supported!");
	}

	@Override
	public void insertItem(String dbName, String tableName, String rowID,
			String columnName, String value) throws Exception {
		Keyspace keyspace = HFactory.createKeyspace(dbName, cluster);

		Mutator<String> mutator = HFactory.createMutator(keyspace,
				StringSerializer.get());
		mutator.addInsertion(rowID, tableName,
				HFactory.createStringColumn(columnName, value));
		mutator.execute();
	}

	@Override
	public void insertItems(String dbName, String tableName, String[] items)
			throws Exception {
		if (items == null)
			return;
		if (items.length % 3 != 0) {
			throw new Exception(
					"Number of items in the array must be a multiplier of 3!");
		}

		Keyspace keyspace = HFactory.createKeyspace(dbName, cluster);

		Mutator<String> mutator = HFactory.createMutator(keyspace,
				StringSerializer.get());
		for (int i = 0; i < items.length; i += 3) {
			if (items[i] == null)
				continue;

			mutator = mutator.addInsertion(items[i], tableName,
					HFactory.createStringColumn(items[i + 1], items[i + 2]));
		}
		mutator.execute();
	}

	@Override
	public void insertItems(String dbName, String tableName, List<String> items) throws Exception {
		if (items == null || items.size() == 0) return;
		if (items.size() % 3 != 0) {
			throw new Exception(
					"Number of items in the list must be a multiplier of 3!");
		}

		Keyspace keyspace = HFactory.createKeyspace(dbName, cluster);

		Mutator<String> mutator = HFactory.createMutator(keyspace,
				StringSerializer.get());
		for (int i = 0; i < items.size(); i += 3) {
			if (items.get(i) == null)
				continue;

			mutator = mutator.addInsertion(
					items.get(i),
					tableName,
					HFactory.createStringColumn(items.get(i + 1),
							items.get(i + 2)));
		}
		mutator.execute();

	}

    @Override
	public void insertItemsSparse(String dbName, String[] items)
			throws Exception {
		if (items == null)
			return;
		if (items.length % 4 != 0) {
			throw new Exception(
					"Number of items in the array must be a multiplier of 4!");
		}

		Keyspace keyspace = HFactory.createKeyspace(dbName, cluster);

		Mutator<String> mutator = HFactory.createMutator(keyspace,
				StringSerializer.get());
		for (int i = 0; i < items.length; i += 4) {
			if (items[i] == null)
				continue;

			mutator = mutator.addInsertion(items[i + 1], items[i],
					HFactory.createStringColumn(items[i + 2], items[i + 3]));
		}
		mutator.execute();
	}

	@Override
	public void insertItemsSparse(String dbName, List<String> items)
			throws Exception {
		if (items == null)
			return;
		if (items.size() % 4 != 0) {
			throw new Exception(
					"Number of items in the list must be a multiplier of 4!");
		}

		Keyspace keyspace = HFactory.createKeyspace(dbName, cluster);

		Mutator<String> mutator = HFactory.createMutator(keyspace,
				StringSerializer.get());
		for (int i = 0; i < items.size(); i += 4) {
			if (items.get(i) == null)
				continue;

			mutator = mutator.addInsertion(
					items.get(i + 1),
					items.get(i),
					HFactory.createStringColumn(items.get(i + 2),
							items.get(i + 3)));
		}
		mutator.execute();
	}


	@Override
	public void removeCellItem(String tableName, String rowID, String columnName)
			throws Exception {
		this.removeCellItem(ModelConfig.KEYSPACE, tableName, rowID, columnName);
	}

	@Override
	public void removeCellItem(String dbName, String tableName, String rowID,
			String columnName) throws Exception {
		Keyspace keyspace = HFactory.createKeyspace(dbName, cluster);

		Mutator<String> mutator = HFactory.createMutator(keyspace,
				StringSerializer.get());
		mutator.addDeletion(rowID, tableName, columnName,
				StringSerializer.get());
		mutator.execute();
	}

	@Override
	public void removeCellItemsSparse(String dbName, String[] items)
			throws Exception {
		if (items == null)
			return;
		if (items.length % 3 != 0)
			throw new Exception(
					"Number of items in the list must be a multiplier of 3!");

		Keyspace keyspace = HFactory.createKeyspace(dbName, cluster);

		Mutator<String> mutator = HFactory.createMutator(keyspace,
				StringSerializer.get());
		StringSerializer ss = StringSerializer.get();
		for (int i = 0; i < items.length; i += 3) {
			if (items[i] == null)
				continue;

			mutator.addDeletion(items[i + 1], items[i], items[i + 2], ss);
		}
		mutator.execute();
	}

	@Override
	public void removeCellItemsSparse(String dbName, List<String> items)
			throws Exception {
		if (items == null)
			return;
		if (items.size() % 3 != 0)
			throw new Exception(
					"Number of items in the list must be a multiplier of 3!");

		Keyspace keyspace = HFactory.createKeyspace(dbName, cluster);

		Mutator<String> mutator = HFactory.createMutator(keyspace,
				StringSerializer.get());
		StringSerializer ss = StringSerializer.get();
		for (int i = 0; i < items.size(); i += 3) {
			if (items.get(i) == null)
				continue;

			mutator.addDeletion(items.get(i + 1), items.get(i),
					items.get(i + 2), ss);
		}
		mutator.execute();
	}

    @Override
	public Map<String, String> getValuesInSingleRow(String dbName,
			String tableName, String rowID) throws Exception {
		Keyspace keyspace = HFactory.createKeyspace(dbName, cluster);

		Map<String, String> resultMap = new LinkedHashMap<String, String>();

		SliceQuery<String, String, String> query = HFactory.createSliceQuery(
				keyspace, StringSerializer.get(), StringSerializer.get(),
				StringSerializer.get());
		query.setColumnFamily(tableName).setKey(rowID)
				.setRange("", "", false, Integer.MAX_VALUE);
		QueryResult<ColumnSlice<String, String>> result = query.execute();

		for (HColumn<String, String> column : result.get().getColumns()) {
			resultMap.put(column.getName(), column.getValue());
		}
		return resultMap;
	}

    @Override
	public Map<String, String> getValuesInSingleRow(String tableName,
			String rowID) throws Exception {
		return this.getValuesInSingleRow(ModelConfig.KEYSPACE, tableName, rowID);
	}


    @Override
	public Map<String, String> getValuesInSingleRowWithRange(String dbName,
			String tableName, String rowID, String rangeValue1,
			String rangeValue2) throws Exception {
		Keyspace keyspace = HFactory.createKeyspace(dbName, cluster);

		Map<String, String> resultMap = new LinkedHashMap<String, String>();

		SliceQuery<String, String, String> query = HFactory.createSliceQuery(
				keyspace, StringSerializer.get(), StringSerializer.get(),
				StringSerializer.get());
		query.setColumnFamily(tableName).setKey(rowID)
				.setRange(rangeValue1, rangeValue2, false, Integer.MAX_VALUE);
		QueryResult<ColumnSlice<String, String>> result = query.execute();

		for (HColumn<String, String> column : result.get().getColumns()) {
			resultMap.put(column.getName(), column.getValue());

		}
		return resultMap;
	}

    @Override
	public Map<String, String> getValuesInSingleRowWithRange(String tableName,
            String rowID, String rangeValue1,
			String rangeValue2) throws Exception {
        return this.getValuesInSingleRowWithRange(ModelConfig.KEYSPACE, tableName,
                rowID, rangeValue1, rangeValue2);
    }

    @Override
	public List<String> getColumnKeysInSingleRow(String dbName,
			String tableName, String rowID) throws Exception {
		Keyspace keyspace = HFactory.createKeyspace(dbName, cluster);

		List<String> resultList = new ArrayList<String>();

		SliceQuery<String, String, String> query = HFactory.createSliceQuery(
				keyspace, StringSerializer.get(), StringSerializer.get(),
				StringSerializer.get());
		query.setColumnFamily(tableName).setKey(rowID)
				.setRange("", "", false, Integer.MAX_VALUE);
		QueryResult<ColumnSlice<String, String>> result = query.execute();

		for (HColumn<String, String> column : result.get().getColumns()) {
			resultList.add(column.getName());
		}
		return resultList;
	}

    @Override
	public List<String> getColumnKeysInSingleRow(String tableName,
             String rowID) throws Exception {
        return this.getColumnKeysInSingleRow(ModelConfig.KEYSPACE,tableName,rowID);
    }

    @Override
	public Map<String, String> getXnumberOfColumnsInSingleRow(String dbName,
			String tableName, String rowID, String range1, String range2,
			int numberOfColumns, boolean isFirst) throws Exception {
		Keyspace keyspace = HFactory.createKeyspace(dbName, cluster);

		Map<String, String> resultMap = new LinkedHashMap<String, String>();

		RangeSlicesQuery<String, String, String> rangeSlicesQuery = HFactory
				.createRangeSlicesQuery(keyspace, StringSerializer.get(),
						StringSerializer.get(), StringSerializer.get());
		rangeSlicesQuery.setColumnFamily(tableName);
		rangeSlicesQuery.setKeys(rowID, "");

		if (isFirst) {
			rangeSlicesQuery.setRange(range1, range2, false, numberOfColumns);
		} else {
			rangeSlicesQuery.setRange(range2, range1, true, numberOfColumns);
		}

		rangeSlicesQuery.setRowCount(1);

		QueryResult<OrderedRows<String, String, String>> result = rangeSlicesQuery
				.execute();
		OrderedRows<String, String, String> orderedRows = result.get();

		for (Row<String, String, String> r : orderedRows) {
			for (HColumn<String, String> column : r.getColumnSlice()
					.getColumns()) {
				resultMap.put(column.getName(), column.getValue());
			}
		}

		return resultMap;

	}

    @Override
	public Map<String, String> getXnumberOfColumnsInSingleRow(String tableName,
            String rowID, String range1, String range2,int numberOfColumns,
            boolean isFirst) throws Exception {
        return this.getXnumberOfColumnsInSingleRow(ModelConfig.KEYSPACE,
                tableName,rowID,range1,range2,numberOfColumns,isFirst);

    }

    @Override
	public List<String> getXnumberOfColumnsKeysInSingleRow(String dbName,
			String tableName, String rowID, String range1, String range2,
			int numberOfColumns, boolean isFirst) throws Exception {
		Keyspace keyspace = HFactory.createKeyspace(dbName, cluster);

		List<String> resultList = new ArrayList<String>();

		RangeSlicesQuery<String, String, String> rangeSlicesQuery = HFactory
				.createRangeSlicesQuery(keyspace, StringSerializer.get(),
						StringSerializer.get(), StringSerializer.get());
		rangeSlicesQuery.setColumnFamily(tableName);
		rangeSlicesQuery.setKeys(rowID, "");

		if (isFirst) {
			rangeSlicesQuery.setRange(range1, range2, false, numberOfColumns);
		} else {
			rangeSlicesQuery.setRange(range2, range1, true, numberOfColumns);
		}

		rangeSlicesQuery.setRowCount(1);

		QueryResult<OrderedRows<String, String, String>> result = rangeSlicesQuery
				.execute();
		OrderedRows<String, String, String> orderedRows = result.get();

		for (Row<String, String, String> r : orderedRows) {
			for (HColumn<String, String> column : r.getColumnSlice()
					.getColumns()) {
				resultList.add(column.getName());
			}
		}

		return resultList;

	}

    @Override
	public List<String> getXnumberOfColumnsKeysInSingleRow(String tableName,
            String rowID, String range1, String range2,int numberOfColumns,
            boolean isFirst) throws Exception {

        return this.getXnumberOfColumnsKeysInSingleRow(ModelConfig.KEYSPACE,tableName,
                rowID,range1,range2,numberOfColumns,isFirst);

    }

    @Override
    public int CountColumnsInSingleRow(String dbName,
			String tableName, String rowID, String range1, String range2)
             throws Exception {

       Keyspace keyspace = HFactory.createKeyspace(dbName, cluster);

       QueryResult<Integer> query = HFactory.createCountQuery(keyspace,
                StringSerializer.get(), StringSerializer.get()).
            setColumnFamily(tableName).
            setKey(rowID).
            setRange(range1, range2, Integer.MAX_VALUE).execute();
       return query.get().intValue();

     }

    @Override
    public int CountColumnsInSingleRow(String tableName, String rowID,
        String range1, String range2) throws Exception{

        return this.CountColumnsInSingleRow(ModelConfig.KEYSPACE,tableName,
                rowID,range1,range2);
    }


    @Override
	public String getValueInCell(String dbName, String tableName, String rowID,
			String columnID) throws Exception {
		Keyspace keyspace = HFactory.createKeyspace(dbName, cluster);

		ColumnQuery<String, String, String> cq = HFactory.createColumnQuery(
				keyspace, StringSerializer.get(), StringSerializer.get(),
				StringSerializer.get());

		cq.setColumnFamily(tableName).setKey(rowID).setName(columnID);
		return cq.execute().get().getValue();
	}


	@Override
	public String getValueInCell(String tableName, String rowID, String columnID)
			throws Exception {
		return this.getValueInCell(ModelConfig.KEYSPACE, tableName, rowID,
				columnID);
	}

	@Override
	public List<String> getValuesInCellRange(String dbName, String tableName,
			String rowID, String columnIDRangeStart, String columnIDRangeEnd)
			throws Exception {
		Keyspace keyspace = HFactory.createKeyspace(dbName, cluster);

		List<String> resultList = new ArrayList<String>();

		SliceQuery<String, String, String> query = HFactory.createSliceQuery(
				keyspace, StringSerializer.get(), StringSerializer.get(),
				StringSerializer.get());
		query.setColumnFamily(tableName).setKey(rowID);

		ColumnSliceIterator<String, String, String> iterator = new ColumnSliceIterator<String, String, String>(
				query, columnIDRangeStart, columnIDRangeEnd, false);

		while (iterator.hasNext()) {
			resultList.add(iterator.next().getValue());
		}

		return resultList;
	}

	@Override
	public List<String> getValuesInCellRange(String tableName, String rowID,
			String columnIDRangeStart, String columnIDRangeEnd)
			throws Exception {
		return this.getValuesInCellRange(ModelConfig.KEYSPACE, tableName,
				rowID, columnIDRangeStart, columnIDRangeEnd);
	}

    @Override
	public String getValueInLastCell(String dbName, String tableName,
			String rowID) throws Exception {
		Keyspace keyspace = HFactory.createKeyspace(dbName, cluster);

		SliceQuery<String, String, String> query = HFactory.createSliceQuery(
				keyspace, StringSerializer.get(), StringSerializer.get(),
				StringSerializer.get());
		query.setColumnFamily(tableName).setKey(rowID)
				.setRange("", "", true, 1);
		QueryResult<ColumnSlice<String, String>> result = query.execute();

		List<HColumn<String, String>> hlist = result.get().getColumns();
		if (hlist == null || hlist.size() == 0)
			return null;

		return (hlist.get(hlist.size() - 1).getValue());
	}

    @Override
	public String getValueInLastCell(String tableName, String rowID)
			throws Exception {
		return this.getValueInLastCell(ModelConfig.KEYSPACE, tableName, rowID);
	}

    @Override
    public String getKeyInLastCell(String dbName, String tableName, String rowID)
			throws Exception {
		Keyspace keyspace = HFactory.createKeyspace(dbName, cluster);

		SliceQuery<String, String, String> query = HFactory.createSliceQuery(
				keyspace, StringSerializer.get(), StringSerializer.get(),
				StringSerializer.get());
		query.setColumnFamily(tableName).setKey(rowID)
				.setRange("", "", true, 1);
		QueryResult<ColumnSlice<String, String>> result = query.execute();

		List<HColumn<String, String>> hlist = result.get().getColumns();
		if (hlist == null || hlist.size() == 0)
			return null;

		return (hlist.get(hlist.size() - 1).getName());
	}

	@Override
	public String getKeyInLastCell(String tableName, String rowID)
			throws Exception {
		return this.getKeyInLastCell(ModelConfig.KEYSPACE, tableName, rowID);
	}

    @Override
    public String getValueInFirstCell(String dbName, String tableName,
                String rowID) throws Exception {
            Keyspace keyspace = HFactory.createKeyspace(dbName, cluster);

            SliceQuery<String, String, String> query = HFactory.createSliceQuery(
                    keyspace, StringSerializer.get(), StringSerializer.get(),
                    StringSerializer.get());
            query.setColumnFamily(tableName).setKey(rowID)
                    .setRange("", "", false, 1);
            QueryResult<ColumnSlice<String, String>> result = query.execute();

            List<HColumn<String, String>> hlist = result.get().getColumns();
            if (hlist == null || hlist.size() == 0)
                return null;

            return (hlist.get(0).getValue());
    }

    @Override
    public String getValueInFirstCell(String tableName, String rowID)
                throws Exception {
            return this.getValueInFirstCell(ModelConfig.KEYSPACE, tableName, rowID);
     }

    @Override
	public Set<String> getColumnKeysBySplit(String dbName, String tableName,
			String rowID, String delimiter) throws Exception {
		Keyspace keyspace = HFactory.createKeyspace(dbName, cluster);

		Set<String> resultSet = new HashSet<String>();

		SliceQuery<String, String, String> query = HFactory.createSliceQuery(
				keyspace, StringSerializer.get(), StringSerializer.get(),
				StringSerializer.get());
		query.setColumnFamily(tableName).setKey(rowID);

		ColumnSliceIterator<String, String, String> iterator = new ColumnSliceIterator<String, String, String>(
				query, "", "", false);

		while (iterator.hasNext()) {
			resultSet.add((iterator.next().getName()).split(delimiter)[0]);
		}

		return resultSet;
	}

    @Override
	public Set<String> getColumnKeysBySplit(String tableName,String rowID,
            String delimiter) throws Exception {
        return this.getColumnKeysBySplit(ModelConfig.KEYSPACE, tableName,rowID,delimiter);
    }

	@Override
	public void insertRowItems(String dbName, String tableName, String rowName,
			Map<String, String> items) throws Exception {
		if (items == null) return;
		Keyspace keyspace = HFactory.createKeyspace(dbName, cluster);

		Mutator<String> mutator = HFactory.createMutator(keyspace, StringSerializer.get());
		for (String key : items.keySet()) {
			mutator = mutator.addInsertion(rowName, tableName,
					HFactory.createStringColumn(key, items.get(key)));
		}
		mutator.execute();
	}

    @Override
	public List<String> getAllRowsOfAGivenCF (String dbName, String tableName)
        throws Exception{
       Keyspace keyspace = HFactory.createKeyspace(dbName, cluster);

        List<String> resultList = new ArrayList<String>();

		RangeSlicesQuery<String, String, String> rangeSlicesQuery = HFactory
				.createRangeSlicesQuery(keyspace, StringSerializer.get(),
						StringSerializer.get(), StringSerializer.get());

		rangeSlicesQuery.setColumnFamily(tableName).setRange(null,null,false, Integer.MAX_VALUE)
                .setKeys(null, null);

        QueryResult<OrderedRows<String, String, String>> result = rangeSlicesQuery.execute();
        OrderedRows<String, String, String> orderedRows = result.get();

            for (Row<String, String, String> r : orderedRows) {
                    {
                       resultList.add(r.getKey());
                    }
                }
        return resultList;
    }

    @Override
	public List<String> getAllRowsOfAGivenCF (String tableName)
        throws Exception{
        return this.getAllRowsOfAGivenCF(ModelConfig.KEYSPACE,tableName);

    }




}
