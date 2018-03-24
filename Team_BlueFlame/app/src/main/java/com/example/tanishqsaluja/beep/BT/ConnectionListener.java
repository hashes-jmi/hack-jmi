package com.example.tanishqsaluja.beep.BT;

import android.bluetooth.BluetoothDevice;

public interface ConnectionListener {
    void onDeviceConnected(BluetoothDevice device);
    void onDeviceDisconnected(BluetoothDevice device, String message);
    void onMessage(String message);
    void onError(String message);
    void onConnectError(BluetoothDevice device, String message);
}