package com.example.mymusic.ui.detail

import android.content.Context
import com.example.mymusic.broadcast.MyBroadcastReceiver
import com.example.mymusic.ui.base.MvpPresenter

interface DetailMvpPresenter<V: DetailMvpView> : MvpPresenter<V> {

    /**
     * xử lý sự kiện click button previous
     */
    fun onTrackPrevious()

    /**
     * xử lý sự kiện click button play
     */
    fun onTrackPlay()

    /**
     * xử lý sự kiện click button pause
     */
    fun onTrackPause()

    /**
     * xử lý sự kiện click button next
     *
     * @param position vị trí tiếp theo của bài hát
     */
    fun onTrackNext(position: Int)

    /**
     * xử lý sự kiện click button play
     */
    fun onClickPlay()

}
