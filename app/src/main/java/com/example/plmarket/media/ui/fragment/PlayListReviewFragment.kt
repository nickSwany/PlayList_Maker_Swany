package com.example.plmarket.media.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.pl_market.R
import com.example.pl_market.databinding.FragmentPlaylistReviewBinding
import com.example.plmarket.media.ui.PlayListStateTrack
import com.example.plmarket.media.ui.adapter.TrackPlayListAdapter
import com.example.plmarket.media.ui.adapter.TrackPlayListLongClickListener
import com.example.plmarket.media.ui.view_model.PlayListReviewViewModel
import com.example.plmarket.player.domain.models.Track
import com.example.plmarket.player.ui.fragment.PlayerFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel

const val EXTRA_NAME_PLAYLIST = "name_playlist"
const val EXTRA_DESCRIPTION = "description"
const val EXTRA_URL = "uri"
const val EXTRA_PLAYLIST_ID = "playlist_id"

class PlayListReviewFragment : Fragment(), TrackPlayListLongClickListener {

    companion object {
        fun createArgs(
            namePlayList: String,
            description: String?,
            uri: String?,
            playlistId: Int,
        ): Bundle = bundleOf(
            EXTRA_NAME_PLAYLIST to namePlayList,
            EXTRA_DESCRIPTION to description,
            EXTRA_URL to uri,
            EXTRA_PLAYLIST_ID to playlistId
        )
    }

    private var _binding: FragmentPlaylistReviewBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PlayListReviewViewModel by viewModel()

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<*>
    private lateinit var bottomSheetBehaviorSettings: BottomSheetBehavior<*>

    private lateinit var deleteTrack: MaterialAlertDialogBuilder
    private lateinit var deletePlayList: MaterialAlertDialogBuilder

    private var trackIdPlayList: String = "null"
    var uri: String = "null"

    val adapter = TrackPlayListAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlaylistReviewBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.imgShape.setOnClickListener {
            sharePlayList()
        }

        binding.tvShare.setOnClickListener {
            sharePlayList()
        }

        val idPlaylist = requireArguments().getInt(EXTRA_PLAYLIST_ID)
        viewModel.fillDataTracks(idPlaylist)
        viewModel.showInfoPlayList(idPlaylist)

        bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheetTrack).apply {
            state = BottomSheetBehavior.STATE_COLLAPSED
        }

        bottomSheetBehaviorSettings = BottomSheetBehavior.from(binding.bottomSheetSettings).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        viewModel.observePlayList().observe(this) {
            binding.apply {
                tvNamePlayList.text = it.name
                description.text = it.description
                uri = it.uri.toString()
                Glide.with(binding.root)
                    .load(uri)
                    .centerCrop()
                    .placeholder(R.drawable.placholder_for_play_list)
                    .into(imgPlayList)
            }
        }

        viewModel.observeStateTrackReview().observe(this) {
            when (it) {
                is PlayListStateTrack.Empty -> showEmpty()
                is PlayListStateTrack.Content -> showContent(it.tracks)
            }
        }

        binding.rvTracks.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvTracks.adapter = adapter

        deleteTrack = MaterialAlertDialogBuilder(requireContext())
            .setMessage("Хотите удалить трек?")
            .setNegativeButton("Нет") { dialog, which ->

            }
            .setPositiveButton("Да") { dialog, which ->
                viewModel.deleteTrackInPlayList(trackIdPlayList, idPlaylist)
            }

        viewModel.observeStateDeleteTrack().observe(this) {
            if (it) viewModel.fillDataTracks(idPlaylist)
        }

        binding.apply {
            imgMenu.setOnClickListener {
                tvNamePlayLisSettings.text = tvNamePlayList.text
                tvCurrentTracksSettings.text = curruntTrack.text
                val cornerSize = resources.getDimensionPixelSize(R.dimen.dp_2)
                Glide.with(binding.root)
                    .load(uri)
                    .centerCrop()
                    .placeholder(R.drawable.error_paint_internet)
                    .transform(RoundedCorners(cornerSize))
                    .into(imgTrack)
                bottomSheetBehaviorSettings.state = BottomSheetBehavior.STATE_COLLAPSED
            }
        }

        bottomSheetBehaviorSettings.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_COLLAPSED -> binding.overlay.isVisible = true
                    BottomSheetBehavior.STATE_HIDDEN -> binding.overlay.isVisible = false
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })

        binding.tvDeleteSettings.setOnClickListener {
            val namePlayList = binding.tvNamePlayList.text
            deletePlayList = MaterialAlertDialogBuilder(requireContext())
                .setMessage("Хотите удалить плейлист $namePlayList?")
                .setNegativeButton("Нет") { dialog, which ->

                }
                .setPositiveButton("Да") { dialog, which ->
                    viewModel.deletePlayList(idPlaylist)
                }
            deletePlayList.show()
        }

        viewModel.observeStateDeletePlayList().observe(this) {
            if (it) findNavController().popBackStack()
        }

        binding.tvTvEditInfoSettings.setOnClickListener {
            findNavController().navigate(
                R.id.action_playListReviewFragment_to_fragmentNewPlayList,
                NewPlayListFragment.createArgs(
                    binding.tvNamePlayList.text.toString(),
                    binding.description.text.toString(),
                    uri,
                    idPlaylist
                )
            )
        }
    }

    override fun onClick(track: Track) {
        findNavController().navigate(
            R.id.action_playListReviewFragment_to_playerFragment,
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

    override fun onLongClick(trackId: String) {
        deleteTrack.show()
        trackIdPlayList = trackId
    }

    private fun sharePlayList() {
        if (adapter.tracks.isEmpty()) {
            Toast.makeText(
                context,
                "В этом плейлисте нет списка треков, которым можно поделиться",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            val trackName = binding.tvNamePlayList.text.toString()
            val description = binding.description.text.toString()
            val currentTrack = binding.curruntTrack.text.toString()
            sharePlayList(adapter.tracks, trackName, description, currentTrack)
        }
    }

    private fun sharePlayList(
        tracks: List<Track>,
        nameTrack: String,
        description: String,
        currentTrack: String
    ) {
        viewModel.sharePlayList(tracks, nameTrack, description, currentTrack)
    }

    private fun showContent(tracks: List<Track>) {
        binding.rvTracks.isVisible = true
        var timeSec = 0
        for (track in tracks) {
            timeSec += track.trackTimeMillis?.toInt() ?: 0
        }
        val timeMin = timeSec / 60000
        val trackCount = tracks.size
        adapter.tracks.clear()
        adapter.tracks.addAll(tracks.reversed())

        val duration = timeMin.toString() + " " + endingMinute(timeMin)
        val currentTrack = trackCount.toString() + " " + endingTracks(trackCount)
        binding.sumTrack.text = duration
        binding.curruntTrack.text = currentTrack
        adapter.notifyDataSetChanged()
    }

    private fun showEmpty() {
        binding.rvTracks.isVisible = false
        Toast.makeText(
            context,
            "В этом плейлисте нет треков.",
            Toast.LENGTH_SHORT
        ).show()
        binding.sumTrack.text = getString(R.string.zero_minut)
        binding.curruntTrack.text = getString(R.string.zero_trecks)
    }

    private fun endingTracks(count: Int): String {
        return when (count % 10) {
            1 -> getString(R.string.trek)
            in 2..4 -> getString(R.string.treka)
            else -> getString(R.string.trekov)
        }
    }

    private fun endingMinute(min: Int): String {
        return when (min % 10) {
            1 -> getString(R.string.minuta)
            in 2..4 -> getString(R.string.minutes)
            else -> getString(R.string.minut)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}