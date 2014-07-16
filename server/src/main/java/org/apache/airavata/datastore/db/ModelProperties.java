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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class ModelProperties {

    private final Logger logger = LogManager.getLogger(ModelProperties.class);

    private static ModelProperties props = null;

    private static final String CASSANDRA_PROPERTY_FILE = "../conf/cassandra.properties";
    private static final String DEFAULT_CASSANDRA_PROPERTY_FILE = "conf/cassandra.properties";

    public static final String CASSANDRA_CLUSTER_NAME_PROPERTY = "cassandraCluster.name";
    public static final String CASSANDRA_CLUSTER_PORT_PROPERTY = "cassandraCluster.port";
    public static final String CASSANDRA_CLUSTER_KEYSPACE_PROPERTY = "cassandraCluster.keyspace";

    private String cassandraCluster = null,clusterPort = null,keyspace = null;

    private ModelProperties() {
        logger.debug("Trying to open [" + CASSANDRA_PROPERTY_FILE + "]");

        try {
            this.loadMessageBrokerProperties();
        } catch (Exception ex) {
            logger.fatal("Failed to load properties file!", ex);
        }
    }

    public static ModelProperties getInstance() {
        if (props == null) {
            props = new ModelProperties();
        }
        return props;
    }

    private InputStream getCassandraPropertiesURL() {
        InputStream file;
        //load cassandra server properties from conf directory.
        try {
            file = new FileInputStream(CASSANDRA_PROPERTY_FILE);
        } catch (FileNotFoundException ex) {
            file = null;
            logger.warn("Using default cassandra server properties from " + DEFAULT_CASSANDRA_PROPERTY_FILE);
        }

        if(file == null) {   // try to load default cassandra server properties from class loader.
            try {
                file =  ClassLoader.getSystemResource(DEFAULT_CASSANDRA_PROPERTY_FILE).openStream();
            } catch (IOException ex) {
                logger.fatal(ex);
            }
        }
        return file;
    }

    private void loadMessageBrokerProperties() throws Exception {
        Properties properties = new Properties();

        InputStream file = getCassandraPropertiesURL();
        properties.load(file);
        this.cassandraCluster = (String) properties.get(CASSANDRA_CLUSTER_NAME_PROPERTY);
        this.clusterPort = (String) properties.get(CASSANDRA_CLUSTER_PORT_PROPERTY);
        this.keyspace = (String) properties.get(CASSANDRA_CLUSTER_KEYSPACE_PROPERTY);
    }

    public String getCassandraCluster() {
        return cassandraCluster;
    }

    public String getClusterPort() {
        return clusterPort;
    }

    public String getKeySpace() {
        return keyspace;
    }

}
