package com.turgunoff.musicplayer2.UI.Controllers

import android.annotation.SuppressLint
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.turgunoff.musicplayer2.Helper.MyRecentlyAdapter
import com.turgunoff.musicplayer2.Helper.MyTrackAdapter
import com.turgunoff.musicplayer2.Models.SongInfo
import com.turgunoff.musicplayer2.R

class FragmentTracks : Fragment() {
    private lateinit var recycleTrack: RecyclerView
    private lateinit var recycleRecent: RecyclerView

    companion object {
        @SuppressLint("StaticFieldLeak")
        var musicAdapter: MyTrackAdapter? = null

        @SuppressLint("StaticFieldLeak")
        var coverList: ImageView? = null
    }

    @SuppressLint("WrongViewCast", "Recycle", "CutPasteId", "Range")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_tracks, container, false)
        recycleTrack = view.findViewById(R.id.RecyclerViewTrack)
        recycleRecent = view.findViewById(R.id.RecyclerViewRecent)

        recycleTrack.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(activity, 1)
        }

        recycleRecent.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(activity, 1, LinearLayoutManager.HORIZONTAL, false)
        }

        //list music track
        val allSong = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val sortOrder = MediaStore.Audio.Media.TITLE + " ASC"
        val cursor = requireActivity().contentResolver.query(allSong, null, null, null, sortOrder)
        val listOfSongs = ArrayList<SongInfo>()

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    val songURL =
                        cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA))
                    val songAuthor =
                        cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST))
                    val songName =
                        cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE))
                    val cover =
                        cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TRACK))

                    try {
                        listOfSongs.add(
                            SongInfo(
                                songName,
                                songAuthor,
                                songURL,
                                cover
                            )
                        )
                    } catch (e: Exception) {
//                        Toast.makeText(context,e.message.toString(),Toast.LENGTH_LONG).show()
                    }

                } while (cursor.moveToNext())
            }
            cursor.close()

            recycleTrack.adapter = MyTrackAdapter(requireActivity().applicationContext, listOfSongs)

        }

        //list music recent
        val recentSong = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val recentOrder = MediaStore.Audio.Media.DATE_ADDED
        val recentCursor =
            requireActivity().contentResolver.query(recentSong, null, null, null, recentOrder)
        val listOfRecently = ArrayList<SongInfo>()

        if (recentCursor != null) {
            if (recentCursor.moveToFirst()) {
                do {
                    val songURL =
                        recentCursor.getString(recentCursor.getColumnIndex(MediaStore.Audio.Media.DATA))
                    val songAuthor =
                        recentCursor.getString(recentCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST))
                    val songName =
                        recentCursor.getString(recentCursor.getColumnIndex(MediaStore.Audio.Media.TITLE))
                    val cover =
                        recentCursor.getString(recentCursor.getColumnIndex(MediaStore.Audio.Media.TRACK))

                    try {
                        listOfRecently.add(
                            SongInfo(
                                songName,
                                songAuthor,
                                songURL,
                                cover
                            )
                        )
                    } catch (e: Exception) {
//                        Toast.makeText(context,e.message.toString(),Toast.LENGTH_LONG).show()
                    }

                } while (recentCursor.moveToNext())
            }
            recentCursor.close()

            recycleRecent.adapter =
                MyRecentlyAdapter(requireActivity().applicationContext, listOfRecently)
        }
        return view

    }


}