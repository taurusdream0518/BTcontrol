package com.example.btcontrol;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private Context context;
    private int mode;
    private final int Mode_Home = 1;
    private final int Mode_Car = 2;
    private ListView listViewBT;
    private BluetoothAdapter btAdapter;
    private Intent intent;
    private int RequestBT_code = 100;
    private Set<BluetoothDevice> allBTDevice;
    private List<String> btDeviceList;
    private ArrayAdapter<String> listAdapter;
    private Intent intentBT;
    private ArrayAdapter<CharSequence> spinnerAdpater;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;
        mode = Mode_Home;
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//固定成直立的畫面


        listViewBT = (ListView) findViewById(R.id.listView_id);
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        //判別有沒有抓到藍芽
        if (btAdapter == null) {
            Toast.makeText(context, "There is not BT device", Toast.LENGTH_SHORT).show();
            finish();
        } else if (!btAdapter.isEnabled()) {

            intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, RequestBT_code);
        }

    }//end od onCreate()

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //判別是不是對的 如果是對的就會開啟權限 不對就關閉
        if (requestCode == RequestBT_code) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(context, "BT enable", Toast.LENGTH_SHORT).show();

            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(context, "Users deny BT enable", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mode_home:
                mode = Mode_Home;
                Log.d("main", "mode = " + mode);
                break;
            case R.id.mode_car:
                mode = Mode_Car;
                Log.d("main", "mode = " + mode);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //抓到所有的藍芽
        allBTDevice = btAdapter.getBondedDevices();
        btDeviceList = new ArrayList<String>();

        //檢查是否配對過
        if(allBTDevice.size()>0){
            for(BluetoothDevice device :allBTDevice){
                //取得藍芽的名字和位址
                btDeviceList.add(device.getName()+"\n"+device.getAddress());
            }

            listAdapter = new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1,btDeviceList);
            listViewBT.setAdapter(listAdapter);
        }
        //監聽藍芽 抓到所選擇的值
        listViewBT.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String itemData = parent.getItemAtPosition(position).toString();
                Log.d("main","itemDate = "+itemData);
                switch (mode){
                    case Mode_Home:
                        intentBT = new Intent(context,HomeActivity.class);
                        intentBT.putExtra("btdata",itemData);
                        startActivity(intentBT);
                        break;
                    case Mode_Car:
                        intentBT = new Intent(context,CarActivity.class);
                        intentBT.putExtra("btdata",itemData);
                        startActivity(intentBT);
                        break;
                }
            }
        });


    }

}