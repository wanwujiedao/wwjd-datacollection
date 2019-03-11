#!/usr/bin/env bash
# debug
path=$(cd `dirname $0`;pwd)
sh ${path}/stop.sh
# 19805
echo 'debug model，port is 19805'
java -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=19805 -Xms512m -Xmx1024m -jar ${path}/../datacollection-web/target/datacollection-web.jar --spring.profiles.active=dev > /var/log/qts/pulsar.log 2>&1 &
