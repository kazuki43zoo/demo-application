#!/usr/bin/env bash

export CARGO_DAEMON_WEBAPP_VERSION=1.6.2
if test ! -f $HOME/cargo-daemon/cargo-daemon-webapp-${CARGO_DAEMON_WEBAPP_VERSION}.war ; then
    mvn dependency:copy -Dartifact=org.codehaus.cargo:cargo-daemon-webapp:${CARGO_DAEMON_WEBAPP_VERSION}:war -DoutputDirectory=$HOME/cargo-daemon/.
fi
java -jar $HOME/cargo-daemon/cargo-daemon-webapp-${CARGO_DAEMON_WEBAPP_VERSION}.war &
