package com.example.mymusic.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.mymusic.utils.AppConstraints

/**
 * send action when click action button on notification
 */
class NotificationAction : BroadcastReceiver() {
    override fun onReceive(p0: Context?, p1: Intent?) {
        p0?.sendBroadcast(Intent(AppConstraints.ACTION_NOTIFICATION).putExtra(AppConstraints.EXTRA_ACTION,p1?.action))
    }
}
