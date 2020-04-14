package com.szxb.zibo.Mqtt;

import org.eclipse.paho.client.mqttv3.MqttException;

public interface InterfaceMBinder {
    void sendMessage(XdRecordUpload xdRecordUpload) throws MqttException;
}
