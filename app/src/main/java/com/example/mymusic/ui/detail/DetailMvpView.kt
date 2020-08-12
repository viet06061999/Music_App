package com.example.mymusic.ui.detail

import android.net.Uri
import com.example.mymusic.data.Track
import com.example.mymusic.ui.base.MvpView

interface DetailMvpView : MvpView {

    /**
     * set tổng thời gian bài hát
     *
     * @param time tổng thời gian
     */
    fun setTimeTotal(time: String)

    /**
     * update thời gian bài hát
     *
     * @param time thời gian hiện tại
     */
    fun setTimeCurrent(time: String)

    /**
     * khởi tạo seekbar
     *
     * @param time tổng progress
     */
    fun initSeekBar(time: Int)

    /**
     * cập nhật progress của seekbar
     *
     * @param time progress hiện tại
     */

    fun updateSeekBar(time: Int)

    /**
     * set nội dung của bài hát cho trình phát
     *
     * @param track thông tin bài hát
     */
    fun setContentTrack(track: Track)

    /**
     * Tạo notification phát nhạc
     *
     * @param track bài hát cần tạo
     * @param playButton id của button play
     * @param position vị trí của bài hát
     * @param size số lượng bài hát trong list
     */
    fun createNotification(track: Track, playButton: Int, position: Int, size: Int)

    /**
     * Cập nhật button play
     *
     * @param id resource button
     */
    fun updatePlayButton(id: Int)

}
