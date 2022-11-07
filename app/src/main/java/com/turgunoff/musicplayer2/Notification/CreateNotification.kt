package com.turgunoff.musicplayer2.Notification

import android.content.Context
import android.os.Build
import android.support.v4.media.session.MediaSessionCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.turgunoff.musicplayer2.Models.SongInfo
import com.turgunoff.musicplayer2.R

/**
 * Created by Eldor Turgunov.
 * Music Player 2
 * eldorturgunov777@gmail.com
 */
class CreateNotification {

    companion object {

        fun notification(
            context: Context,
            track: SongInfo,
            playButton: Int,
            pause: Int,
            size: Int
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                val notificationManagerCompat: NotificationManagerCompat =
                    NotificationManagerCompat.from(context)
                val mediaSessionCompat: MediaSessionCompat = MediaSessionCompat(context, "tag")

                val CHANNEL_ID = "Your_Channel_ID"
                var ACTION_PREVIUOS = "Action_Previous"
                var CHANNEL_PLAY = "Action_Play"
                var CHANNEL_NEXT = "Action_Next"

                val notification = NotificationCompat.Builder(context, CHANNEL_ID)
                    .setContentTitle(track.gettitle())
                    .setContentText(track.getdesc())
                    .setSmallIcon(R.drawable.ic_baseline_arrow_back_24)
                    .setOnlyAlertOnce(true)
                    .setShowWhen(false)
                    .setPriority(NotificationCompat.PRIORITY_LOW)
                    .build()
                notificationManagerCompat.notify(1, notification)

            }

        }

    }

}