package com.example.plmarket.media.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.pl_market.R
import com.example.pl_market.databinding.FragmentMediatecaBinding
import com.example.plmarket.media.MediaAdapter
import com.google.android.material.tabs.TabLayoutMediator

class MediaFragment : Fragment() {
/*
    companion object {
        private const val ARGS_PLAYLIST = "playlist"
        private const val ARGS_SELECTED_TRSCKS = "selectedt_racks"

        const val TAG = "MediaFragment"

        fun newIntance()
    }

 */

    private lateinit var binding: FragmentMediatecaBinding
    private lateinit var tabMediator: TabLayoutMediator

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMediatecaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.viewPager.adapter = MediaAdapter(childFragmentManager, lifecycle)

        tabMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.selected_tracks)
                1 -> tab.text = getString(R.string.playList)
            }
        }
        tabMediator.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        tabMediator.detach()
    }
}
