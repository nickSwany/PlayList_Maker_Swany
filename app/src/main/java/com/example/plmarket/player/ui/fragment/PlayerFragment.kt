package com.example.plmarket.player.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.pl_market.R
import com.example.pl_market.databinding.FragmentPlayerBinding
import com.example.plmarket.media.domain.module.PlayList
import com.example.plmarket.media.ui.PlayListState
import com.example.plmarket.media.ui.fragment.NewPlayListFragment
import com.example.plmarket.player.domain.StatePlayer
import com.example.plmarket.player.domain.models.Track
import com.example.plmarket.player.ui.adapter.PlayerPlayListAdapter
import com.example.plmarket.player.ui.viewModel.PlayerViewModel
import com.example.plmarket.search.ui.fragment.SearchFragment.Companion.EXTRA_ARTIST_NAME
import com.example.plmarket.search.ui.fragment.SearchFragment.Companion.EXTRA_ART_TRACK
import com.example.plmarket.search.ui.fragment.SearchFragment.Companion.EXTRA_COUNTRY
import com.example.plmarket.search.ui.fragment.SearchFragment.Companion.EXTRA_COllECTION_NAME
import com.example.plmarket.search.ui.fragment.SearchFragment.Companion.EXTRA_GENRE_NAME
import com.example.plmarket.search.ui.fragment.SearchFragment.Companion.EXTRA_ID
import com.example.plmarket.search.ui.fragment.SearchFragment.Companion.EXTRA_LIKE
import com.example.plmarket.search.ui.fragment.SearchFragment.Companion.EXTRA_SONG
import com.example.plmarket.search.ui.fragment.SearchFragment.Companion.EXTRA_TIME_MILLIS
import com.example.plmarket.search.ui.fragment.SearchFragment.Companion.EXTRA_TRACK
import com.example.plmarket.search.ui.fragment.SearchFragment.Companion.EXTRA_TRACK_NAME
import com.example.plmarket.search.ui.fragment.SearchFragment.Companion.EXTRA_YEAR
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*

const val DEFAULT_TIME_LEFT = "00:00"

class PlayerFragment : Fragment() {

    companion object {
        fun createArgs(
            trackId: String,
            trackName: String?,
            artistName: String?,
            trackTimeMillis: String?,
            artworkUrl100: String?,
            collectionName: String?,
            releaseDate: String?,
            primaryGenreName: String?,
            country: String?,
            previewUrl: String?,
            isFavorite: Boolean,
            track: Track
        ): Bundle = bundleOf(
            EXTRA_ID to trackId,
            EXTRA_TRACK_NAME to trackName,
            EXTRA_ARTIST_NAME to artistName,
            EXTRA_TIME_MILLIS to trackTimeMillis,
            EXTRA_ART_TRACK to artworkUrl100,
            EXTRA_COllECTION_NAME to collectionName,
            EXTRA_YEAR to releaseDate,
            EXTRA_GENRE_NAME to primaryGenreName,
            EXTRA_COUNTRY to country,
            EXTRA_SONG to previewUrl,
            EXTRA_LIKE to isFavorite,
            EXTRA_TRACK to track

        )
    }

    private var _binding: FragmentPlayerBinding? = null
    private val binding get() = _binding!!

    private var songurl = ""
    private var playListName = ""
    private var isLiked = false
    private val viewModel: PlayerViewModel by viewModel()
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<*>
    private lateinit var adapter: PlayerPlayListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlayerBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.fillData()

        songurl = requireArguments().getString(EXTRA_SONG).toString()
        viewModel.preparePlayer(songurl)

        binding.playButton.setOnClickListener {
            viewModel.playStart()
        }

        binding.tollBar.setOnClickListener {
            findNavController().popBackStack()
        }

        viewModel.checkState.observe(this) {
            checkState(it)
        }

        binding.apply {
            trackName.text = requireArguments().getString(EXTRA_TRACK_NAME)
            groupName.text = requireArguments().getString(EXTRA_ARTIST_NAME)
            countryText.text = requireArguments().getString(EXTRA_COUNTRY)
            genreText.text = requireArguments().getString(EXTRA_GENRE_NAME)
            isLiked = requireArguments().getBoolean(EXTRA_LIKE, false)
            yearNumber.text = getYearFromDatString(requireArguments().getString(EXTRA_YEAR) ?: "")
            timeLeft.text = DEFAULT_TIME_LEFT
            durationTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(
                requireArguments().getString(
                    EXTRA_TIME_MILLIS
                )?.toInt() ?: return
            )

            val albumText = requireArguments().getString(EXTRA_COllECTION_NAME)
            if (albumText != null) {
                albumName.text = albumText
            } else {
                albumName.isVisible = false
                album.isVisible = false
            }
        }

        fun getCoverArtwork(artworkUrl100: String?) =
            artworkUrl100?.replaceAfterLast('/', "512x512bb.jpg")

        val urlImage = requireArguments().getString(EXTRA_ART_TRACK)
        if (!urlImage.isNullOrEmpty()) {
            viewModel.getCoverArtwork(urlImage)
        }

        viewModel.coverArtwork.observe(this) {
            val url = it

            val cornerSize = resources.getDimensionPixelSize(R.dimen.radius_8)
            Glide.with(this)
                .load(getCoverArtwork(url))
                .centerCrop()
                .placeholder(R.drawable.error_paint_internet)
                .transform(RoundedCorners(cornerSize))
                .into(binding.trackImage)
        }

        val data = requireArguments().getString(EXTRA_YEAR).toString()
        viewModel.correctDataSong(data)
        viewModel.dataSong.observe(this) {
            binding.year.text = it
        }


        val time = requireArguments().getString(EXTRA_TIME_MILLIS)

        if (time != null) {
            viewModel.correctTimeSong(time)
            viewModel.timeSong.observe(this) {
                binding.durationTime.text = it
            }
        }

        val track = requireArguments().getParcelable<Track>(EXTRA_TRACK)

        if (track != null) {
            viewModel.checkLike(track.trackId)
        }

        viewModel.secondCounter.observe(this) {
            binding.timeLeft.text = it
        }

        binding.buttonLike.setOnClickListener {
            lifecycleScope.launch {
                if (track != null) {
                    viewModel.addFavoriteTrack(track)
                }
            }
        }

        viewModel.likeState.observe(this) { isLiked ->
            if (isLiked) {
                binding.buttonLike.setImageResource(R.drawable.button_like_true)
                if (track != null) track.isFavorite = isLiked
            } else {
                binding.buttonLike.setImageResource(R.drawable.button_like)
                if (track != null) track.isFavorite = isLiked
            }
        }

        bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
            binding.overlay.isVisible = false
            binding.overlay.isVisible = state != BottomSheetBehavior.STATE_HIDDEN
        }

        binding.buttonAdd.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            binding.overlay.isVisible = true
        }

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {

            override fun onStateChanged(bottomSheet: View, newState: Int) {

                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.overlay.isVisible = false
                    }
                    else -> {
                        binding.overlay.isVisible = true
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }
        })

        viewModel.observeStatePlayList().observe(this) {
            checkStatePlayList(it)
        }

        binding.btNewPlayList.setOnClickListener {
            findNavController().navigate(
                R.id.action_playerFragment_to_fragmentNewPlayList,
                NewPlayListFragment.createArgs(null, null, null, null)
            )
        }

        adapter = PlayerPlayListAdapter { playList ->
            track?.let {
                viewModel.addTrackToPlayList(track, playList)
                playListName = playList.name
            }
        }

        viewModel.playListState.observe(this) {
            if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_COLLAPSED ||
                bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED ||
                bottomSheetBehavior.state == BottomSheetBehavior.STATE_DRAGGING ||
                bottomSheetBehavior.state == BottomSheetBehavior.STATE_SETTLING
            ) {
                messageAdd(it)
            }
        }

        binding.rcAddToPlayList.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rcAddToPlayList.adapter = adapter
    }

    override fun onPause() {
        super.onPause()
        viewModel.onPause()
    }

    private fun getYearFromDatString(dataString: String): String? {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
        val data = inputFormat.parse(dataString)
        val outputFormat = SimpleDateFormat("yyyy", Locale.getDefault())
        return data?.let { outputFormat.format(it) }
    }

    private fun checkState(state: StatePlayer) {
        when (state) {
            StatePlayer.STATE_PLAYING -> {
                binding.playButton.setImageResource(R.drawable.button_pause)
            }
            StatePlayer.STATE_PAUSED, StatePlayer.STATE_DEFAULT -> {
                binding.playButton.setImageResource(R.drawable.button_play)
            }
            StatePlayer.STATE_PREPARED -> {
                binding.playButton.setImageResource(R.drawable.button_play)
                binding.timeLeft.text = DEFAULT_TIME_LEFT
            }
        }
    }

    private fun checkStatePlayList(statePlayList: PlayListState) {
        when (statePlayList) {
            is PlayListState.Empty -> showEmptyPlayList()
            is PlayListState.Loading -> showLoadingPlayLst()
            is PlayListState.Content -> showContentPlayList(statePlayList.playList)
        }
    }

    private fun showEmptyPlayList() {
        binding.rcAddToPlayList.isVisible = false
    }

    private fun showLoadingPlayLst() {
        binding.rcAddToPlayList.isVisible = false
    }

    private fun showContentPlayList(playList: List<PlayList>) {
        binding.apply {
            rcAddToPlayList.isVisible = true
            adapter.playList.clear()
            adapter.playList.addAll(playList)
            adapter.notifyDataSetChanged()

        }
    }

    private fun messageAdd(add: Boolean) {
        if (!add) {
            Toast.makeText(context, "Трек уже добавлен в плейлист $playListName", LENGTH_SHORT)
                .show()
        } else {
            viewModel.fillData()
            Toast.makeText(context, "Добавлено в плейлист $playListName", LENGTH_SHORT).show()
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }
    }
}