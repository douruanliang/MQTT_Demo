package io.dourl.mqtt;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import io.dourl.mqtt.bean.UserModel;
import io.dourl.mqtt.manager.LoginManager;
import io.dourl.mqtt.manager.MqttManager;
import io.dourl.mqtt.ui.ChatActivity;

public class MainActivity extends AppCompatActivity {
    private TextView mBtnSend ;
    private UserModel mToUser,mFromUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFromUser = new UserModel();
        mFromUser.setUid(LoginManager.getCurrentUserId());
        mFromUser.setAge(10);
        // to
        mToUser = new UserModel();
        mToUser.setUid("xiaomi");
        mToUser.setAge(9);

       // message.setSessionId("u" + message.getTo().getUid());
        mBtnSend = findViewById(R.id.btn_send);
        mBtnSend.setText(LoginManager.getCurrentUserId());
        mBtnSend.setOnClickListener(  v -> {
           // MessageManager.getInstance().sendTextMessage(mBaseUser,"hello");
            ChatActivity.intentTo(this,"u" + mToUser.getUid(), mToUser);
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        MqttManager.connectAndSubinJob();
    }

}