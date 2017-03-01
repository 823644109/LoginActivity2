package com.example.administrator.pacaactivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;


public class MainActivity extends AppCompatActivity  implements SensorEventListener {
    private long time = 0;
    private SeekBar seekBar;
    private TextView text2;
    private String data;
    ToggleButton tbtn;
    ToggleStatus status = new ToggleStatus();
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    private boolean first;
    private GoogleApiClient client;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        seekBar = (SeekBar) findViewById(R.id.seekbar1);
        seekBar.setMax(100);
        seekBar.setEnabled(false);
        text2 = (TextView) findViewById(R.id.text2);
        data = text2.getText().toString();
        String zi = data.substring(0, 2);
        int x = Integer.valueOf(zi).intValue();
        seekBar.setProgress(x);
        tbtn = (ToggleButton) findViewById(R.id.tb_1);
        tbtn.setText("");
        tbtn.setOnClickListener(new ToggleButton.OnClickListener()
        {


            public void onClick(View v) {
                ToggleButton tbtn = (ToggleButton) findViewById(R.id.tb_1);
                if(tbtn.isChecked())
                {
                    Toast.makeText(MainActivity.this,"该停车场已成功加入收藏",Toast.LENGTH_SHORT).show();
                    tbtn.setBackgroundResource(R.drawable.xin_1);
                }
                else
                {
                    Toast.makeText(MainActivity.this,"该停车场已取消收藏",Toast.LENGTH_SHORT).show();
                    tbtn.setBackgroundResource(R.drawable.xin_2);
                }
            }
        });

        SensorManager manager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor sensor = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);//得到加速度传感器对象
        manager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI);

        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        init();
        setData();
        setListener();

    }
    private void init(){
        tbtn = (ToggleButton)findViewById(R.id.tb_1);}
    private void setData(){
        preferences = getSharedPreferences("togglebuttonstatus", Context.MODE_PRIVATE);
		/*
		 * 判断是不是第一次运行该程序
		 * （因为第一次运行时，SharedPreferences是没有保存"first"的，
		 * "first"不存在即为null，默认返回自己设置的参数true）
		 *
		 */
        first = preferences.getBoolean("first", true);
        editor = preferences.edit();
        if(first){
            getStatus();

        }else{
            status.one = preferences.getBoolean("s_one", false);
            setToggButonStatus(status);
        }
    }
    private void setToggButonStatus(ToggleStatus data){
        tbtn.setChecked(data.one);
    }
    private void getStatus(){
        status.one = tbtn.isChecked();
    }
    private void setListener(){

        tbtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                status.setOne(arg1);
            }
        });
    }

    @Override


        public void onSensorChanged(SensorEvent event){
            float[] values = event.values;
            values = event.values;
            Intent intent = new Intent(MainActivity.this, yao.class);
            //取x，y，z三个方向晃动的绝对值
            int x = (int) Math.abs(values[0]);
            int y = (int) Math.abs(values[1]);
            int z = (int) Math.abs(values[2]);
            if (x > 5 && y > 5 && z > 5) {//当绝对值>5并且两次摇的时间间隔>3秒的时候，才认为是摇动了一次，可以自定义值大小
                if (System.currentTimeMillis() - time > 2000) {

                    time = System.currentTimeMillis();
                    startActivity(intent);

                }
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor,int accuracy){

        }


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();
        if(first){
            editor.putBoolean("first", false);
        }
        //关闭之前把数据写进去
        editor.putBoolean("s_one", status.one);
        editor.commit();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }}