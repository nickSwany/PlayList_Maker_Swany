package com.example.plmarket.media.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pl_market.R
import com.example.pl_market.databinding.FragmentSelectedTracksBinding
import com.example.plmarket.media.ui.FavoriteState
import com.example.plmarket.media.ui.adapter.FavoriteAdapter
import com.example.plmarket.media.ui.view_model.FavoriteTracksViewModel
import com.example.plmarket.player.domain.models.Track
import com.example.plmarket.player.ui.fragment.PlayerFragment
import com.example.plmarket.search.ui.fragment.SearchFragment
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class SelectedTracksFragment : Fragment() {
    companion object {
        fun newInstance() = SelectedTracksFragment()
    }

    private var _binding: FragmentSelectedTracksBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FavoriteTracksViewModel by viewModel()

    private var isClickAllowed = true

    private val adapter = FavoriteAdapter { track ->
        if (clickDebounce()) {
            openPlayerToIntent(track)
        }
    }

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            RCFavoriteTracks.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            RCFavoriteTracks.adapter = adapter
        }

        viewModel.fillData()
        viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }
    }

    private fun render(state: FavoriteState) {
        when (state) {
            is FavoriteState.Loading -> showLoading()
            is FavoriteState.Empty -> showEmpty()
            is FavoriteState.Content -> {
                val tracks = state.track.reversed()
                showContent(tracks)
            }
        }
    }

    private fun showLoading() {
        binding.apply {
            progressbarFavoriteTracks.isVisible = true
            RCFavoriteTracks.isVisible = false
            tvEmpty.isVisible = false
            imageView.isVisible = false
        }
    }

    private fun showContent(track: List<Track>) {
        binding.apply {
            progressbarFavoriteTracks.isVisible = false
            tvEmpty.isVisible = false
            imageView.isVisible = false
            RCFavoriteTracks.isVisible = true
        }
        adapter.trackListFavorite.clear()
        adapter.trackListFavorite.addAll(track)
        adapter.notifyDataSetChanged()
    }

    private fun showEmpty() {
        binding.apply {
            progressbarFavoriteTracks.isVisible = false
            RCFavoriteTracks.isVisible = false
            tvEmpty.isVisible = true
            imageView.isVisible = true
        }
    }

    private fun openPlayerToIntent(track: Track) {
        findNavController().navigate(
            R.id.action_mediaFragment_to_playerFragment,
            PlayerFragment.createArgs(
                track.trackId,
                track.trackName.toString(),
                track.artistName,
                track.trackTimeMillis,
                track.artworkUrl100,
                track.collectionName,
                track.releaseDate,
                track.country,
                track.primaryGenreName,
                track.previewUrl,
                track.isFavorite,
                track
            )
        )
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            lifecycleScope.launch {
                delay(SearchFragment.CLICK_DEBOUNCE_DELAY)
                isClickAllowed = true
            }
        }
        return current
    }

    override fun onResume() {
        super.onResume()
        viewModel.fillData()
        adapter.notifyDataSetChanged()
    }
}
