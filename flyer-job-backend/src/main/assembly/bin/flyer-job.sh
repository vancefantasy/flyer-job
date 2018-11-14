#!/bin/bash

#APP_LOCATION=/path/to/flyer-job.jar
#APP_NAME=flyer-job
#JAVA_HOME=/path/to/java
#LOG_HOME=/tmp/logs
#ACTIVE_PROFILE=ext

APP_LOCATION=/Users/user/IdeaProjects/flyer-job/flyer-job-backend/target/flyer-job/flyer-job-exec.jar
APP_NAME=flyer-job
JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk1.8.0_181.jdk/Contents/Home
LOG_HOME=/Users/user/IdeaProjects/flyer-job/flyer-job-backend/target/flyer-job/logs/
ACTIVE_PROFILE=ext

start(){
    ## source ./jvm-options.sh

    pid=`ps -ef|grep $APP_NAME| grep java | grep -v grep|grep -v kill|awk '{print $2}'`

	if [ -n "${pid}" ]; then
        echo "$APP_NAME is already running"
    else
        echo "Starting $APP_NAME ..."
        nohup java -Dlogs.home=$LOG_HOME -Dspring.profiles.active=$ACTIVE_PROFILE $JAVA_OPT -jar $APP_LOCATION  > /dev/null 2>&1 &
    fi
}

stop(){
    pid=`ps -ef|grep $APP_NAME| grep java | grep -v grep|grep -v kill|awk '{print $2}'`

    if [ ! -n "${pid}" ]
    then
        echo "$APP_NAME is not running"
    else
        echo "Stoping $APP_NAME($PID) ..."
        kill -15 $pid
    fi

    sleep 2

    pid=`ps -ef|grep $APP_NAME| grep java | grep -v grep|grep -v kill|awk '{print $2}'`

    if [ -n "${pid}" ]; then
		echo "Kill $APP_NAME($PID) !";
		kill -9 $pid
	else
		echo "Stop $APP_NAME($PID) Success!"
	fi

    pid=`ps -ef|grep $APP_NAME| grep java | grep -v grep|grep -v kill|awk '{print $2}'`

    if [ -n "${pid}" ]; then
        echo "Stop $APP_NAME($PID) failed!"
        exit 1
    else
        exit 0
    fi
}

status(){
    pid=`ps -ef|grep $APP_NAME| grep java | grep -v grep|grep -v kill|awk '{print $2}'`
    if [ -n "${pid}" ]; then
    	echo "$APP_NAME($PID) is running"
    else
    	echo "$APP_NAME is not running"
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
