package io.dourl.mqtt;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import io.dourl.mqtt.base.log.LoggerUtil;
import io.dourl.mqtt.bean.UserModel;
import io.dourl.mqtt.manager.LoginManager;
import io.dourl.mqtt.manager.MessageManager;
import io.dourl.mqtt.manager.MqttManager;

public class MainActivity extends AppCompatActivity {
    private ImageView mBtnSend ;
    private UserModel mBaseUser,mFromUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mFromUser = new UserModel();
        mFromUser.setUid(LoginManager.getCurrentUserId());
        mFromUser.setAge(10);
        // to
        mBaseUser = new UserModel();
        mBaseUser.setUid("admin");
        mBaseUser.setAge(9);


        mBtnSend = findViewById(R.id.btn_send);
        mBtnSend.setOnClickListener(  v -> {
            MessageManager.getInstance().sendTextMessage(mBaseUser,"hello");
        });
        LoggerUtil.d("dou","-------");
        LoggerUtil.d("dou","-------"+getUseTimeString(20000));
    }

    @Override
    protected void onResume() {
        super.onResume();
        MqttManager.connectAndSubinJob();
    }

    public String getUseTimeString(double time) {
        time = time / 1000f;
        return String.format("%.1fs", time);

    }
}