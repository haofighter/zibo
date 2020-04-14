#!/usr/bin/env sh

export ALIPAY_ROOT=$(cd `dirname $0`; cd ../../; pwd)

# define the global value
pid=

start_service() {
  cd ${ALIPAY_ROOT}/iotsdk/bin
  nohup ./alipay_iotd >/dev/null 2>&1 &
}

PS_LINE=`ps |grep alipay_iotd|grep -v grep`
if [ ! -z "${PS_LINE}" ]; then
  #pids=(${PS_LINE// / })
  pid=`echo ${PS_LINE} | cut -d ' ' -f 1`
  #pid=${pids[0]}
  echo "snapshot pid is ${pid}"
fi

case $1 in
  "keepalive")
    if [ -z "${pid}" ]; then
      start_service
    fi
  ;;
  "startup")
    if [ -z "${pid}" ]; then
      start_service
    else
      echo "Service was already started!"
    fi
  ;;
  "shutdown")
    if [ -n "${pid}" ]; then
      kill -9 "${pid}"
      echo "Service was terminated!"
    fi
  ;;
  "restart")
    if [ -n "${pid}" ]; then
      echo "Stop service..."
      kill -9 "${pid}"
      echo "Service was terminated!"
    fi
    sleep 3
    start_service
  ;;
  "status")
     echo "Service is running on proc: ${pid}"
  ;;
  *)
     echo "Unsupported command!"
  ;;
esac