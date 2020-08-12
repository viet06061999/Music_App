package com.example.mymusic.ui.main

import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mymusic.ui.detail.DetailActivity
import com.example.mymusic.R
import com.example.mymusic.broadcast.MyBroadcastReceiver
import com.example.mymusic.data.AppDataManager
import com.example.mymusic.data.Track
import com.example.mymusic.service.OnClearFromRecentService
import com.example.mymusic.ui.base.BaseActivity
import com.example.mymusic.utils.AppConstraints
import com.example.mymusic.utils.NotificationUtils
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseActivity(), MainMvpView {

    lateinit var broadcastReceiver: MyBroadcastReceiver
    lateinit var mainPresenter: MainPresenter<MainActivity>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissionsSafely(arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE), 1)
        }
        setUp()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if (grantResults.size > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setUp()
            } else {
                Toast.makeText(this, "Chưa cấp quyền!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun setUp() {
        if (hasPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                && hasPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
            mainPresenter = MainPresenter(AppDataManager(this))
            mainPresenter.onAttach(this)
            broadcastReceiver = MyBroadcastReceiver(mainPresenter)
            mainPresenter.onPrepare(this, broadcastReceiver)
            registerReceiver(broadcastReceiver, IntentFilter(AppConstraints.ACTION_NOTIFICATION))
            startService(Intent(this, OnClearFromRecentService::class.java))
            btn_play.setOnClickListener {
                mainPresenter.onClickPlay()
            }
            btn_next.setOnClickListener {
                mainPresenter.onTrackNext(mainPresenter.position + 1)
            }
            btn_pre.setOnClickListener {
                mainPresenter.onTrackPrevious()
            }
            music_app_common.setOnClickListener {
                goDetailActivity(mainPresenter.position)
            }
        }
    }

    override fun initRecycle(trackList: ArrayList<Track>) {
        var adapter = TrackAdapter(trackList, mainPresenter)
        if (adapter != null) {
            list_track.setAdapter(adapter)
            list_track.setLayoutManager(LinearLayoutManager(baseContext))
            list_track.setItemAnimator(DefaultItemAnimator())
        }
    }

    override fun goDetailActivity(position: Int) {
        val intent: Intent = DetailActivity().goDetail(this)
        intent.putExtra(AppConstraints.EXTRA_TRACK, mainPresenter.tracks.get(position))
        startActivity(intent)
    }

    override fun setContentTrack(title: String, artist: String, uri: Uri) {
        txt_title.text = title
        txt_artist.text = artist
    }

    override fun createNotification(track: Track, playButton: Int, position: Int, size: Int) {
        NotificationUtils.createNotification(
                this,
                track,
                playButton,
                position,
                size)
    }

    override fun updatePlayButton(id: Int) {
        btn_play.setImageResource(id)
    }

    override fun initProgress(total: Int) {
        progress_time_music.max = total
        progress_time_music.progress = 0
    }

    override fun updateProgress(current: Int) {
        progress_time_music.progress = current
    }

    override fun onDestroy() {
        super.onDestroy()
        mainPresenter.onDestroy(this)
        unregisterReceiver(broadcastReceiver)
    }

}
