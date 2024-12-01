#!/usr/bin/env bash
#set -x
set -eu -o pipefail

VOMSCLIENTS_LIBS="${voms-clients.libs-}"

if [ -z "$VOMSCLIENTS_LIBS" ]; then

  PRG="$(realpath "$0")"

  PRGDIR="$(dirname "$PRG")"

  VOMSCLIENTS_HOME="$(cd "$PRGDIR/.." ; pwd)"

  VOMSCLIENTS_LIBS="$VOMSCLIENTS_HOME/share/java/voms-clients"

fi

# ':' separated list of jars, for the classpath
VOMSCLIENTS_CP="$(find "$VOMSCLIENTS_LIBS" -name "*.jar" -printf '%p:')"

# the class implementing voms-proxy-destroy
VOMSPROXYDESTROY_CLASS="org.italiangrid.voms.clients.VomsProxyDestroy"

# JVM options
VOMS_CLIENTS_JAVA_OPTIONS="${VOMS_CLIENTS_JAVA_OPTIONS:-"-XX:+UseSerialGC -Xmx16m -Djava.net.preferIPv6Addresses=true"}"

# shellcheck disable=SC2086 # VOMS_CLIENTS_JAVA_OPTIONS is meant to be expanded space-delimited
java $VOMS_CLIENTS_JAVA_OPTIONS -cp "$VOMSCLIENTS_CP" "$VOMSPROXYDESTROY_CLASS" "$@"
