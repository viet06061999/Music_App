package com.example.mymusic.service

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import android.util.Log
import com.example.mymusic.data.AppDataManager
import com.example.mymusic.data.Track

/**
 * service hỗ trợ play nhạc
 */
class PlaySong : Service() {

    // Binder given to clients
    private val mBinder: IBinder = LocalBinder()
    lateinit var tracks: ArrayList<Track>
    var mediaPlayer: MediaPlayer? = null
    var position = 0

    val instance: PlaySong = this

    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    inner class LocalBinder : Binder() {
        fun getService(): PlaySong {
            return instance
        }
    }

    override fun onCreate() {
        tracks = AppDataManager(baseContext).getListSong()
        super.onCreate()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return mBinder
    }

    fun onTrackPrevious(pos: Int) {
        mediaPlayer?.reset()
        position = pos
        initMedia()
        mediaPlayer?.start()
    }

    fun onTrackPlay(pos: Int) {
        position = pos
        if (mediaPlayer == null) {
            initMedia()
        }
        mediaPlayer?.start()
    }

    fun onTrackPause(pos: Int) {
        mediaPlayer?.pause()
    }

    fun onTrackNext(pos: Int) {
        mediaPlayer?.reset()
        position = pos
        initMedia()
        println("${mediaPlayer?.isPlaying} $position $mediaPlayer")
        mediaPlayer?.start()
    }

    fun initMedia() {
        mediaPlayer = MediaPlayer.create(baseContext, tracks.get(position).resource)
    }

}
