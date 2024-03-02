package com.example.plmarket.media

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.pl_market.databinding.FragmentSelectedTracksBinding
import com.example.plmarket.media.view_model.SelectedTracksViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SelectedTracksFragment : Fragment() {
    companion object {
        fun newInstance() = SelectedTracksFragment()
    }

    private  var _binding: FragmentSelectedTracksBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SelectedTracksViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSelectedTracksBinding.inflate(inflater)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
