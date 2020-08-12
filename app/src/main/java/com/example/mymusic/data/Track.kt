package com.example.mymusic.data

import android.graphics.Bitmap
import android.net.Uri
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Track(
    var id: Long,
    var title: String,
    var artist: String,
    var duration: Long,
    var image: Uri,
    val resource: Uri
) : Parcelable
