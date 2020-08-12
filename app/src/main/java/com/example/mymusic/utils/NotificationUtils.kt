package com.example.mymusic.utils

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.provider.MediaStore
import android.support.v4.media.session.MediaSessionCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.mymusic.ui.detail.DetailActivity
import com.example.mymusic.R
import com.example.mymusic.broadcast.NotificationAction
import com.example.mymusic.data.Track
import java.io.FileNotFoundException

/**
Chứa các phương thức hỗ trợ notification
 */
object NotificationUtils {

    /**
     * Tạo notification play music
     *
     * @param context context của activity gọi phương thức
     * @param track thông tin bài hát tạo notification
     * @param playButton id của button play or pause
     * @param position vị trí hiện tại của bài hát trong list
     * @param size tổng sô các bài hát trong list
     */
    fun createNotification(
            context: Context,
            track: Track,
            playButton: Int,
            position: Int,
            size: Int
    ) {
        var notificationManagerCompat = NotificationManagerCompat.from(context)
        var mediaSessionCompat: MediaSessionCompat = MediaSessionCompat(context, AppConstraints.TAG_NOTIFICATION)
        var notification: Notification
        var uri = track.image
        var icon =
                try {
                    MediaStore.Images.Media.getBitmap(context.contentResolver,uri)
                } catch (e: FileNotFoundException) {
                    BitmapFactory.decodeResource(context.resources, R.drawable.img_no_image)
                }
        var pendingIntentPrevious: PendingIntent?
        var drawable_previous: Int

        var intentPlay: Intent
        var pendingIntentPlay: PendingIntent
        var drawable_play = playButton

        var pendingIntentNext: PendingIntent?
        var drawable_next: Int

        //previous
        if (position == 0) {
            pendingIntentPrevious = null
            drawable_previous = 0
        } else {
            var intentPrivous = Intent(context, NotificationAction::class.java).setAction(
                    AppConstraints.ACTION_PREVIOUS)
            pendingIntentPrevious = PendingIntent.getBroadcast(
                    context,
                    0,
                    intentPrivous,
                    PendingIntent.FLAG_UPDATE_CURRENT)
            drawable_previous = R.drawable.ic_skip_previous
        }
        //play
        intentPlay = Intent(context, NotificationAction::class.java).setAction(
                AppConstraints.ACTION_PLAY)
        pendingIntentPlay =
                PendingIntent.getBroadcast(context, 0, intentPlay, PendingIntent.FLAG_UPDATE_CURRENT)
        //next
        if (position == size) {
            pendingIntentNext = null
            drawable_next = 0
        } else {
            var intentNext = Intent(context, NotificationAction::class.java).setAction(
                    AppConstraints.ACTION_NEXT
            )
            pendingIntentNext = PendingIntent.getBroadcast(
                    context,
                    0,
                    intentNext,
                    PendingIntent.FLAG_UPDATE_CURRENT
            )
            drawable_next = R.drawable.ic_skip_next
        }
        //intent for click notification
        val intent = Intent(context, DetailActivity::class.java)
        intent.putExtra(AppConstraints.EXTRA_TRACK,track)
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        //create notification
        notification = NotificationCompat.Builder(context, AppConstraints.CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_music_note)
                .setContentTitle(track.title)
                .setContentText(track.artist)
                .setLargeIcon(icon)
                .setOnlyAlertOnce(true)
                .setShowWhen(false)
                .addAction(drawable_previous, AppConstraints.TITLE_PREVIOUS, pendingIntentPrevious)
                .addAction(drawable_play, AppConstraints.TITLE_PLAY, pendingIntentPlay)
                .addAction(drawable_next, AppConstraints.TITLE_NEXT, pendingIntentNext)
                .setContentIntent(pendingIntent)
                .setStyle(
                        androidx.media.app.NotificationCompat.MediaStyle()
                                .setShowActionsInCompactView(0, 1, 2)
                                .setMediaSession(mediaSessionCompat.sessionToken)
                )
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .build()
        notificationManagerCompat.notify(1, notification)
    }
}
