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

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class ModelConfig {
	private static final Logger logger = LogManager.getLogger(ModelConfig.class);
	
	public static String CASSANDRA_CLUSTER, CLUSTER_PORT, KEYSPACE;

    private static ModelProperties props = null;

    public static void loadProperties() {
        props = ModelProperties.getInstance();
        
        logger.debug("Cassandra properties: [cluster: " + props.getCassandraCluster() + ", port: " +
                props.getClusterPort() + ", keyspace: " + props.getKeySpace());
        
        CASSANDRA_CLUSTER = props.getCassandraCluster();
        CLUSTER_PORT = props.getClusterPort();
        KEYSPACE = props.getKeySpace();
    }

}
