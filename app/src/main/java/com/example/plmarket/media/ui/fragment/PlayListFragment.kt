package com.example.plmarket.media.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.pl_market.R
import com.example.pl_market.databinding.FragmentPlayListBinding
import com.example.plmarket.media.domain.module.PlayList
import com.example.plmarket.media.ui.PlayListState
import com.example.plmarket.media.ui.view_model.PlayListViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayListFragment : Fragment() {

    companion object {
        fun newInstance() = PlayListFragment()
    }

    private var _binding: FragmentPlayListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PlayListViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlayListBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btNewPlayList.setOnClickListener {
            findNavController().navigate(
                R.id.action_mediaFragment_to_fragmentNewPlayList,
                NewPlayListFragment.createArgs(null, null, null, null)
            )
        }

        viewModel.fillData()

        viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }
    }

    private fun render(state:PlayListState) {
        when (state) {
            is PlayListState.Content -> showContent(state.playList)
            is PlayListState.Loading -> showLoading()
            is PlayListState.Empty -> showEmpty()
        }
    }

    private fun showContent(playList: List<PlayList>) {
        binding.apply {
            rcPlayList.isVisible = true
            imgEmptyPlayList.isVisible = false
            tvEmptyPlaList.isVisible = false
            //добавить адаптер для отоброжения
        }
    }

    private fun showLoading() {
        binding.apply {
            imgEmptyPlayList.isVisible = false
            tvEmptyPlaList.isVisible = false
            // добавиьт прогрессбар
        }
    }

    private fun showEmpty() {
        binding.apply {
            imgEmptyPlayList.isVisible = true
            tvEmptyPlaList.isVisible = true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btNewPlayList.setOnClickListener {
            findNavController().navigate(
                R.id.action_mediaFragment_to_fragmentNewPlayList)
        }
    }
}