package com.turgunoff.musicplayer2.Models

/**
 * Created by Eldor Turgunov.
 * Music Player 2
 * eldorturgunov777@gmail.com
 */
public final class SongInfo {

    var Title: String? = null
    var Desc: String? = null
    var SongURL: String? = null
    var Cover: String? = null

    public constructor(title: String, desc: String, songURL: String, cover: String) {
        this.Title = title
        this.Desc = desc
        this.SongURL = songURL
        this.Cover = cover
    }

    fun getpach(): String? {
        return SongURL
    }

    fun setpach(songURL: String) {
        this.SongURL = songURL
    }

    fun gettitle(): String? {
        return Title
    }

    fun settitle(title: String) {
        this.Title = title
    }

    fun getdesc(): String? {
        return Desc
    }

    fun setdesc(desc: String) {
        this.Desc = desc
    }

    fun getimage(): String? {
        return Cover
    }

    fun setimage(cover: String) {
        this.Cover = cover
    }

}