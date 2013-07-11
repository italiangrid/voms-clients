#!/bin/bash
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

  VOMSCLIENTS_LIBS=$VOMSCLIENTS_HOME/share/java

fi

# ':' separated list of jars, for the classpath
VOMSCLIENTS_CP=`ls -x $VOMSCLIENTS_LIBS/*.jar | tr '\n' ':'`

# the class implementing voms-proxy-info
VOMSPROXYINFO_CLASS="org.italiangrid.voms.clients.VomsProxyInfo"

# java options
VOMSCLIENTS_JAVA_OPTIONS=$VOMSCLIENTS_JAVA_OPTIONS
if [ "x$VOMSCLIENTS_JAVA_OPTIONS" == "x" ]; then
  VOMSCLIENTS_JAVA_OPTIONS=-Xmx512m
fi

java $VOMSCLIENTS_JAVA_OPTIONS -cp $VOMSCLIENTS_CP $VOMSPROXYINFO_CLASS "$@"
