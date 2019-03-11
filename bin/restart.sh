#!/usr/bin/env bash
# 重启
path=$(cd `dirname $0`;pwd)
sh ${path}/stop.sh

sh ${path}/start-mcp.sh