package com.example.tanishqsaluja.beep.BT;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


/**
 * Created by Omar on 14/07/2015.
 */
public class BluetoothManager {
    private static final int REQUEST_ENABLE_BT = 1111;

    private Activity activity;//for running(or not) various connections on main thread
    private Context context;// for registering/unregistering dynamic recievers for your app
    private UUID uuid; // this is a unique key shared between 2 devices to establish a connection
    private BluetoothAdapter bluetoothAdapter; // the main class for handling bluetooth changes,discovery and many more.
    private BluetoothSocket socket;// the main class for handling a bluetooh connection( a connection here means a send-recieve bond between 2 devices)

    private BluetoothDevice device, devicePair;

    private BufferedReader input; // stream of data recieved by the our device

    private OutputStream out; // stream of data send to  the other device

    private ConnectionListener connectionListener;
    private DiscoveryListener discoveryListener;
    private BTstateListener BTstateListener;
    private boolean connected;

    private boolean runOnUi;

    public BluetoothManager(Context context){
        initialize(context, UUID.fromString("00001101-0000-1000-8000-00805f9b34fb"));
    }

    public BluetoothManager(Context context, UUID uuid){
        initialize(context, uuid);
    }

    private void initialize(Context context, UUID uuid){
        this.context = context;
        this.uuid = uuid;
        this.connectionListener = null;
        this.discoveryListener = null;
        this.BTstateListener = null;
        this.connected = false;
        this.runOnUi = false;
        this.bluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
    }

    public void startBtManager(){
        this.bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        context.registerReceiver(bluetoothReceiver, new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));

    }

    public void stopBtManager(){
        context.unregisterReceiver(bluetoothReceiver);
    }

    public void showEnableDialog(Activity activity){
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activity.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
    }

    public void enableBT(){
        if(bluetoothAdapter!=null) {
            if (!bluetoothAdapter.isEnabled()) {
                bluetoothAdapter.enable();
            }
        }
    }
    public void disableBT(){
        if(bluetoothAdapter!=null) {
            if (bluetoothAdapter.isEnabled()) {
                bluetoothAdapter.disable();
            }
        }
    }
    public boolean isBTEnabled(){
        return bluetoothAdapter.getState()==BluetoothAdapter.STATE_ON;

    }


    public BluetoothSocket getSocket(){
        return socket;
    }


    public void setCallbackOnUI(Activity activity){
        this.activity = activity;
        this.runOnUi = true;
    }
    public void onActivityResult(int requestCode, final int resultCode){
        if(BTstateListener !=null){
            if(requestCode==REQUEST_ENABLE_BT){
                ThreadHelper.run(runOnUi, activity, new Runnable() {
                    @Override
                    public void run() {
                        if(resultCode==Activity.RESULT_CANCELED){
                            BTstateListener.onUserDeniedActivation();
                        }
                    }
                });
            }
        }
    }
    
    public void connectToAddress(String address, boolean insecureConnection) {
        BluetoothDevice device = bluetoothAdapter.getRemoteDevice(address);
        connectToDevice(device, insecureConnection);
    }
    public void connectToAddress(String address) {
        connectToAddress(address, false);
    }
    public void connectToName(String name, boolean insecureConnection) {
        for (BluetoothDevice blueDevice : bluetoothAdapter.getBondedDevices()) {
            if (blueDevice.getName().equals(name)) {
                connectToDevice(blueDevice, insecureConnection);
                return;
            }
        }
    }
    public void connectToName(String name) {
        connectToName(name, false);
    }
    public void connectToDevice(BluetoothDevice device, boolean insecureConnection){
        new ConnectThread(device, insecureConnection).start();
    }
    public void connectToDevice(BluetoothDevice device){
        connectToDevice(device, false);
    }

    public void disconnect() {
        try {
            socket.close();
        } catch (final IOException e) {
            if(connectionListener !=null) {
                ThreadHelper.run(runOnUi, activity, new Runnable() {
                    @Override
                    public void run() {
                        connectionListener.onError(e.getMessage());
                    }
                });
            }
        }
    }
    public boolean isConnected(){
        return connected;
    }

    public void send(String msg){
        try {
            out.write(msg.getBytes());
        } catch (final IOException e) {
            connected=false;
            if(connectionListener !=null){
                ThreadHelper.run(runOnUi, activity, new Runnable() {
                    @Override
                    public void run() {
                        connectionListener.onDeviceDisconnected(device, e.getMessage());
                    }
                });
            }
        }
    }

    public List<BluetoothDevice> getPairedDevices(){
        List<BluetoothDevice> devices = new ArrayList<>();
        devices.addAll(bluetoothAdapter.getBondedDevices());
        return devices;
    }

    public void startScanning(){
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);

        context.registerReceiver(scanReceiver, filter);
        bluetoothAdapter.startDiscovery();
    }
    public void stopScanning(){
        if(bluetoothAdapter!=null && bluetoothAdapter.isDiscovering()) {
            bluetoothAdapter.cancelDiscovery();
        }
    }

    public void pair(BluetoothDevice device){
        context.registerReceiver(pairReceiver, new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED));
        devicePair=device;
        try {
            Method method = device.getClass().getMethod("createBond", (Class[]) null);
            method.invoke(device, (Object[]) null);
        } catch (final Exception e) {
            if(discoveryListener !=null) {
                ThreadHelper.run(runOnUi, activity, new Runnable() {
                    @Override
                    public void run() {
                        discoveryListener.onError(e.getMessage());
                    }
                });
            }
        }
    }
    public void unpair(BluetoothDevice device) {
        context.registerReceiver(pairReceiver, new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED));
        devicePair=device;
        try {
            Method method = device.getClass().getMethod("removeBond", (Class[]) null);
            method.invoke(device, (Object[]) null);
        } catch (final Exception e) {
            if(discoveryListener !=null) {
                ThreadHelper.run(runOnUi, activity, new Runnable() {
                    @Override
                    public void run() {
                        discoveryListener.onError(e.getMessage());
                    }
                });
            }
        }
    }

    private class ReceiveThread extends Thread implements Runnable{
        public void run(){
            String msg;
            try {
                while((msg = input.readLine()) != null) {
                    if(connectionListener != null){
                        final String msgCopy = msg;
                        ThreadHelper.run(runOnUi, activity, new Runnable() {
                            @Override
                            public void run() {
                                connectionListener.onMessage(msgCopy);
                            }
                        });
                    }
                }
            } catch (final IOException e) {
                connected=false;
                if(connectionListener != null){
                    ThreadHelper.run(runOnUi, activity, new Runnable() {
                        @Override
                        public void run() {
                            connectionListener.onDeviceDisconnected(device, e.getMessage());
                        }
                    });
                }
            }
        }
    }
    private class ConnectThread extends Thread {    
        ConnectThread(BluetoothDevice device, boolean insecureConnection) {
            BluetoothManager.this.device=device;
            try {
                if(insecureConnection){
                    BluetoothManager.this.socket = device.createInsecureRfcommSocketToServiceRecord(uuid);
                }
                else{
                    BluetoothManager.this.socket = device.createRfcommSocketToServiceRecord(uuid);
                }
            } catch (IOException e) {
                if(connectionListener !=null){
                    connectionListener.onError(e.getMessage());
                }
            }
        }

        public void run() {
            bluetoothAdapter.cancelDiscovery();

            try {
                socket.connect();
                out = socket.getOutputStream();
                input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                connected=true;

                new ReceiveThread().start();

                if(connectionListener !=null) {
                    ThreadHelper.run(runOnUi, activity, new Runnable() {
                        @Override
                        public void run() {
                            connectionListener.onDeviceConnected(device);
                        }
                    });
                }
            } catch (final IOException e) {
                if(connectionListener !=null) {
                    ThreadHelper.run(runOnUi, activity, new Runnable() {
                        @Override
                        public void run() {
                            connectionListener.onConnectError(device, e.getMessage());
                        }
                    });
                }

                try {
                    socket.close();
                } catch (final IOException closeException) {
                    if (connectionListener != null) {
                        ThreadHelper.run(runOnUi, activity, new Runnable() {
                            @Override
                            public void run() {
                                connectionListener.onError(closeException.getMessage());
                            }
                        });
                    }
                }
            }
        }
    }

    private BroadcastReceiver scanReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action!=null) {
                switch (action) {
                    case BluetoothAdapter.ACTION_STATE_CHANGED:
                        final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
                        if (state == BluetoothAdapter.STATE_OFF) {
                            if (discoveryListener != null) {
                                ThreadHelper.run(runOnUi, activity, new Runnable() {
                                    @Override
                                    public void run() {
                                        discoveryListener.onError("BluetoothManager turned off");
                                    }
                                });
                            }
                        }
                        break;
                    case BluetoothAdapter.ACTION_DISCOVERY_STARTED:
                        if (discoveryListener != null){
                            ThreadHelper.run(runOnUi, activity, new Runnable() {
                                @Override
                                public void run() {
                                    discoveryListener.onDiscoveryStarted();
                                }
                            });
                        }
                        break;
                    case BluetoothAdapter.ACTION_DISCOVERY_FINISHED:
                        context.unregisterReceiver(scanReceiver);
                        if (discoveryListener != null){
                            ThreadHelper.run(runOnUi, activity, new Runnable() {
                                @Override
                                public void run() {
                                    discoveryListener.onDiscoveryFinished();
                                }
                            });
                        }
                        break;
                    case BluetoothDevice.ACTION_FOUND:
                        final BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                        if (discoveryListener != null){
                            ThreadHelper.run(runOnUi, activity, new Runnable() {
                                @Override
                                public void run() {
                                    discoveryListener.onDeviceFound(device);
                                }
                            });
                        }
                        break;
                }
            }
        }
    };

    private final BroadcastReceiver pairReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            Log.d("BLE", "PAIR RECEIVER");

            if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
                final int state = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, BluetoothDevice.ERROR);
                final int prevState	= intent.getIntExtra(BluetoothDevice.EXTRA_PREVIOUS_BOND_STATE, BluetoothDevice.ERROR);

                if (state == BluetoothDevice.BOND_BONDED && prevState == BluetoothDevice.BOND_BONDING) {
                    context.unregisterReceiver(pairReceiver);
                    if(discoveryListener !=null){
                        ThreadHelper.run(runOnUi, activity, new Runnable() {
                            @Override
                            public void run() {
                                discoveryListener.onDevicePaired(devicePair);
                            }
                        });
                    }
                } else if (state == BluetoothDevice.BOND_NONE && prevState == BluetoothDevice.BOND_BONDED){
                    context.unregisterReceiver(pairReceiver);
                    if(discoveryListener !=null){
                        ThreadHelper.run(runOnUi, activity, new Runnable() {
                            @Override
                            public void run() {
                                discoveryListener.onDeviceUnpaired(devicePair);
                            }
                        });
                    }
                }
            }
        }
    };

    private final BroadcastReceiver bluetoothReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (action!=null && action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
                if(BTstateListener !=null) {
                    ThreadHelper.run(runOnUi, activity, new Runnable() {
                        @Override
                        public void run() {
                            switch (state) {
                                case BluetoothAdapter.STATE_OFF:
                                    BTstateListener.onBluetoothOff();
                                    break;
                                case BluetoothAdapter.STATE_TURNING_OFF:
                                    BTstateListener.onBluetoothTurningOff();
                                    break;
                                case BluetoothAdapter.STATE_ON:
                                    BTstateListener.onBluetoothOn();
                                    break;
                                case BluetoothAdapter.STATE_TURNING_ON:
                                    BTstateListener.onBluetoothTurningOn();
                                    break;
                            }
                        }
                    });
                }
            }
        }
    };

    public void setConnectionListener(ConnectionListener connectionListener) {
        this.connectionListener = connectionListener;
    }
    public void removeConnectionListener(){
        this.connectionListener = null;
    }

    public void setDiscoveryListener(DiscoveryListener discoveryListener){
        this.discoveryListener = discoveryListener;
    }
    public void removeDiscoveryListener(){
        this.discoveryListener = null;
    }

    public void setBTstateListener(BTstateListener BTstateListener){
        this.BTstateListener = BTstateListener;
    }
    public void removeBTstateListener(){
        this.BTstateListener = null;
    }



}