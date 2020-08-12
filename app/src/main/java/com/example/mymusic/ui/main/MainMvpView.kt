package com.example.mymusic.ui.main

import android.content.ServiceConnection
import android.graphics.Bitmap
import android.net.Uri
import com.example.mymusic.data.Track
import com.example.mymusic.ui.base.MvpView

interface MainMvpView : MvpView {

    /**
     * set nội dung của bài hát cho trình phát
     *
     * @param title tên bài hát
     * @param artist tên ca sĩ
     * @param uri uri ảnh của bài hát
     */
    fun setContentTrack(title: String, artist: String, uri: Uri)

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

    /**
     * Khởi tạo progress
     *
     * @param total progress max
     */
    fun initProgress(total: Int)

    /**
     * Cập nhật progress
     *
     * @param current progress hiện tại cần cập nhật
     */
    fun updateProgress(current: Int)

    /**
     * hiển thị danh sách bài hát
     *
     * @param trackList danh sách bài hát
     */
    fun initRecycle(trackList: ArrayList<Track>)

    /**
     * chuyển tới activity detail
     *
     * @param position vị trí bài hát hiện tại
     */
    fun goDetailActivity(position: Int)
}