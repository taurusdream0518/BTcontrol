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
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity {

    private Spinner spinnerSong;
    private ArrayAdapter<CharSequence> spinnerAdpater;
    private Context context;
    private String btData;
    private TextView textViewBT;
    private BluetoothAdapter btAdapter;
    private BTChatService myChatService;
    private String macAddress;
    private final String ALL_ON = "1";
    private final String ALL_OFF = "2";
    private final String Breath = "3";
    private final String RLED = "4";
    private final String G = "5";
    private final String B = "6";
    private final String purple = "7";
    private final String yellow = "8";
    private final String BG = "9";
    private final String rainbow = "a";
    private final String flash = "b";
    private final String chase = "c";
    private Switch switchALL_ON,switchBreath,switchRLED,switchG,switchB,switchpurple,switchyellow,switchrainbow,switchflash,switchchase,switchBG;
    private String songCMD = "0";
    private Button buttonPlay;
    private Button buttonLink;
    private SeekBar seekbarR,seekbarG,seekbarB;
    private TextView textViewR,textViewG,textViewB;
    private int level_R,level_G,level_B;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        context = this;
        setTitle("Home control");
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//固定成直立的畫面
        //返回鍵
        final ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        btData = intent.getStringExtra("btdata");
        Log.d("home","btData = "+btData);

        textViewBT = (TextView) findViewById(R.id.textView_homeBT);
        textViewBT.setText(btData);
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        myChatService = new BTChatService(context,myHandler);//藍芽的物件

        //先確定data是不是空的
        if(btData != null){
            macAddress = btData.substring(btData.length()-17);
            Log.d("home","macAddress = "+macAddress);
            BluetoothDevice device = btAdapter.getRemoteDevice(macAddress);
            myChatService.connect(device);
        }

        switchALL_ON = (Switch) findViewById(R.id.switch_1);
        switchALL_ON.setChecked(false);
        switchALL_ON.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    sendCMD(ALL_ON);
                }else {
                    sendCMD(ALL_OFF);
                }
            }
        });
        switchBreath = (Switch) findViewById(R.id.switch_3);
        switchBreath.setChecked(false);
        switchBreath.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    sendCMD(Breath);
                }else {
                    sendCMD(ALL_OFF);
                }
            }
        });
        switchRLED = (Switch) findViewById(R.id.switch_4);
        switchRLED.setChecked(false);
        switchRLED.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    sendCMD(RLED);
                }else {
                    sendCMD(ALL_OFF);
                }
            }
        });
        switchG = (Switch) findViewById(R.id.switch_5);
        switchG.setChecked(false);
        switchG.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    sendCMD(G);
                }else {
                    sendCMD(ALL_OFF);
                }
            }
        });
        switchB = (Switch) findViewById(R.id.switch_6);
        switchB.setChecked(false);
        switchB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    sendCMD(B);
                }else {
                    sendCMD(ALL_OFF);
                }
            }
        });
        switchpurple = (Switch) findViewById(R.id.switch_7);
        switchpurple.setChecked(false);
        switchpurple.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    sendCMD(purple);
                }else {
                    sendCMD(ALL_OFF);
                }
            }
        });
        switchyellow = (Switch) findViewById(R.id.switch_8);
        switchyellow.setChecked(false);
        switchyellow.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    sendCMD(yellow);
                }else {
                    sendCMD(ALL_OFF);
                }
            }
        });
        switchBG = (Switch) findViewById(R.id.switch_9);
        switchBG.setChecked(false);
        switchBG.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    sendCMD(BG);
                }else {
                    sendCMD(ALL_OFF);
                }
            }
        });
        switchrainbow = (Switch) findViewById(R.id.switch_10);
        switchrainbow.setChecked(false);
        switchrainbow.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    sendCMD(rainbow);
                }else {
                    sendCMD(ALL_OFF);
                }
            }
        });
        switchflash = (Switch) findViewById(R.id.switch_11);
        switchflash.setChecked(false);
        switchflash.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    sendCMD(flash);
                }else {
                    sendCMD(ALL_OFF);
                }
            }
        });
        switchchase = (Switch) findViewById(R.id.switch_12);
        switchchase.setChecked(false);
        switchchase.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    sendCMD(chase);
                }else {
                    sendCMD(ALL_OFF);
                }
            }
        });
        textViewR = (TextView) findViewById(R.id.textView_R);
        textViewR.setText("");
        seekbarR = (SeekBar) findViewById(R.id.seekBar_R);
        seekbarR.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textViewR.setText("當前值:"+ progress +"/255");
                level_R = progress;
                Log.d("home","R = "+level_R);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Toast.makeText(context,"觸碰SeekBar",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Toast.makeText(context,"放開SeekBar",Toast.LENGTH_SHORT).show();
            }
        });
        textViewG = (TextView) findViewById(R.id.textView_G);
        textViewG.setText("");
        seekbarG = (SeekBar) findViewById(R.id.seekBar_G);
        seekbarG.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textViewG.setText("當前值:"+ progress +"/255");
                level_G = progress;
                Log.d("home","G = "+level_G);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Toast.makeText(context,"觸碰SeekBar",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Toast.makeText(context,"放開SeekBar",Toast.LENGTH_SHORT).show();
            }
        });
        textViewB = (TextView) findViewById(R.id.textView_B);
        textViewB.setText("");
        seekbarB = (SeekBar) findViewById(R.id.seekBar_B);
        seekbarB.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textViewB.setText("當前值:"+ progress +"/255");
                level_B = progress;
                Log.d("home","G = "+level_B);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Toast.makeText(context,"觸碰SeekBar",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Toast.makeText(context,"放開SeekBar",Toast.LENGTH_SHORT).show();
            }
        });


//        spinnerSong = (Spinner) findViewById(R.id.spinner_carSong);
//        spinnerSong.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                switch (position){
//                    case 0:
//                        songCMD = "0";
//                        break;
//                    case 1:
//                        songCMD = "1";
//                        break;
//                    case 2:
//                        songCMD = "2";
//                        break;
//                    case 3:
//                        songCMD = "3";
//                        break;
//                    case 4:
//                        songCMD = "4";
//                        break;
//                    case 5:
//                        songCMD = "5";
//                        break;
//                }
//                Log.d("home","songCMD = "+songCMD);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });

//        spinnerAdpater = ArrayAdapter.createFromResource(context,R.array.song_name,R.layout.simple_spinner_item);
//        spinnerAdpater.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinnerSong.setAdapter(spinnerAdpater);
//
//        buttonLink = (Button) findViewById(R.id.button_homeLink);
//        buttonLink.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(btData != null){
//                    BluetoothDevice device = btAdapter.getRemoteDevice(macAddress);
//                    myChatService.connect(device);
//                }else {
//                    Toast.makeText(context,"no paired BT device.",Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//
//
//        buttonPlay = (Button) findViewById(R.id.button_homePlay);
//        buttonPlay.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d("home","send songCMD = "+songCMD);
//                sendCMD(songCMD);
//            }
//        });
    }
    //返回鍵的設定
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
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
}