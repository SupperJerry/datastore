#!/bin/sh

# Licensed to the Apache Software Foundation (ASF) under one
# or more contributor license agreements. See the NOTICE file
# distributed with this work for additional information
# regarding copyright ownership. The ASF licenses this file
# to you under the Apache License, Version 2.0 (the
# "License"); you may not use this file except in compliance
# with the License. You may obtain a copy of the License at
#
# http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing,
# software distributed under the License is distributed on an
# "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
# KIND, either express or implied. See the License for the
# specific language governing permissions and limitations
# under the License.
# Get standard environment variables

if [ -z "$JAVA_HOME" ]; then
  echo "You must set the JAVA_HOME variable before running DataStore Scripts."
  exit 1
fi

SCRIPT=`readlink -f $0`
# Absolute path this script is in, thus /home/user/bin
SCRIPTPATH=`dirname $SCRIPT`
DATASTORE_HOME=`dirname $SCRIPTPATH`

DATASTORE_CLASSPATH=""

for f in "$DATASTORE_HOME"/lib/*.jar
do
  DATASTORE_CLASSPATH="$DATASTORE_CLASSPATH":$f
done

export DATASTORE_HOME
export DATASTORE_CLASSPATH