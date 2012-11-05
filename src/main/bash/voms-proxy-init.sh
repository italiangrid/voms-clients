#!/bin/bash
#set -x

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

# get standard environment variables
PRGDIR=`dirname "$PRG"`

# set HOME, if not already set
[ -z "$VOMSCLIENTS_HOME" ] && VOMSCLIENTS_HOME=`cd "$PRGDIR/.." ; pwd`

# location of the dependency jars
VOMSCLIENTS_LIBS=$VOMSCLIENTS_HOME/lib

# ':' separated list of jars, for the classpath
VOMSCLIENTS_DEPS=`ls -x $VOMSCLIENTS_LIBS/*.jar | tr '\n' ':'`

# location of the voms-clients jar file
VOMSCLIENTS_JAR="$VOMSCLIENTS_HOME/lib/voms-clients.jar"

# the classpath
VOMSCLIENTS_CP="$VOMSCLIENTS_DEPS$VOMSCLIENTS_JAR"

# the class implementing voms-proxy-init
VOMSPROXYINIT_CLASS="org.italiangrid.vomsclients.VomsProxyInit"

java -DeffectiveUserId=$EUID -cp $VOMSCLIENTS_CP $VOMSPROXYINIT_CLASS "$@"
