package io.dourl.mqtt;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.LinearLayout;
import android.widget.TextView;

import io.dourl.mqtt.bean.UserModel;
import io.dourl.mqtt.job.MsgJobManager;
import io.dourl.mqtt.job.core.MqttConAndSubJob;
import io.dourl.mqtt.manager.LoginManager;
import io.dourl.mqtt.manager.MqttManager;
import io.dourl.mqtt.model.ClanModel;
import io.dourl.mqtt.ui.ChatActivity;
import io.dourl.mqtt.ui.GroupChatActivity;
import io.dourl.mqtt.utils.TopicUtils;

public class MainActivity extends AppCompatActivity {
    private TextView mBtnSend ,mGroupSend;
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
        mToUser.setUid("boss");
        mToUser.setAge(9);

       // message.setSessionId("u" + message.getTo().getUid());
        mBtnSend = findViewById(R.id.btn_send);
        mBtnSend.setText(LoginManager.getCurrentUserId());
        mBtnSend.setOnClickListener(  v -> {

            ChatActivity.intentTo(this,"u" + mToUser.getUid(), mToUser);
        });

        mGroupSend = findViewById(R.id.btn_send_group);

        ClanModel clanModel = new ClanModel();
        clanModel.id = "project";
        clanModel.leader = 120;

        mGroupSend.setOnClickListener(  v -> {
             MsgJobManager.getInstance().addJob(new MqttConAndSubJob(
                     TopicUtils.getGimTopic(clanModel.id), 1, true));
             GroupChatActivity.intentTo(this,clanModel.id,clanModel);

        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        MqttManager.wakeMqtt();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
       // MqttManager.getInstance().destroy();
    }
}