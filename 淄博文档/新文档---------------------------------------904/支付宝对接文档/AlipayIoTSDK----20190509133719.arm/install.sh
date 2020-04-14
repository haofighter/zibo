#!/usr/bin/env sh

SOURCE_DIR=`pwd`
TARGET_DIR=/opt/kira/alipay

if [ ! -d $TARGET_DIR ]; then
    mkdir $TARGET_DIR
fi

if [ ! -d $TARGET_DIR/iotsdk ]; then
    mkdir $TARGET_DIR/iotsdk
fi

if [ ! -d $TARGET_DIR/iotsdk/bin ]; then
    mkdir $TARGET_DIR/iotsdk/bin
fi

if [ ! -d $TARGET_DIR/iotsdk/conf ]; then
    mkdir $TARGET_DIR/iotsdk/conf
fi

if [ ! -d $TARGET_DIR/runtime ]; then
    mkdir $TARGET_DIR/runtime
fi

if [ -e $SOURCE_DIR/iotsdk/bin/alipay_iotd ]; then
    cp $SOURCE_DIR/iotsdk/bin/alipay_iotd $TARGET_DIR/iotsdk/bin
fi

if [ -e $SOURCE_DIR/iotsdk/bin/alipay_iotmd ]; then
    cp $SOURCE_DIR/iotsdk/bin/alipay_iotmd $TARGET_DIR/iotsdk/bin
fi

if [ -e $SOURCE_DIR/iotsdk/bin/monitor.sh ]; then
    cp $SOURCE_DIR/iotsdk/bin/monitor.sh $TARGET_DIR/iotsdk/bin
fi

if [ -e $SOURCE_DIR/iotsdk/bin/service.sh ]; then
    cp $SOURCE_DIR/iotsdk/bin/service.sh $TARGET_DIR/iotsdk/bin
fi

