#!/bin/bash

# SPDX-FileCopyrightText: 2006 Istituto Nazionale di Fisica Nucleare
#
# SPDX-License-Identifier: Apache-2.0

#set -x

VOMSCLIENTS_LIBS=${voms-clients.libs}

if [ "x$VOMSCLIENTS_LIBS" == "x" ]; then

  PRG="$0"
  while [ -h "$PRG" ]; do
    ls=`ls -ld "$PRG"`
    link=`expr "$ls" : '.*-> \(.*\)$'`
    if expr "$link" : '/.*' > /dev/null; then
      PRG="$link"
    else
      PRG=`dirname "$PRG"`/"$link"
    fi
  done

  PRGDIR=`dirname "$PRG"`

  VOMSCLIENTS_HOME=`cd "$PRGDIR/.." ; pwd`

  VOMSCLIENTS_LIBS=$VOMSCLIENTS_HOME/share/java/voms-clients

fi

# ':' separated list of jars, for the classpath
VOMSCLIENTS_CP=`ls -1 $VOMSCLIENTS_LIBS/*.jar | tr '\n' ':'`

# the class implementing voms-proxy-destroy
VOMSPROXYDESTROY_CLASS="org.italiangrid.voms.clients.VomsProxyDestroy"

# JVM options
VOMS_CLIENTS_JAVA_OPTIONS=${VOMS_CLIENTS_JAVA_OPTIONS:-"-XX:+UseSerialGC -Xmx16m"}

java $VOMS_CLIENTS_JAVA_OPTIONS -cp $VOMSCLIENTS_CP $VOMSPROXYDESTROY_CLASS "$@"
