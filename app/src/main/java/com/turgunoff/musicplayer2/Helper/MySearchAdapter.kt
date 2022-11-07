package com.turgunoff.musicplayer2.Helper

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.turgunoff.musicplayer2.Models.SongInfo
import com.turgunoff.musicplayer2.R
import com.turgunoff.musicplayer2.UI.MainActivity

/**
 * Created by Eldor Turgunov.
 * Music Player 2
 * eldorturgunov777@gmail.com
 */
class MySearchAdapter(
    val context: Context,
    myListSearches: ArrayList<SongInfo>
) : RecyclerView.Adapter<MySearchAdapter.SearchHolder>() {

    var listOfSearches = ArrayList<SongInfo>()

    private val mContext: Context

    init {
        listOfSearches = myListSearches
        mContext = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.track_item, parent, false)
        return SearchHolder(view)
    }

    override fun getItemCount(): Int {
        return listOfSearches.size
    }

    override fun onBindViewHolder(holder: MySearchAdapter.SearchHolder, position: Int) {
        return holder.bindMusic(listOfSearches[position], position)
    }

    inner class SearchHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {

        private val layoutCover = itemView?.findViewById<ConstraintLayout>(R.id.layoutCover)
        private var setCover = itemView?.findViewById<ImageView>(R.id.coverMusic)
        private var songTitle = itemView?.findViewById<TextView>(R.id.textViewTitle)
        private var songArtist = itemView?.findViewById<TextView>(R.id.textViewDesc)
        private var itemMusic = itemView?.findViewById<ConstraintLayout>(R.id.PlayMusic)

        @SuppressLint("CutPasteId")
        fun bindMusic(songInfo: SongInfo, position: Int) {
            songTitle?.text = songInfo.Title
            songArtist?.text = songInfo.Desc

            //cover
            val image = listOfSearches[position].getpach()?.let { getSongArt(it) }
            if (image != null) {
                setCover?.let {
                    Glide.with(mContext).asBitmap()
                        .load(image)
                        .into(it)
                }
            } else {
                setCover?.let {
                    Glide.with(mContext)
                        .load(R.drawable.coverrrl)
                        .into(it)
                }
            }

            if (mediaPlayer == null)
                mediaPlayer = MediaPlayer()

            itemView.setOnClickListener {
                mediaPlayer!!.reset()
                mediaPlayer!!.setDataSource(songInfo.SongURL)
                mediaPlayer!!.prepare()
                mediaPlayer!!.start()
                animationItem()
                animationCover()

                songArtist?.setTextColor(Color.parseColor("#00d6b3"))
                songTitle?.setTextColor(Color.parseColor("#13f8d1"))
                songArtist?.isSelected = true
                songTitle?.isSelected = true

                changTextTitle = songTitle?.text.toString()
                changTextArtist = songArtist?.text.toString()
                changeCover = image

                MainActivity.TitleN?.text = changTextTitle
                MainActivity.ArtistN?.text = changTextArtist
                MainActivity.PlayN?.setImageResource(R.drawable.ic_round_pause)

                if (image != null) {
                    MainActivity.Cover?.let { it1 -> Glide.with(mContext).load(image).into(it1) }
                    MainActivity.backNavar?.let { it1 ->
                        Glide.with(mContext).load(image).into(it1)
                    }
                } else {
                    MainActivity.Cover?.let { it1 ->
                        Glide.with(mContext).load(R.drawable.coverrrl).into(it1)
                    }
                    MainActivity.backNavar?.let { it1 ->
                        Glide.with(mContext).load(R.drawable.back_navar_music).into(it1)
                    }
                }

                songe = songInfo
            }
        }

        private fun animationItem() {
            val animScale = AnimationUtils.loadAnimation(mContext, R.anim.anim_pause)
            itemMusic?.startAnimation(animScale)
        }

        private fun animationCover() {
            val animScale = AnimationUtils.loadAnimation(mContext, R.anim.anim_pause)
            layoutCover?.startAnimation(animScale)
        }

    }

    //cover
    private fun getSongArt(uri: String): ByteArray? {
        val retrever = MediaMetadataRetriever()
        retrever.setDataSource(uri)
        val art: ByteArray? = retrever.embeddedPicture
        retrever.release()
        return art
    }

    //SEARCH
    fun filterList(filteredList: ArrayList<SongInfo>) {
        listOfSearches = filteredList
        notifyDataSetChanged()
    }

}