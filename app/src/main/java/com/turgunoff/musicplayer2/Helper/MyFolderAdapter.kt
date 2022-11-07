package com.turgunoff.musicplayer2.Helper

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.turgunoff.musicplayer2.Models.SongInfo
import com.turgunoff.musicplayer2.R

/**
 * Created by Eldor Turgunov.
 * Music Player 2
 * eldorturgunov777@gmail.com
 */

var changNameFolder = "Title"
var changAddressFolder = "Artist"

class MyFolderAdapter(
    val context: Context,
    private val folders: ArrayList<SongInfo>,
    private val itemClicked: (SongInfo) -> Unit
) : RecyclerView.Adapter<MyFolderAdapter.FolderHolder>() {

    companion object {
        var myFolderSong = ArrayList<SongInfo>()
    }

    private val mContext: Context
    init {
        myFolderSong = folders
        mContext = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FolderHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.folder_item, parent, false)
        return FolderHolder(view, itemClicked)
    }

    override fun getItemCount(): Int {
        return folders.size
    }

    override fun onBindViewHolder(holder: MyFolderAdapter.FolderHolder, position: Int) {
        return holder.bindFolder(folders[position])
    }

    inner class FolderHolder(itemView: View?, val itemClicked: (SongInfo) -> Unit) : RecyclerView.ViewHolder(itemView!!) {
        private var folderTitle = itemView?.findViewById<TextView>(R.id.folderName)
        private var folderAddress = itemView?.findViewById<TextView>(R.id.folderAddres)

        fun bindFolder(songInfo:SongInfo) {
            folderTitle?.text = songInfo.Title
            folderAddress?.text = songInfo.Desc

            itemView.setOnClickListener { itemClicked(songInfo)
                changNameFolder = folderTitle?.text.toString()
                changAddressFolder = folderAddress?.text.toString()
            }
        }

    }


}