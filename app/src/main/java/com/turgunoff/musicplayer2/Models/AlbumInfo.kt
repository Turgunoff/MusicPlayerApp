package com.turgunoff.musicplayer2.Models

/**
 * Created by Eldor Turgunov.
 * Music Player 2
 * eldorturgunov777@gmail.com
 */
class AlbumInfo {

    var album: String? = null
    var album2: String? = null
    var album3: String? = null
    var title: String? = null
    var artist: String? = null
    var albumUrl: String? = null

    constructor(
        Album: String,
        Album2: String,
        Album3: String,
        Title: String,
        Artist: String,
        AlbumUrl: String
    ) {
        this.album = Album
        this.album2 = Album2
        this.album3 = Album3
        this.title = Title
        this.artist = Artist
        this.albumUrl = AlbumUrl
    }

    fun getpach(): String? {
        return albumUrl
    }

    fun setpach(AlbumUrl: String) {
        this.albumUrl = AlbumUrl
    }

}