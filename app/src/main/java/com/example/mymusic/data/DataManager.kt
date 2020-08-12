package com.example.mymusic.data

/**
 * chứa các phương thức tương tác với data
 */
interface DataManager {
    /**
     * lấy tất cả các bài hát trên thiết bị
     */
    fun getListSong() : ArrayList<Track>
}
