package com.example.tanishqsaluja.beep.BT;

import android.bluetooth.BluetoothDevice;

public interface DiscoveryListener {
    void onDiscoveryStarted();
    void onDiscoveryFinished();
    void onDeviceFound(BluetoothDevice device);
    void onDevicePaired(BluetoothDevice device);
    void onDeviceUnpaired(BluetoothDevice device);
    void onError(String message);
}