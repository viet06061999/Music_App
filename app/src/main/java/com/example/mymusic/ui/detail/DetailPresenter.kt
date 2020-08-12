package com.example.mymusic.ui.detail

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.Handler
import android.os.IBinder
import androidx.core.content.ContextCompat
import com.example.mymusic.R
import com.example.mymusic.broadcast.MyBroadcastReceiver
import com.example.mymusic.data.DataManager
import com.example.mymusic.data.Track
import com.example.mymusic.service.PlaySong
import com.example.mymusic.ui.base.BasePresenter
import com.example.mymusic.ui.base.Generatic
import com.example.mymusic.utils.AppConstraints
import java.text.SimpleDateFormat

class DetailPresenter<V : DetailMvpView> :BasePresenter<V>, DetailMvpPresenter<V> {
    var position: Int = 0
    var tracks: ArrayList<Track>
    lateinit var runnable: Runnable

    constructor(dataManager: DataManager) : super(dataManager) {
        tracks = Generatic.mService?.tracks!!
        position = Generatic.mService?.position!!
    }

    fun initMedia() {
        var formatTime = SimpleDateFormat(AppConstraints.TIME_PLAY)
        var handler: Handler = Handler()
        runnable = Runnable() {
            if (Generatic.mService?.mediaPlayer != null) {
                var timeTotal= Generatic.mService?.mediaPlayer?.duration!!
                getMvpView()?.setTimeTotal(formatTime.format(timeTotal))
                getMvpView()?.initSeekBar(timeTotal)
                var timeCurent= Generatic.mService?.mediaPlayer?.currentPosition!!
                getMvpView()?.updateSeekBar(timeCurent)
                getMvpView()?.setTimeCurrent(formatTime.format(timeCurent))
                Generatic.mService?.mediaPlayer?.setOnCompletionListener {
                    onTrackNext(position + 1)
                }
                handler.postDelayed(runnable, 100)
            }
        }
        handler.postDelayed(runnable, 100)
    }

    override fun onTrackPrevious() {
        position--;
        if (position < 0) position = tracks.size - 1
        getMvpView()?.createNotification(tracks.get(position),
                R.drawable.ic_pause,
                position,
                tracks.size - 1)
        initMedia()
        getMvpView()?.updatePlayButton(R.drawable.ic_pause_circle)
        getMvpView()?.setContentTrack(tracks.get(position))
        Generatic.mService?.onTrackPrevious(position)
    }

    override fun onTrackPlay() {
        getMvpView()?.createNotification(tracks.get(position),
                R.drawable.ic_pause,
                position,
                tracks.size - 1)
        getMvpView()?.setContentTrack(tracks.get(position))
        getMvpView()?.updatePlayButton(R.drawable.ic_pause_circle)
        Generatic.mService?.onTrackPlay(position)
        initMedia()

    }

    override fun onTrackPause() {
        getMvpView()?.createNotification(tracks.get(position),
                R.drawable.ic_play,
                position,
                tracks.size - 1)
        getMvpView()?.setContentTrack(tracks.get(position))
        getMvpView()?.updatePlayButton(R.drawable.ic_play_circle)
        Generatic.mService?.onTrackPause(position)
    }

    override fun onTrackNext(pos: Int) {
        position++;
        if (position != pos) position = pos
        if (position > tracks.size - 1) position = 0
        getMvpView()?.createNotification(tracks.get(position),
                R.drawable.ic_pause,
                position,
                tracks.size - 1)
        initMedia()
        getMvpView()?.updatePlayButton(R.drawable.ic_pause_circle)
        getMvpView()?.setContentTrack(tracks.get(position))
        Generatic.mService?.onTrackNext(position)
    }

    override fun onClickPlay() {
        if (Generatic.mService?.mediaPlayer?.isPlaying!!) onTrackPause()
        else onTrackPlay()
    }

}
