package com.example.mymusic.utils

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.provider.Settings

object CommonUtils {
        private const val TAG = "CommonUtils"
        fun showLoadingDialog(context: Context?): ProgressDialog {
            val progressdialog = ProgressDialog(context)
            progressdialog.setMessage("Please Wait....")
            return progressdialog
        }

        @SuppressLint("all")
        fun getDeviceId(context: Context): String {
            return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    }
}