package com.example.mymusic.data

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import com.example.mymusic.R
import java.io.FileNotFoundException

/**
 * Mô tả các phương thức tương tác với data
 */
class AppDataManager : DataManager {
    private val sArtworkUri =
            Uri.parse("content://media/external/audio/albumart")
    private var mcontext: Context

    constructor(context: Context) {
        mcontext = context
    }

    override fun getListSong(): ArrayList<Track> {
        val tracks = ArrayList<Track>()
        //retrieve item_song info
        val musicResolver = mcontext.contentResolver
        val musicUri: Uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val musicCursor: Cursor? = musicResolver.query(musicUri, null, null, null, null)
        if (musicCursor != null && musicCursor.moveToFirst()) {
            //get columns
            val titleColumn: Int =
                    musicCursor.getColumnIndex(MediaStore.Audio.Media.TITLE)
            val idColumn: Int =
                    musicCursor.getColumnIndex(MediaStore.Audio.Media._ID)
            val albumID: Int = musicCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)
            val artistColumn: Int =
                    musicCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)
            val songLink: Int = musicCursor.getColumnIndex(MediaStore.Audio.Media.DATA)
            val duration: Int = musicCursor.getColumnIndex(MediaStore.Audio.Media.DURATION)
            //add songs to list
            do {
                if (musicCursor.getString(songLink).endsWith(".mp3")) {
                    val thisId: Long = musicCursor.getLong(idColumn)
                    val thisTitle: String = musicCursor.getString(titleColumn)
                    val thisArtist: String = musicCursor.getString(artistColumn)
                    val thisSongLink: Uri = Uri.parse(musicCursor.getString(songLink))
                    val some: Long = musicCursor.getLong(albumID)
                    val uri: Uri = ContentUris.withAppendedId(sArtworkUri, some)
                    val thisDuration = musicCursor.getLong(duration)

                    var track =
                            Track(thisId, thisTitle, thisArtist, thisDuration, uri, thisSongLink)
                    tracks.add(track)
                }
            } while (musicCursor.moveToNext())
        }
        assert(musicCursor != null)
        musicCursor?.close()
        return tracks
    }

}
