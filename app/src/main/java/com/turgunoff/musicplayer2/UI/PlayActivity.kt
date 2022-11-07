package com.turgunoff.musicplayer2.UI

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.graphics.Color
import android.media.AudioManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.Toast
import com.bumptech.glide.Glide
import com.getkeepsafe.taptargetview.TapTarget
import com.getkeepsafe.taptargetview.TapTargetView
import com.turgunoff.musicplayer2.DataBase.DBManager
import com.turgunoff.musicplayer2.Helper.*
import com.turgunoff.musicplayer2.Helper.MyTrackAdapter.Companion.myListTrack
import com.turgunoff.musicplayer2.R
import com.turgunoff.musicplayer2.Services.DataService.Companion.playSong
import com.turgunoff.musicplayer2.databinding.ActivityPlayBinding
import eightbitlab.com.blurview.RenderScriptBlur
import java.util.*
import kotlin.random.Random


class PlayActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlayBinding
    var totalTime: Int = 0

    companion object {
        var seekbar: SeekBar? = null
        var Pause: ImageView? = null
        var position: Int = -1
    }

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        window.statusBarColor = Color.parseColor("#1A000000")
        binding = ActivityPlayBinding.inflate(layoutInflater)
        setContentView(binding.root)
        blurBackground()
        setCoverPlayActivity()
        setCoverBlur()

        seekbar = findViewById(R.id.seekBar)
        Pause = findViewById(R.id.imagePause)
        totalTime = mediaPlayer!!.duration

        // Set text Artist & Title
        binding.textViewTitle2.text = changTextTitle
        binding.textViewArtist2.text = changTextArtist

        //ToDo  More => create list
        binding.imageCreate.setOnClickListener {
            TapTargetView.showFor(
                this, TapTarget.forView(
                    findViewById(R.id.imageCreate),
                    "Not Active", "it can be used in later Updates"
                )
                    .tintTarget(true)
                    .outerCircleColor(R.color.colorTheme)
            )
        }

        //ToDo  add Favourite tab
        var isLike = true
        binding.imageHeart.setOnClickListener {
            if (isLike) {
                binding.imageHeart.setImageResource(R.drawable.heart1)
                isLike = false
                animationZoom(binding.imageHeart)

                //database
                val dbManager = DBManager(this)
                val values = ContentValues()
                values.put("Title", binding.textViewTitle2.text.toString())
                values.put("Artist", binding.textViewArtist2.text.toString())

                values.put(
                    "Cover", Glide.with(this)
                        .asBitmap()
                        .load(R.id.coverPlayActivity)
                        .toString()
                )

                val ID = dbManager.Insert(values)
                if (ID > 0)
                    Toast.makeText(this, " Add song to Favourite list ", Toast.LENGTH_SHORT).show()
                else
                    Toast.makeText(
                        this,
                        " ERROR!! NOT Add song to Favourite list ",
                        Toast.LENGTH_SHORT
                    ).show()

            } else if (!isLike) {
                binding.imageHeart.setImageResource(R.drawable.heart)
                isLike = true
                animationZoom(binding.imageHeart)
            }
        }

        //seekBar
        binding.seekBar.max = mediaPlayer!!.duration
        binding.seekBar.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    if (fromUser)
                        mediaPlayer!!.seekTo(progress)
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            }
        )

        Thread(Runnable {
            while (mediaPlayer != null) {
                try {
                    val msg = Message()
                    msg.what = mediaPlayer!!.currentPosition
                    handler.sendMessage(msg)
                    Thread.sleep(1000)
                } catch (_: InterruptedException) {
                }
            }
        }).start()

        if (mediaPlayer!!.isPlaying)
            binding.imagePause.setImageResource(R.drawable.pause)

        //btn pause
        binding.imagePause.setOnClickListener {
            if (mediaPlayer!!.isPlaying) {
                binding.imagePause.setImageResource(R.drawable.play)
                MainActivity.PlayN?.setImageResource(R.drawable.ic_round_play)
                mediaPlayer!!.pause()
                animationZoom(binding.imagePause)
            } else {
                binding.imagePause.setImageResource(R.drawable.pause)
                MainActivity.PlayN?.setImageResource(R.drawable.ic_round_pause)
                mediaPlayer!!.start()
                animationZoom(binding.imagePause)
            }
        }

        //ToDo btn Shuffle
        var isRepeat = false
        var isShuffle = false

        loadData(isRepeat, isShuffle)

        if (!isShuffle && !isRepeat) {
            binding.randomButton.setImageResource(R.drawable.random)
            binding.imageTekrar.setImageResource(R.drawable.refresh)
        } else if (isShuffle or !isRepeat) {
            binding.imageTekrar.setImageResource(R.drawable.refresh1)
            isRepeat = true
            binding.randomButton.setImageResource(R.drawable.random)
            isShuffle = false
        } else if (!isShuffle or isRepeat) {
            binding.randomButton.setImageResource(R.drawable.random1)
            isShuffle = true
            binding.imageTekrar.setImageResource(R.drawable.refresh)
            isRepeat = false
        }

        binding.randomButton.setOnClickListener {
            if (isShuffle) {
                binding.randomButton.setImageResource(R.drawable.random)
                isShuffle = false
                animationBlur(binding.randomButton)
            } else if (!isShuffle) {
                binding.randomButton.setImageResource(R.drawable.random1)
                isShuffle = true
                binding.imageTekrar.setImageResource(R.drawable.refresh)
                isRepeat = false
                animationBlur(binding.randomButton)
            }
            saveData(isRepeat, isShuffle)
        }

        //ToDo  btn Repeat
        //btn Repeat
        binding.imageTekrar.setOnClickListener {
            if (isRepeat) {
                binding.imageTekrar.setImageResource(R.drawable.refresh)
                isRepeat = false
                animationRepeat()
            } else if (!isRepeat) {
                binding.imageTekrar.setImageResource(R.drawable.refresh1)
                isRepeat = true
                binding.randomButton.setImageResource(R.drawable.random)
                isShuffle = false
                animationRepeat()
            }
            saveData(isRepeat, isShuffle)
        }

        //ToDo this fixes   mediaPlayer => isRepeat & isShuffle
        mediaPlayer!!.setOnCompletionListener {
            if (isRepeat)
                playSong(currentSongIndex)
            else if (isShuffle) {
                val rand = Random
                currentSongIndex = rand.nextInt((myListTrack.size - 1))
                playSong(currentSongIndex)
            } else {
                if (currentSongIndex < (myListTrack.size - 1)) {
                    playSong(currentSongIndex + 1)
                    currentSongIndex += 1
                } else {
                    playSong(0)
                    currentSongIndex = 0
                }
            }
        }

        //btn forward
        var currentPosition = 0
        val duration = mediaPlayer!!.duration
        binding.btnForward.setOnClickListener {
            currentPosition = mediaPlayer!!.currentPosition
            if (mediaPlayer!!.isPlaying && duration != currentPosition) {
                currentPosition += 5000
                mediaPlayer!!.seekTo(currentPosition)
                animationBlur(binding.btnForward)
            }
        }

        //btn backward
        var backwardTime = 5000
        binding.btnBackward.setOnClickListener {
            currentPosition = mediaPlayer!!.currentPosition
            if (mediaPlayer!!.isPlaying && currentPosition > 5000) {
                currentPosition -= 5000
                mediaPlayer!!.seekTo(currentPosition)
                animationBlur(binding.btnBackward)
            }
        }

        // btn Speaker
        binding.imageSpeaker.setOnClickListener {
            val audioManager: AudioManager = getSystemService(AUDIO_SERVICE) as AudioManager
            val maxVolume = audioManager.mediaMaxVolume
            val randomIndex = Random.nextInt(((maxVolume - 0) + 1) + 0)
            audioManager.setMediaVolume(randomIndex)
            animationBlur(binding.imageSpeaker)
        }

        //btn list
        binding.baseline.setOnClickListener {
            animationBlur(binding.baseline)
            onBackPressed()
        }
    }

    private fun saveData(repeat: Boolean, shuffle: Boolean) {
        val sharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.apply {
            putBoolean("REPEAT_KEY", repeat)
            putBoolean("SHUFFLE_KEY", shuffle)
        }.apply()
    }

    private fun loadData(repeat: Boolean, shuffle: Boolean) {
        val sharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        val repeatSh: Boolean = sharedPreferences.getBoolean("REPEAT_KEY", false)
        val shuffleSh: Boolean = sharedPreferences.getBoolean("SHUFFLE_KEY", false)
        repeat == repeatSh
        shuffle == shuffleSh
    }


    // volume
    private fun AudioManager.setMediaVolume(volumeIndex: Int) {
        this.setStreamVolume(
            AudioManager.STREAM_MUSIC,
            volumeIndex,
            AudioManager.FLAG_SHOW_UI
        )
    }

    private val AudioManager.mediaMaxVolume
        get() = this.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
    val AudioManager.mediaCurrentVolume
        get() = this.getStreamVolume(AudioManager.STREAM_MUSIC)

    //seekBar 2
    @SuppressLint("HandlerLeak")
    var handler = object : Handler() {
        @SuppressLint("SetTextI18n")
        override fun handleMessage(msg: Message) {
            val currentPosition = msg.what
            binding.seekBar.progress = currentPosition
            val elapsedTime = createTimeLabel(currentPosition)
            binding.elapsedTimeLable.text = elapsedTime

            val remainingTime = createTimeLabel(totalTime - currentPosition)
            binding.totalTimer.text = "-$remainingTime"
        }
    }

    fun createTimeLabel(time: Int): String {
        var timeLabel = ""
        val min = time / 1000 / 60
        val sec = time / 1000 % 60

        timeLabel = "$min:"
        if (sec < 10) timeLabel += "0"
        timeLabel += sec
        return timeLabel
    }


    inner class mySongThread() : Thread() {
        override fun run() {
            while (true) {
                try {
                    sleep(1000)
                } catch (ex: Exception) {
                }
                runOnUiThread {
                    if (mediaPlayer != null)
                        binding.seekBar.progress = mediaPlayer!!.currentPosition
                }
            }

        }
    }

    private fun setCoverPlayActivity() {
        if (changeCover != null) {
            Glide.with(this).asBitmap()
                .load(changeCover)
                .into(binding.coverPlayActivity as ImageView)
        } else {
            Glide.with(this)
                .load(R.drawable.coverrrl)
                .into(binding.coverPlayActivity as ImageView)
        }
    }

    private fun setCoverBlur() {
        if (changeCover != null) {
            Glide.with(this).asBitmap()
                .load(changeCover)
                .into(binding.imageblur)
        } else {
            Glide.with(this)
                .load(R.drawable.matt)
                .into(binding.imageblur)
        }
    }

    private fun blurBackground() {
        val radius = 22f
        val decorView = window.decorView
        val rootView: ViewGroup = decorView.findViewById(android.R.id.content)
        val windowBackground = decorView.background

        binding.blurView.setupWith(rootView)
            .setFrameClearDrawable(windowBackground)
            .setBlurAlgorithm(RenderScriptBlur(this))
            .setBlurRadius(radius)
            .setBlurAutoUpdate(true)
            .setHasFixedTransformationMatrix(true)
    }

    //animation
    private fun animationZoom(imageView: ImageView) {
        val animScale = AnimationUtils.loadAnimation(this, R.anim.anim_pause)
        imageView.startAnimation(animScale)
    }

    private fun animationRepeat() {
        val animScale = AnimationUtils.loadAnimation(this, R.anim.anim_repeat)
        binding.imageTekrar.startAnimation(animScale)
    }

    private fun animationBlur(imageView: ImageView) {
        val animScale = AnimationUtils.loadAnimation(this, R.anim.anim_back)
        imageView.startAnimation(animScale)
    }
}

