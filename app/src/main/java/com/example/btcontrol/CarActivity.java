package com.example.btcontrol;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class CarActivity extends AppCompatActivity {

    private Context context;
    private BTChatService myChatService;
    private String btData;
    private TextView textViewBT;
    private BluetoothAdapter btAdapter;
    private ImageButton imagebuttonForward,imagebuttonLeft,imagebuttonRight,imagebuttonBack,imagebuttonStop;
    private String macAddress;
    private Spinner spinnerSong;
    private String songCMD = "0";
    private final String car_foward = "f";
    private final String car_left = "l";
    private final String car_right = "r";
    private final String car_back = "b";
    private final String car_stop = "p";
    private ArrayAdapter<CharSequence> spinnerAdpater;
    private Switch switch_adc;
    private Button buttonLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car);

        context = this;
        setTitle("Car control");
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        btData = intent.getStringExtra("btdata");
        Log.d("car","btData = "+btData);

        textViewBT = (TextView) findViewById(R.id.textView_carBT);
        textViewBT.setText(btData);

        btAdapter = BluetoothAdapter.getDefaultAdapter();
        myChatService = new BTChatService(context,myHandler);

        if (btData != null){
            macAddress = btData.substring(btData.length()-17);
            Log.d("car","macAddress = "+macAddress);
            BluetoothDevice device = btAdapter.getRemoteDevice(macAddress);
            myChatService.connect(device);

        }

        spinnerSong = (Spinner) findViewById(R.id.spinner_carSong);
        spinnerSong.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch (position){
                    case 0:
                        songCMD = "0";
                        break;
                    case 1:
                        songCMD = "1";
                        break;
                    case 2:
                        songCMD = "2";
                        break;
                    case 3:
                        songCMD = "3";
                        break;
                    case 4:
                        songCMD = "4";
                        break;
                    case 5:
                        songCMD = "5";
                        break;
                }
                Log.d("car","songCMD = "+songCMD );
                sendCMD(songCMD);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerAdpater = ArrayAdapter.createFromResource(context,R.array.song_name,R.layout.simple_spinner_item);
        spinnerAdpater.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSong.setAdapter(spinnerAdpater);


        imagebuttonForward = (ImageButton) findViewById(R.id.imageButton_forward);
        imagebuttonLeft = (ImageButton) findViewById(R.id.imageButton_left);
        imagebuttonRight = (ImageButton) findViewById(R.id.imageButton_right);
        imagebuttonBack = (ImageButton) findViewById(R.id.imageButton_back);
        imagebuttonStop = (ImageButton)findViewById(R.id.imageButton_stop);

        imagebuttonForward.setOnClickListener(new MyButton());
        imagebuttonLeft.setOnClickListener(new MyButton());
        imagebuttonRight.setOnClickListener(new MyButton());
        imagebuttonBack.setOnClickListener(new MyButton());
        imagebuttonStop.setOnClickListener(new MyButton());

        switch_adc = (Switch) findViewById(R.id.switch_adc);
        switch_adc.setChecked(false);
        switch_adc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    sendCMD("v");
                }else {
                    sendCMD("z");
                }
            }
        });

        buttonLink = (Button) findViewById(R.id.button_carLink);
        buttonLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btData != null){
                    BluetoothDevice device = btAdapter.getRemoteDevice(macAddress);
                    myChatService.connect(device);
                }else {
                    Toast.makeText(context,"no paired BT device.",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.car_menu,menu);
        return  true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.car_sensor:

                break;
            case R.id.mode_home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);

    }

    private final Handler myHandler = new Handler(Looper.myLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case Constants.MESSAGE_DEVICE_NAME:
                    String data = msg.getData().getString(Constants.DEVICE_NAME);//回傳已連上的訊息
                    Toast.makeText(context,"Connected to "+data,Toast.LENGTH_SHORT).show();
                    break;
                case Constants.MESSAGE_TOAST://連線錯誤的訊息
                    String error = msg.getData().getString(Constants.TOAST);
                    Toast.makeText(context,error,Toast.LENGTH_SHORT).show();
                    break;

            }

        }
    };
    //傳送指令
    private void sendCMD(String message){
        int mState = myChatService.getState();
        if(mState == BTChatService.STATE_CONNECTED){
            if(message.length()>0){
                byte[] sendData = message.getBytes();
                myChatService.BTWrite((sendData));
            }

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(myChatService != null){
            myChatService.stop();
            myChatService=null;
        }
    }

    private class MyButton implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.imageButton_forward:
                    sendCMD(car_foward);
                    break;
                case R.id.imageButton_left:
                    sendCMD(car_left);
                    break;
                case R.id.imageButton_right:
                    sendCMD(car_right);
                    break;
                case R.id.imageButton_back:
                    sendCMD(car_back);
                    break;
                case R.id.imageButton_stop:
                    sendCMD(car_stop);
                    break;
            }


        }
    }
}