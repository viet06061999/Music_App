package com.example.mymusic.ui.main

import android.content.Context
import com.example.mymusic.broadcast.MyBroadcastReceiver
import com.example.mymusic.data.Track
import com.example.mymusic.ui.base.MvpPresenter
import java.text.FieldPosition


interface MainMvpPresenter<V : MainMvpView> : MvpPresenter<V> {

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

    /**
     * Khởi tạo các thành phần cho view
     */
    fun onPrepare(context: Context, broadcastReceiver: MyBroadcastReceiver)

    /**
     * chuyển đến activity detail
     *
     * @param position vị trí hiện tại của bài hát
     */
    fun goDetail(position: Int)
    fun onDestroy(context: Context)
}