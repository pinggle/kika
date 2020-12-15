#!/bin/bash
set -x
# 启用宿主机环境变量
source /etc/profile

# kill上一个进程pid
programpid=$(ps aux | grep java | grep $(pwd) | cut -c 9-15)
while [ "${programpid}" != "" ]; do
  kill ${programpid}
  echo "kill ${programpid}"
  sleep 1
  programpid=$(ps aux | grep java | grep $(pwd) | cut -c 9-15)
done
# 启动kika
nohup sh bin/kika >/dev/null 2>&1 &
