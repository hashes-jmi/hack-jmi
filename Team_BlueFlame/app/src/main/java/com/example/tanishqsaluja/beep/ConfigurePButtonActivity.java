package com.example.tanishqsaluja.beep;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.tanishqsaluja.beep.BT.BTstateListener;
import com.example.tanishqsaluja.beep.BT.BluetoothManager;
import com.example.tanishqsaluja.beep.BT.ConnectionListener;
import com.example.tanishqsaluja.beep.BT.DiscoveryListener;

import java.util.ArrayList;
import java.util.List;

public class ConfigurePButtonActivity extends AppCompatActivity {
        private static final int SEARCH_DEVICE_ACTIVITY_REQ_CODE = 100;
        private static final String TAG = ">>>M.ACTIVITY>>>";
        TextView textInfo1, textInfo2, textInfo3, textInfo4;
        ImageButton btnRMS, btnEnableBT, btnSyncServer;

        BluetoothManager bbttManager;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_configure_pbutton);



            initialiseViews();


            bbttManager = new BluetoothManager(this);
            bbttManager.setCallbackOnUI(this);
            bbttManager.setBTstateListener(getBtStateListener());
            bbttManager.setConnectionListener(getConnectionListener());//to be used after on activityResult()
            bbttManager.startBtManager();

            toggleInitialStateChanges();//need to call this AFTER bbtmanager is initialised sinc it uses an enable() checker in itself


        }


        public void initialiseViews() {
            textInfo1 = findViewById(R.id.text_info1);
            textInfo2 = findViewById(R.id.text_info2);
           // textInfo3 = findViewById(R.id.text_info3);
            textInfo4 = findViewById(R.id.text_info4);
//.            textInfo4.setVisibility(View.GONE);

            btnEnableBT = findViewById(R.id.btn_enable_bluetooth);
            btnRMS = findViewById(R.id.btn_rms);
          //  btnSyncServer = findViewById(R.id.btn_sync_server);



        }

        private void toggleInitialStateChanges() {
            Boolean isBtOn = bbttManager.isBTEnabled();
            Log.e(TAG, "toggleInitialStateChanges: btstateenabled:"+isBtOn );
            if (isBtOn) {
                textInfo1.setText("Bluetooth Enabled. Please click on RMS icon to start Connection.");
                textInfo1.setTextColor(Color.BLUE);

                btnRMS.setImageDrawable(getResources().getDrawable(R.drawable.rms_active));
                btnRMS.setEnabled(true);

                btnEnableBT.setImageDrawable(getResources().getDrawable(R.drawable.bluetooth_active));

                textInfo2.setTextColor(Color.GRAY);
                textInfo2.setText("Connect To RMS device for more information.");

            } else {
                textInfo1.setText("Bluetooth is off. Please click on Bluetooth icon to enable Bluetooth");
                textInfo1.setTextColor(Color.GRAY);


                btnRMS.setImageDrawable(getResources().getDrawable(R.drawable.rms_normal));
                btnRMS.setEnabled(false);

                btnEnableBT.setImageDrawable(getResources().getDrawable(R.drawable.bluetooth_inactive));

                textInfo2.setText("");


            }
        }

        public BTstateListener getBtStateListener() {
            BTstateListener btStateListener = new BTstateListener() {
                @Override
                public void onBluetoothTurningOn() {

                }

                @Override
                public void onBluetoothOn() {
                    Log.e(TAG, "onBluetoothOn:called ");
                    toggleInitialStateChanges();

                }

                @Override
                public void onBluetoothTurningOff() {


                }

                @Override
                public void onBluetoothOff() {
                    Log.e(TAG, "onBluetoothOff: called");
                    toggleInitialStateChanges();

                }

                @Override
                public void onUserDeniedActivation() {

                }
            };
            return btStateListener;
        }

        public void on_rms_click(View view) {
            //only after cancelling every possible connections

            Intent i = new Intent(this, SearchDevicesActivity.class);
            startActivityForResult(i, SEARCH_DEVICE_ACTIVITY_REQ_CODE);

        }
        public void on_btn_enable_bt(View view) {
            if (bbttManager.isBTEnabled()) {
                bbttManager.disableBT();
            } else {
                bbttManager.enableBT();
            }
            toggleInitialStateChanges();


        }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            if (requestCode == SEARCH_DEVICE_ACTIVITY_REQ_CODE) {
                if (resultCode == RESULT_OK) {
                    //ansh: recieve the device as parcelable here in data.getParcelableExtra()start
                    //ansh: connection here, enable listener,change UIs here.store data in database here
                    BluetoothDevice dev = data.getParcelableExtra("DEVICE");
                    Log.e(TAG, "onActivityResult: recieved data:" + dev);

                    btnRMS.setImageDrawable(getResources().getDrawable(R.drawable.rms_loading));
                    textInfo2.setTextColor(Color.GRAY);
                    textInfo2.setText("Connecting...");
                    connectAndStoreData(dev);


                } else if (resultCode == RESULT_CANCELED) {

                    //ansh: recieve the device as parcelable here in data.getParcelableExtra()start
                    //ansh: connection here, enable listener,change UIs here.store data in database here
                    btnRMS.setImageDrawable(getResources().getDrawable(R.drawable.rms_error));
                    textInfo2.setText("Error Occurred. Please click on RMS icon again!");
                    textInfo2.setTextColor(Color.RED);

                }
            }
            super.onActivityResult(requestCode, resultCode, data);
        }

        private void connectAndStoreData(BluetoothDevice dev) {
            bbttManager.stopScanning();
            bbttManager.connectToDevice(dev, true);

        }

        public ConnectionListener getConnectionListener() {
            final int[] flagFileCounter = {0};
            ConnectionListener connectionListener = new ConnectionListener() {
                @Override
                public void onDeviceConnected(BluetoothDevice device) {
                    btnRMS.setImageDrawable(getResources().getDrawable(R.drawable.rms_connected));
                    textInfo2.setText("Connected!\n Recieving data: " + flagFileCounter[0] + " / " + flagFileCounter[0]);
                    textInfo2.setTextColor(Color.BLUE);

                }

                @Override
                public void onDeviceDisconnected(BluetoothDevice device, String message) {
                    textInfo2.setText("Successfully recieved RMS data. Closing Connection...");
                    bbttManager.disableBT();


                }

                @Override
                public void onMessage(String message) {
                    Log.e(TAG, "onMessage: " + message);
                    flagFileCounter[0]++;
                    textInfo2.setText("Connected!\n Recieving data: " + flagFileCounter[0] + "/" + flagFileCounter[0]);
                    Log.e(TAG, "onMessage: recieved");
                }

                @Override
                public void onError(String message) {
                    Log.e(TAG, "onError: error occurred!:" + message);
                    btnRMS.setImageDrawable(getResources().getDrawable(R.drawable.rms_error));
                    textInfo2.setText("Error Occurred. Please click on RMS icon again!");
                    textInfo2.setTextColor(Color.RED);


                }

                @Override
                public void onConnectError(BluetoothDevice device, String message) {
                    btnRMS.setImageDrawable(getResources().getDrawable(R.drawable.rms_error));
                    textInfo2.setText("Error Occurred. Please click on RMS icon again!");
                    textInfo2.setTextColor(Color.RED);


                }
            };

            return connectionListener;
        }




        @Override
        public void finish() {

            bbttManager.stopScanning();
            bbttManager.stopBtManager();
            super.finish();
        }

        public void on_bt_uploadToDBclick(View view) {

        }
        public boolean isOnline() {
            ConnectivityManager cm =
                    (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            return netInfo != null && netInfo.isConnectedOrConnecting();
        }
    }


