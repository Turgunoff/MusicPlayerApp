package com.turgunoff.musicplayer2.UI

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.turgunoff.musicplayer2.Helper.MyMusicAlbumAd
import com.turgunoff.musicplayer2.Helper.changTextAlbum
import com.turgunoff.musicplayer2.Helper.changTextArtistAlbum
import com.turgunoff.musicplayer2.Helper.changeCoverAlbum
import com.turgunoff.musicplayer2.Models.SongInfo
import com.turgunoff.musicplayer2.R
import com.turgunoff.musicplayer2.databinding.ActivityAlbumBinding

class AlbumActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAlbumBinding

    companion object {
        @SuppressLint("StaticFieldLeak")
        var artistAlbum: TextView? = null

        @SuppressLint("StaticFieldLeak")
        var imageAlbum: ImageView? = null
    }

    @SuppressLint("Range")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlbumBinding.inflate(layoutInflater)
        setContentView(binding.root)


        artistAlbum = findViewById(R.id.textArtistAlbum)
        imageAlbum = findViewById(R.id.imageViewCoverAlbum)

        binding.textAlbumName.text = changTextAlbum
        artistAlbum?.text = changTextArtistAlbum

        setCoverAlbumActivity()

        val allSongAlbum = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0"
        val sortOrder = MediaStore.Audio.Media.TITLE + " ASC"
        val cursor = this.contentResolver.query(allSongAlbum, null, selection, null, sortOrder)
        val listOfMusicAlbum = ArrayList<SongInfo>()

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    @Suppress("DEPRECATION")
                    val songAlbumURL =
                        cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA))
                    val songAlbumDesc =
                        cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST))
                    val songAlbumName =
                        cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE))
                    val cover =
                        cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID))

                    try {
                        listOfMusicAlbum.add(
                            SongInfo(
                                songAlbumName,
                                songAlbumDesc,
                                songAlbumURL,
                                cover
                            )
                        )
                    } catch (_: Exception) {
                    }

                } while (cursor.moveToNext())
            }
            cursor.close()

            val songAlbumList = findViewById<ListView>(R.id.listViewAlbum)
            songAlbumList.adapter = MyMusicAlbumAd(this.applicationContext, listOfMusicAlbum)

        }

    }

    private fun setCoverAlbumActivity() {
        if (changeCoverAlbum != null) {
            Glide.with(this).asBitmap()
                .load(changeCoverAlbum)
                .into(binding.imageViewCoverAlbum)
        } else {
            Glide.with(this)
                .load(R.drawable.coverrrl)
                .into(binding.imageViewCoverAlbum)
        }
    }
}