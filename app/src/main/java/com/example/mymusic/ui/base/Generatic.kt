package com.example.mymusic.ui.base

import android.app.Application
import com.example.mymusic.service.PlaySong

class Generatic : Application() {
 public companion object {
     var mService: PlaySong? = null
 }
}