package com.example.plmarket.media

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.pl_market.databinding.FragmentSelectedTracksBinding

class SelectedTracksFragment : Fragment() {
    companion object {

        fun newInstance() = SelectedTracksFragment()
    }

    private lateinit var binding: FragmentSelectedTracksBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSelectedTracksBinding.inflate(inflater)
        return binding.root
    }
}
