package io.dourl.mqtt;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import io.dourl.mqtt.manager.MqttManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MqttManager.connectAndSubinJob();
    }
}