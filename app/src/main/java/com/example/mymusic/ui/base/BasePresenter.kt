package com.example.mymusic.ui.base

import com.example.mymusic.data.DataManager
import com.example.mymusic.service.PlaySong

open class BasePresenter<V : MvpView?> : MvpPresenter<V> {
    private var mMvpView: V? = null
    private var mDataManager: DataManager

    constructor(dataManager: DataManager) {
        mDataManager = dataManager
    }

    override fun onAttach(mvpView: V) {
        mMvpView = mvpView
    }

    override fun onDetach() {
        mMvpView = null
    }

    override fun getMvpView(): V? {
        return mMvpView
    }

    override fun isViewAttached(): Boolean {
        return mMvpView != null
    }

    @Throws(MvpViewNotAttachedException::class)
    override fun checkViewAttached() {
        if (!isViewAttached()) throw MvpViewNotAttachedException()
    }

    class MvpViewNotAttachedException : RuntimeException("Please call Presenter.onAttach(MvpView) before" +
            " requesting data to the Presenter")

    companion object {
        private const val TAG = "BasePresenter"
    }

}
