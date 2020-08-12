package com.example.mymusic.ui.main

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.*
import android.os.Build
import android.os.Handler
import android.os.IBinder
import androidx.core.content.ContextCompat.getSystemService
import com.example.mymusic.R
import com.example.mymusic.broadcast.MyBroadcastReceiver
import com.example.mymusic.data.DataManager
import com.example.mymusic.data.Track
import com.example.mymusic.service.PlaySong
import com.example.mymusic.ui.base.BasePresenter
import com.example.mymusic.ui.base.Generatic
import com.example.mymusic.utils.AppConstraints


class MainPresenter<V : MainMvpView>
    : BasePresenter<V>, MainMvpPresenter<V> {

    var notificationManager: NotificationManager? = null
    var position: Int = 0
    var isPlaying: Boolean = false
    var bound = false
    var tracks: ArrayList<Track>
    lateinit var runnable: Runnable
    private val mConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(
                className: ComponentName,
                service: IBinder) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            val binder = service as PlaySong.LocalBinder
            Generatic.mService = binder.getService()
            bound = true
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            bound = false
        }
    }


    constructor(dataManager: DataManager) : super(dataManager) {
        tracks = dataManager.getListSong()
    }

    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            var notificationChannel: NotificationChannel = NotificationChannel(
                    AppConstraints.CHANNEL_ID,
                    AppConstraints.NAME_CHANNEL_NOTIFICATION,
                    NotificationManager.IMPORTANCE_LOW
            )
            notificationManager = getSystemService(getMvpView() as Context, NotificationManager::class.java)
            if (notificationManager != null) {
                notificationManager?.createNotificationChannel(notificationChannel)
            }
        }
    }

    fun initMedia() {
        var handler: Handler = Handler()
        runnable = Runnable() {
            if (Generatic.mService?.mediaPlayer != null) {
                getMvpView()?.initProgress(Generatic.mService?.mediaPlayer?.duration!!)
                getMvpView()?.updateProgress(Generatic.mService?.mediaPlayer?.currentPosition!!)
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
        getMvpView()?.setContentTrack(tracks.get(position).title, tracks.get(position).artist, tracks.get(position).image)
        Generatic.mService?.onTrackPrevious(position)
    }

    override fun onTrackPlay() {
        getMvpView()?.createNotification(tracks.get(position),
                R.drawable.ic_pause,
                position,
                tracks.size - 1)
        getMvpView()?.setContentTrack(tracks.get(position).title, tracks.get(position).artist, tracks.get(position).image)
        getMvpView()?.updatePlayButton(R.drawable.ic_pause)
        isPlaying = true
        if (bound) {
            Generatic.mService?.onTrackPlay(position)
            initMedia()
        }
    }

    override fun onTrackPause() {
        getMvpView()?.createNotification(tracks.get(position),
                R.drawable.ic_play,
                position,
                tracks.size - 1)
        getMvpView()?.setContentTrack(tracks.get(position).title, tracks.get(position).artist, tracks.get(position).image)
        getMvpView()?.updatePlayButton(R.drawable.ic_play)
        Generatic.mService?.onTrackPause(position)
        isPlaying = false
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
        getMvpView()?.updatePlayButton(R.drawable.ic_pause)
        getMvpView()?.setContentTrack(tracks.get(position).title, tracks.get(position).artist, tracks.get(position).image)
        Generatic.mService?.onTrackNext(position)
    }

    override fun onClickPlay() {
        if (isPlaying) onTrackPause()
        else onTrackPlay()
    }

    override fun onPrepare(context: Context, broadcastReceiver: MyBroadcastReceiver) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel()
        }
        position = 0
        getMvpView()?.initRecycle(tracks)
        onTrackPause()
        val intent = Intent(context, PlaySong::class.java)
        context.startService(intent)
        var mServiceConnected = context.bindService(intent, mConnection, 0)
    }


    override fun goDetail(pos: Int) {
        onTrackNext(pos)
        getMvpView()?.goDetailActivity(position)
    }

    override fun onDestroy(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager?.cancelAll()
        }
        if (bound) {
            context.unbindService(mConnection)
            bound = false
        }
    }

}
