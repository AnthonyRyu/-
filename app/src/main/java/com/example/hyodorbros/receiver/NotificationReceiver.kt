package com.example.hyodorbros.receiver

import android.content.BroadcastReceiver
import android.content.Context
import com.example.hyodorbros.receiver.NotificationReceiver.ReceiveListener
import android.content.Intent

class NotificationReceiver : BroadcastReceiver() {
    interface ReceiveListener {
        fun onReceive(action: String?)
    }
    private var listener: ReceiveListener? = null

    fun callback(listener: ReceiveListener?) {
        this.listener = listener
    }

    override fun onReceive(context: Context, intent: Intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        if (Intent.ACTION_TIME_TICK == intent.action) {
            // 시간이 변경된 경우 해야 할 작업
            listener!!.onReceive(Intent.ACTION_TIME_TICK)
        }

        // ACTION_DATE_CHANGED: 날짜가 변경된 경우 해야 할 작업
        if (Intent.ACTION_DATE_CHANGED == intent.action) {
            // 날짜가 변경된 경우 해야 할 작업
            listener!!.onReceive(Intent.ACTION_DATE_CHANGED)
        }

    }
}