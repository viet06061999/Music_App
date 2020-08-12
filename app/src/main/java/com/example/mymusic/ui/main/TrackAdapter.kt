package com.example.mymusic.ui.main

import android.content.Context
import android.graphics.BitmapFactory
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mymusic.R
import com.example.mymusic.data.Track
import com.example.mymusic.ui.base.BaseViewHolder
import kotlinx.android.synthetic.main.track_item.view.*
import java.io.FileNotFoundException


class TrackAdapter() : RecyclerView.Adapter<BaseViewHolder>() {
    private lateinit var trackList: ArrayList<Track>
    private lateinit var mainPresenter: MainPresenter<MainActivity>
    private lateinit var context: Context

    constructor(tracks: ArrayList<Track>, presenter: MainPresenter<MainActivity>) : this() {
        trackList = tracks
        mainPresenter = presenter
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.onBind(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        context = parent.context
        return when (viewType) {
            VIEW_TYPE_NORMAL -> ViewHolder(
                    LayoutInflater.from(parent.context).inflate(R.layout.track_item, parent, false))
            VIEW_TYPE_EMPTY -> EmptyViewHolder(
                    LayoutInflater.from(parent.context).inflate(R.layout.item_empty_view, parent, false))
            else -> EmptyViewHolder(
                    LayoutInflater.from(parent.context).inflate(R.layout.item_empty_view, parent, false))
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (trackList != null && trackList.size > 0) {
            VIEW_TYPE_NORMAL
        } else {
            VIEW_TYPE_EMPTY
        }
    }

    override fun getItemCount(): Int {
        return if (trackList != null && trackList.size > 0) {
            trackList.size
        } else {
            1
        }
    }


    inner class ViewHolder(itemView: View) : BaseViewHolder(itemView) {
        var imageTrack = itemView.img_track
        var titleTrack = itemView.txt_title_track
        var artistTrack = itemView.txt_artist_track
        override fun clear() {
            imageTrack.setImageResource(R.drawable.img_no_image)
            titleTrack.text = R.string.text_no_music.toString()
            artistTrack.text = R.string.text_no_artist.toString()
        }

        override fun onBind(position: Int) {
            var track = trackList.get(position)
            var image =
                    try {
                        MediaStore.Images.Media.getBitmap(context.contentResolver, track.image)
                    } catch (e: FileNotFoundException) {
                        BitmapFactory.decodeResource(context.resources, R.drawable.img_no_image)
                    }
            imageTrack.setImageBitmap(image)
            titleTrack.text = track.title
            artistTrack.text = track.artist
            itemView.setOnClickListener {
                mainPresenter.goDetail(position)
            }
        }

    }

    class EmptyViewHolder(itemView: View) : BaseViewHolder(itemView) {
        override fun clear() {}
    }

    companion object {
        const val VIEW_TYPE_EMPTY = 0
        const val VIEW_TYPE_NORMAL = 1
    }
}
