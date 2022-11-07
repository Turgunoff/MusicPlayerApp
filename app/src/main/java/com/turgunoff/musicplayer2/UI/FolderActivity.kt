package com.turgunoff.musicplayer2.UI

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.turgunoff.musicplayer2.Helper.MyMusicFolderAp
import com.turgunoff.musicplayer2.Helper.changAddressFolder
import com.turgunoff.musicplayer2.Helper.changNameFolder
import com.turgunoff.musicplayer2.Models.SongInfo
import com.turgunoff.musicplayer2.R
import com.turgunoff.musicplayer2.databinding.ActivityFolderBinding

class FolderActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFolderBinding

    private lateinit var recyclerFolder: RecyclerView

    @SuppressLint("Range")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFolderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.textFolderName.text = changNameFolder
        binding.textAddressFolder.text = changAddressFolder

        recyclerFolder = findViewById(R.id.listMusicFolder)

        recyclerFolder.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(this@FolderActivity, 1)
        }

        MainActivity.Cover = findViewById(R.id.coverNavar)

        val allMusicFolder = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val sortOrder = MediaStore.Audio.Media.TITLE + " ASC"
        val cursor = this.contentResolver.query(allMusicFolder, null, null, null, sortOrder)
        val listOfMusicFolder = ArrayList<SongInfo>()

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    @Suppress("DEPRECATION")
                    val songFolderURL =
                        cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA))
                    val songFolderDesc =
                        cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST))
                    val songFolderName =
                        cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE))
                    val cover =
                        cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TRACK))

                    try {
                        listOfMusicFolder.add(
                            SongInfo(
                                songFolderName,
                                songFolderDesc,
                                songFolderURL,
                                cover
                            )
                        )
                    } catch (_: Exception) {
                    }

                } while (cursor.moveToNext())
            }
            cursor.close()

            binding.listMusicFolder.adapter =
                MyMusicFolderAp(this.applicationContext, listOfMusicFolder)

        }
    }
}