#!/bin/bash

APP_NAME=flyer-job
#JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk1.8.0_181.jdk/Contents/Home
LOG_HOME=/Users/user/IdeaProjects/flyer-job/flyer-job-backend/target/flyer-job/logs/
ACTIVE_PROFILE=ext

DIRNAME=$0
if [ "${DIRNAME:0:1}" = "/" ];then
    CURDIR=`dirname $DIRNAME`
else
    CURDIR="`pwd`"/"`dirname $DIRNAME`"
fi

source ${CURDIR}/jvm-options.sh

start(){

    pid=$(ps -ef|grep ${APP_NAME}| grep java | grep -v grep|grep -v kill|awk '{print $2}')

	if [ -n "${pid}" ]; then
        echo "${APP_NAME} is already running"
    else
        echo "Starting ${APP_NAME} ..."
        nohup java -Dlogs.home=${LOG_HOME} -Dspring.profiles.active=${ACTIVE_PROFILE} -DCURDIR=${CURDIR} JAVA_OPTS -jar ${CURDIR}/../flyer-job-exec.jar --spring.config.location=file:${CURDIR}/../config/ > /dev/null 2>&1 &
    fi
}

stop(){
    pid=$(ps -ef|grep ${APP_NAME}| grep java | grep -v grep|grep -v kill|awk '{print $2}')

    if [ -z "${pid}" ]; then
        echo "${APP_NAME} is not running"
        return
    else
        echo "Stoping ${APP_NAME}($pid) ..."
        kill -15 "${pid}"
    fi

    sleep 2

    pid=$(ps -ef|grep ${APP_NAME}| grep java | grep -v grep|grep -v kill|awk '{print $2}')

    if [ -n "${pid}" ]; then
		echo "Kill ${APP_NAME}(${pid}) !";
		kill -9 "${pid}"
	else
		echo "Stop ${APP_NAME} Success!"
	fi

    pid=$(ps -ef|grep ${APP_NAME}| grep java | grep -v grep|grep -v kill|awk '{print $2}')

    if [ -n "${pid}" ]; then
        echo "Stop ${APP_NAME} failed!"
    fi
}

status(){
    pid=$(ps -ef|grep ${APP_NAME}| grep java | grep -v grep|grep -v kill|awk '{print $2}')
    if [ -n "${pid}" ]; then
    	echo "${APP_NAME}(${pid}) is running"
    else
    	echo "${APP_NAME} is not running"
    fi
}

case "$1" in
start)
    start
    ;;
stop)
    stop
    ;;
status)
    status
    ;;
restart)
    stop
    sleep 1
    start
    ;;
*)
    printf 'Usage: %s {start|stop|status|restart}\n' flyer-job.sh
    exit 1
    ;;
esac
exit 0
