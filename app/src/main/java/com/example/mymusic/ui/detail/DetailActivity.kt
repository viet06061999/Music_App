package com.example.mymusic.ui.detail


import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.CountDownTimer
import android.provider.MediaStore
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import com.example.mymusic.R
import com.example.mymusic.data.AppDataManager
import com.example.mymusic.data.Track
import com.example.mymusic.ui.base.BaseActivity
import com.example.mymusic.ui.base.Generatic
import com.example.mymusic.utils.AppConstraints
import com.example.mymusic.utils.NotificationUtils
import kotlinx.android.synthetic.main.activity_detail.*
import java.io.FileNotFoundException

class DetailActivity : BaseActivity(), DetailMvpView {

    lateinit var detailPresenter: DetailPresenter<DetailActivity>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        detailPresenter = DetailPresenter(AppDataManager(this))
        val track = intent.extras?.get(AppConstraints.EXTRA_TRACK) as Track
        setContentTrack(track)
        setUp()
    }

    override fun setUp() {
        if (Generatic.mService?.mediaPlayer?.isPlaying!!) updatePlayButton(R.drawable.ic_pause_circle)
        else updatePlayButton(R.drawable.ic_play_circle)
        detailPresenter.initMedia()
        detailPresenter.onAttach(this)
        img_btn_back.setOnClickListener {
            onBackPressed()
        }
        btn_play_detail.setOnClickListener {
            detailPresenter.onClickPlay()
        }
        btn_next_detail.setOnClickListener {
            detailPresenter.onTrackNext(detailPresenter.position + 1)
        }
        btn_pre_detail.setOnClickListener {
            detailPresenter.onTrackPrevious()
        }
        seek_bar_music_detail.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                val countDownTimer: CountDownTimer = object : CountDownTimer(seekBar.max.toLong(), 1) {
                    override fun onTick(millisUntilFinished: Long) {}
                    override fun onFinish() {}
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {
                Generatic.mService?.mediaPlayer?.seekTo(seekBar.progress)
            }
        })
    }

    fun goDetail(context: Context): Intent {
        return Intent(context, this::class.java)
    }

    override fun setContentTrack(track: Track) {
        text_title_detail.text = track.title
        text_artist_detail.text = track.artist
        var image =
                try {
                    MediaStore.Images.Media.getBitmap(contentResolver, track.image)
                } catch (e: FileNotFoundException) {
                    BitmapFactory.decodeResource(resources, R.drawable.img_no_image)
                }
        img_detail.setImageBitmap(image)
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
        btn_play_detail.setImageResource(id)
    }

    override fun setTimeTotal(time: String) {
        text_time_total.text = time
    }

    override fun setTimeCurrent(time: String) {
        text_time_current.text = time
    }

    override fun initSeekBar(time: Int) {
        seek_bar_music_detail.max = time
    }

    override fun updateSeekBar(time: Int) {
        seek_bar_music_detail.progress = time
    }

}
