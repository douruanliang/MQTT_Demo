package io.dourl.mqtt.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import io.dourl.mqtt.constants.IntentAction
import io.dourl.mqtt.manager.MqttManager
import io.dourl.mqtt.utils.log.LoggerUtil


class MqttReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        when (action) {
            IntentAction.ACTION_WAKE_MQTT -> {
                MqttManager.wakeMqtt()
            }
            else -> {
                LoggerUtil.d("no action: $action to do")
            }
        }
    }
}
