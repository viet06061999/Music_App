package com.example.mymusic.ui.base

import com.example.mymusic.ui.base.BasePresenter.MvpViewNotAttachedException

interface MvpPresenter<V : MvpView?> {
    fun onAttach(mvpView: V)
    fun onDetach()
    fun getMvpView(): V?
    fun isViewAttached(): Boolean

    @Throws(MvpViewNotAttachedException::class)
    fun checkViewAttached()
}