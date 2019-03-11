#!/usr/bin/env bash
# 拉取代码，打包，然后启动
branch=$1
if [ $# -eq 0 ]
then
branch='dev';
fi

path=$(cd `dirname $0`;pwd)

sh ${path}/git-pull.sh ${branch}

sh ${path}/start.sh