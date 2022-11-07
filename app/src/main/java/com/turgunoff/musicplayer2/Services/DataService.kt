package com.turgunoff.musicplayer2.Services

import com.turgunoff.musicplayer2.Helper.mediaPlayer
import com.turgunoff.musicplayer2.Helper.songe

/**
 * Created by Eldor Turgunov.
 * Music Player 2
 * eldorturgunov777@gmail.com
 */
class DataService {

    //ToDo this fixes   mediaPlayer => isRepeat & isShuffle 2
    companion object {
        fun playSong(songIndex: Int = 0) {
            mediaPlayer!!.reset()
            mediaPlayer!!.setDataSource(songe!!.SongURL)
            mediaPlayer!!.prepare()
            mediaPlayer!!.start()
        }
    }

}