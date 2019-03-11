#!/usr/bin/env bash
branch=$1
if [ $# -eq 0 ]
then
branch='dev';
fi

echo "get code from "${branch}"..."

path=$(cd `dirname $0`;pwd)

git -C ${path} pull origin ${branch}

sh ${path}/../mvn.sh