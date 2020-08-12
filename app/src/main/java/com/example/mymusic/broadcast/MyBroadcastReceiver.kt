package com.example.mymusic.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.mymusic.ui.main.MainActivity
import com.example.mymusic.ui.main.MainPresenter
import com.example.mymusic.utils.AppConstraints

/**
 * Xử lý sự kiện click action button trên notification
 */
class MyBroadcastReceiver(var mainPresenter: MainPresenter<MainActivity>) : BroadcastReceiver(){
    override fun onReceive(p0: Context?, p1: Intent?) {
        var action = p1?.extras?.get(AppConstraints.EXTRA_ACTION)
        when(action){
            AppConstraints.ACTION_PREVIOUS -> mainPresenter.onTrackPrevious()
            AppConstraints.ACTION_PLAY -> {
                if (mainPresenter.isPlaying) mainPresenter.onTrackPause()
                else mainPresenter.onTrackPlay()
            }
            AppConstraints.ACTION_NEXT -> mainPresenter.onTrackNext(mainPresenter.position+1)
        }
    }

}
