package com.turgunoff.musicplayer2.UI

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.cardview.widget.CardView
import androidx.core.app.NotificationCompat
import com.turgunoff.musicplayer2.R
import com.turgunoff.musicplayer2.databinding.ActivityAboutBinding

class AboutActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAboutBinding
    private lateinit var notification: NotificationCompat.Builder
    private lateinit var manage: NotificationManager
    private val channel = "notify"
    private lateinit var notificationChannel: NotificationChannel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //notification
        manage = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel =
                NotificationChannel(channel, "notify", NotificationManager.IMPORTANCE_HIGH)
            manage.createNotificationChannel(notificationChannel)
        }

        notification = NotificationCompat.Builder(this, channel)
            .setContentTitle(binding.textView2.text)
            .setContentText(binding.textView9.text)
            .setSmallIcon(R.drawable.ic_baseline_music_note_24)
            .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.pause))
            .setAutoCancel(true)
            .setColor(getColor(R.color.colorTheme))
            .addAction(R.drawable.ic_round_previous, "previous", null)
            .addAction(R.drawable.ic_round_pause, "play", null)
            .addAction(R.drawable.ic_round_next, "next", null)

        binding.imageView.setOnClickListener {
            manage.notify("media", 1, notification.build())
        }

        //click
        binding.cardViewLinkedinMe.setOnClickListener {
            goToUrl("https://www.linkedin.com/in/soheil-heydari/")
            animationBlur(binding.cardViewLinkedinMe)
        }

        binding.cardViewGithub.setOnClickListener {
            goToUrl("https://github.com/soheilheydari21")
            animationBlur(binding.cardViewGithub)
        }

        binding.cardViewSupporters.setOnClickListener {
            goToUrl("https://www.linkedin.com/in/mohammadkhoddami/")
            animationBlur(binding.cardViewSupporters)
        }

    }

    private fun goToUrl(open: String) {
        val uri: Uri = Uri.parse(open)
        startActivity(Intent(Intent.ACTION_VIEW, uri))
    }

    private fun animationBlur(cardView: CardView) {
        val animScale = AnimationUtils.loadAnimation(this, R.anim.anim_back)
        cardView.startAnimation(animScale)
    }

}