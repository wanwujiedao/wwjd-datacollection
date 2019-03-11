#!/usr/bin/env bash
# 停止
supervisorctl -u root -p 123456 stop data-collection
echo 'try to kill pid'
status=$(ps -ef |grep java  |grep datacollection-web/target/datacollection-web.jar | awk '{print $2}')
if [ ! -n ${status} ]
then
echo 'PID is '${status}
kill -9 ${status}
fi
