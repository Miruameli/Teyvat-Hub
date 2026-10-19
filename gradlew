#!/bin/sh
APP_HOME=$(cd "$(dirname "$0")" && pwd)
CLASSPATH=$APP_HOME/gradle/wrapper/gradle-wrapper.jar
exec java -cp "$CLASSPATH" org.gradle.wrapper.GradleWrapperMain "$@"