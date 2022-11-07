package com.turgunoff.musicplayer2.Helper

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.turgunoff.musicplayer2.UI.Controllers.FragmentAlbums
import com.turgunoff.musicplayer2.UI.Controllers.FragmentFavourites
import com.turgunoff.musicplayer2.UI.Controllers.FragmentFolders
import com.turgunoff.musicplayer2.UI.Controllers.FragmentTracks

/**
 * Created by Eldor Turgunov.
 * Music Player 2
 * eldorturgunov777@gmail.com
 */
class MyPagerAdapter (fm : FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when(position) {
            0-> FragmentTracks()
            1-> FragmentAlbums()
            2-> FragmentFolders()
            else-> return FragmentFavourites()
        }
    }

    override fun getCount(): Int {
        return 4
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position){
            0-> "Tracks"
            1-> "Albums"
            2-> "Folders"
            else-> return ""
        }
    }
}