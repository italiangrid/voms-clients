#!/bin/bash
#set -x

VOMSCLIENTS_LIBS="${voms-clients.libs-}"

if [ -z "$VOMSCLIENTS_LIBS" ]; then

  PRG="$0"
  while [ -h "$PRG" ]; do
    ls="$(ls -ld "$PRG")"
    link="$(expr "$ls" : '.*-> \(.*\)$')"
    if expr "$link" : '/.*' > /dev/null; then
      PRG="$link"
    else
      PRG="$(dirname "$PRG")/$link"
    fi
  done

  PRGDIR="$(dirname "$PRG")"

  VOMSCLIENTS_HOME="$(cd "$PRGDIR/.." ; pwd)"

  VOMSCLIENTS_LIBS="$VOMSCLIENTS_HOME/share/java/voms-clients"
fi

# ':' separated list of jars, for the classpath
VOMSCLIENTS_CP="$(ls -1 $VOMSCLIENTS_LIBS/*.jar | tr '\n' ':')"

# the class implementing voms-proxy-init
VOMSPROXYINIT_CLASS="org.italiangrid.voms.clients.VomsProxyInit"

# JVM options
VOMS_CLIENTS_JAVA_OPTIONS="${VOMS_CLIENTS_JAVA_OPTIONS:-"-XX:+UseSerialGC -Xmx16m -Djava.net.preferIPv6Addresses=true"}"

# shellcheck disable=SC2086 # VOMS_CLIENTS_JAVA_OPTIONS is meant to be expanded space-delimited
java $VOMS_CLIENTS_JAVA_OPTIONS -cp "$VOMSCLIENTS_CP" "$VOMSPROXYINIT_CLASS" "$@"
