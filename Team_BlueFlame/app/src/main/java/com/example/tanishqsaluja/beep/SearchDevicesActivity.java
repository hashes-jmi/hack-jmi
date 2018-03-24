package com.example.tanishqsaluja.beep;

import android.support.v7.app.AppCompatActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tanishqsaluja.beep.BT.BTstateListener;
import com.example.tanishqsaluja.beep.BT.BluetoothManager;
import com.example.tanishqsaluja.beep.BT.DiscoveryListener;

import java.text.MessageFormat;
import java.util.ArrayList;

/**
 * Created by Varun on 3/24/2018.
 */

public class SearchDevicesActivity extends AppCompatActivity {
    private static final String TAG ="@@@SearchDEVactivity@@@" ;
    LinearLayout layoutBtOff;FrameLayout layoutBtOn;
    ListView lvPaired,lvVisible;
    FloatingActionButton fabRefresh;

    BluetoothManager bbttManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_devices);

        initializeViews();
        toggleUIchanges();
        setAllViewsClickListeners();
    }

    private void initializeViews() {
        layoutBtOff = findViewById(R.id.layout_bt_off);
        layoutBtOn = findViewById(R.id.layout_bt_on);

        lvPaired=findViewById(R.id.lv_paired);
        lvVisible=findViewById(R.id.lv_visible);
        lvPaired.setAdapter(getAdpListView(new ArrayList<BluetoothDevice>()));//replace test data with empty lists and make the entry of data in it as dynamic
        lvVisible.setAdapter(getAdpListView(new ArrayList<BluetoothDevice>()));//replace test data with empty lists and make the entry of data in it as dynamic

        fabRefresh=findViewById(R.id.bt_refresh);
        //its listener will be set after bluetooth is initialised

        //===========================================
        bbttManager=new BluetoothManager(this);
        bbttManager.setCallbackOnUI(this);
        bbttManager.setBTstateListener(getBtStateListener());//starts recieving triggers on startBtmanager()
        bbttManager.setDiscoveryListener(getDiscoveryListener());//starts recieving triggers on startBtmanager()
        bbttManager.startBtManager();
        //===========================================






    }
    private void listViewsReset(){

        ArrayAdapter<BluetoothDevice> adp = (ArrayAdapter<BluetoothDevice>) lvVisible.getAdapter();
        ArrayAdapter<BluetoothDevice> adp2 = (ArrayAdapter<BluetoothDevice>) lvPaired.getAdapter();

        adp.clear();adp2.clear();
        if(bbttManager!=null) {
            adp2.addAll(bbttManager.getPairedDevices());
        }
        adp.notifyDataSetChanged();adp2.notifyDataSetChanged();
        ListUtils.setDynamicHeight(lvVisible);ListUtils.setDynamicHeight(lvPaired);



    }
    private void setAllViewsClickListeners(){
        setFabOnclickListener();
        setOnPairedDeviceOnclick();
        setOnVisibleDevicesOnclick();
    }

    private void setFabOnclickListener() {
        fabRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bbttManager.startScanning();//since their is no check for isScanning() its problem of enability will be handled in listener

            }
        });
    }
    private void setOnVisibleDevicesOnclick() {

        lvVisible.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(view.getContext(),"Trying to pair...",Toast.LENGTH_SHORT).show();
                BluetoothDevice device=(BluetoothDevice)lvVisible.getAdapter().getItem(position);
                bbttManager.pair(device);

            }
        });
    }
    private void setOnPairedDeviceOnclick() {
        lvPaired.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BluetoothDevice device=(BluetoothDevice)lvPaired.getAdapter().getItem(position);
                Intent success=new Intent();
                success.putExtra("DEVICE",device);
                setResult(RESULT_OK,success);
                finish();



            }
        });


    }


    public void on_settings_icon_click(View view) {
    }


    public static class ListUtils {
        public static void setDynamicHeight(ListView mListView) {
            ListAdapter mListAdapter = mListView.getAdapter();
            if (mListAdapter == null) {
                // when adapter is null
                return;
            }
            int height = 0;
            int desiredWidth = View.MeasureSpec.makeMeasureSpec(mListView.getWidth(), View.MeasureSpec.UNSPECIFIED);
            for (int i = 0; i < mListAdapter.getCount(); i++) {
                View listItem = mListAdapter.getView(i, null, mListView);
                listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
                height += listItem.getMeasuredHeight();
            }
            ViewGroup.LayoutParams params = mListView.getLayoutParams();
            params.height = height + (mListView.getDividerHeight() * (mListAdapter.getCount() - 1));
            mListView.setLayoutParams(params);
            mListView.requestLayout();
        }

    }
    public ArrayAdapter<BluetoothDevice> getAdpListView(final ArrayList<BluetoothDevice> dataArrDevices) {

//        this is only a initializer array, the actual data and its addition to adapter will be
//        handled by broadcast reciever class
        ArrayAdapter<BluetoothDevice> adp=  new ArrayAdapter<BluetoothDevice>(this
                ,android.R.layout.simple_list_item_2,android.R.id.text1,dataArrDevices){

            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View v= super.getView(position, convertView, parent);

                BluetoothDevice dev=dataArrDevices.get(position);

                TextView text1 =  v.findViewById(android.R.id.text1);
                TextView text2 =  v.findViewById(android.R.id.text2);

                text1.setText(""+dev.getName());
                text2.setText(MessageFormat.format("MAC:{0} pair status:{1}", dev.getAddress(), dev.getBondState()));
                return v;
            }
        };

        return adp;

    }
    public ArrayList<BluetoothDevice> getPairedDevicesArrTestData() {
        ArrayList<BluetoothDevice> arr=new ArrayList<>();
        for (int i = 0; i <5 ; i++) {
            // arr.add(new BluetoothDevice("RXCES"+i,"192.16.000."+i,"1"));

        }
        return arr;
    }
    public ArrayList<BluetoothDevice> getVisisbleDevicesArrTestData() {
        ArrayList<BluetoothDevice> arr=new ArrayList<>();
        for (int i = 0; i <8 ; i++) {
            //    arr.add(new BluetoothDevice("device"+i,"000.16.000."+i,""+i%6));

        }
        return arr;
    }











    //-------------------------------------
    private void toggleUIchanges() {
        if(bbttManager.isBTEnabled()){
            layoutBtOn.setVisibility(View.VISIBLE);
            layoutBtOff.setVisibility(View.GONE);

            listViewsReset();
            bbttManager.startScanning();

        }
        else{
            layoutBtOn.setVisibility(View.GONE);
            layoutBtOff.setVisibility(View.VISIBLE);

            bbttManager.stopScanning();



        }
    }

    public BTstateListener getBtStateListener() {
        BTstateListener btStateListener=new BTstateListener() {
            @Override
            public void onBluetoothTurningOn() {

            }

            @Override
            public void onBluetoothOn() {
                Log.e(TAG, "onBluetoothOn:called " );
                toggleUIchanges();

            }

            @Override
            public void onBluetoothTurningOff() {


            }

            @Override
            public void onBluetoothOff() {
                Log.e(TAG, "onBluetoothOff: called" );
                toggleUIchanges();

            }

            @Override
            public void onUserDeniedActivation() {

            }
        };
        return btStateListener;
    }
    public DiscoveryListener getDiscoveryListener() {
        DiscoveryListener discoveryListener=new DiscoveryListener() {
            @Override
            public void onDiscoveryStarted() {
                invalidateOptionsMenu();
                listViewsReset();
                fabRefresh.setEnabled(false);
                fabRefresh.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(8000);
                            BluetoothAdapter btadp=BluetoothAdapter.getDefaultAdapter();
                            btadp.cancelDiscovery();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();


                Log.e(TAG, "onDiscoveryStarted: " );
            }

            @Override
            public void onDiscoveryFinished() {
                fabRefresh.setEnabled(true);
                invalidateOptionsMenu();
                fabRefresh.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                Log.e(TAG, "onDiscoveryFinished: " );

            }

            @Override
            public void onDeviceFound(BluetoothDevice device) {
                if(device.getBondState()==BluetoothDevice.BOND_BONDED){
                    Log.e(TAG, "onDeviceFound: paired device found must be already in lvPaired " );
                }
                else{
                    ArrayAdapter<BluetoothDevice> adp= (ArrayAdapter<BluetoothDevice>) lvVisible.getAdapter();
                    adp.add(device);
                    adp.notifyDataSetChanged();
                    ListUtils.setDynamicHeight(lvVisible);
                    ListUtils.setDynamicHeight(lvPaired);

                    Log.e(TAG, "brSetActions: added to vivisble devices  list" );
                }


            }

            @Override
            public void onDevicePaired(BluetoothDevice device) {

                bbttManager.stopScanning();
                listViewsReset();


            }

            @Override
            public void onDeviceUnpaired(BluetoothDevice device) {

            }

            @Override
            public void onError(String message) {
                Log.e(TAG, "onError: error OCCURRED!" );
                invalidateOptionsMenu();

                setResult(RESULT_CANCELED);
                finish();

            }
        };
        return discoveryListener;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search_activity, menu);
        return true;
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem menuItemRefresh=menu.findItem(R.id.menu_refresh);

        BluetoothAdapter btAdapter=BluetoothAdapter.getDefaultAdapter();
        Animation anim=menuItemRefresh.getActionView().getAnimation();

        if(btAdapter.isDiscovering()){
            if(anim!=null){
                anim.startNow();

            }
            else {
                ImageView v = (ImageView) menuItemRefresh.getActionView();
                v.clearAnimation();
                v.setImageResource(android.R.drawable.stat_notify_sync_noanim);

                Animation animation = AnimationUtils.loadAnimation(this, R.anim.rot_sync);
                animation.setRepeatCount(Animation.INFINITE);
                animation.setFillAfter(true);
                v.startAnimation(animation);
            }
        }
        else{
            if(anim!=null){
                anim.cancel();
            }

        }


        return true;
    }







    @Override
    public void finish() {

        bbttManager.stopScanning();
        bbttManager.stopBtManager();
        super.finish();
    }


}
