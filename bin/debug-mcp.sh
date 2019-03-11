#!/usr/bin/env bash
# 打包调试模式

path=$(cd `dirname $0`;pwd)

sh ${path}/stop.sh

branch=$1
if [ $# -eq 0 ]
then
branch='dev';
fi
sh ${path}/git-pull.sh ${branch}

sh ${path}/debug.sh


# 19805