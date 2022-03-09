package io.dourl.mqtt;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import io.dourl.mqtt.bean.UserModel;
import io.dourl.mqtt.manager.MessageManager;
import io.dourl.mqtt.manager.MqttManager;

public class MainActivity extends AppCompatActivity {
    private ImageView mBtnSend ;
    private UserModel mBaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        mBaseUser = new UserModel();
        mBaseUser.setAge(9);
        mBaseUser.setEmail("1120");
        mBaseUser.setMobile("120");
        mBtnSend = findViewById(R.id.btn_send);
        mBtnSend.setOnClickListener(  v -> {
            MessageManager.getInstance().sendTextMessage(mBaseUser,"hello");
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        MqttManager.connectAndSubinJob();
    }
}