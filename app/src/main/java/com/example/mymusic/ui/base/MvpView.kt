package com.example.mymusic.ui.base

interface MvpView {
    fun showLoading()

    fun hideLoading()

    fun hideKeyboard()

    fun isMyServiceRunning(serviceClass: Class<*>): Boolean
}