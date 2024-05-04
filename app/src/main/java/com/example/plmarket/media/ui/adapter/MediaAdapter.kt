package com.example.plmarket.media.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.plmarket.media.ui.fragment.PlayListFragment
import com.example.plmarket.media.ui.fragment.SelectedTracksFragment

const val MEDIA_TAB_SIZE = 2

class MediaAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return MEDIA_TAB_SIZE
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> SelectedTracksFragment.newInstance()
            else -> PlayListFragment.newInstance()
        }
    }

}