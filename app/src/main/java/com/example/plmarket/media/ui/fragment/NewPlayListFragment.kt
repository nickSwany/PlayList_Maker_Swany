package com.example.plmarket.media.ui.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.pl_market.R
import com.example.pl_market.databinding.FragmentNewPlayListBinding
import com.example.plmarket.media.domain.module.PlayList
import com.example.plmarket.media.ui.CreateNewPlayListState
import com.example.plmarket.media.ui.view_model.NewPlayListViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

const val NAME_PLAY_PLAY_LIST = "name_play_list"
const val DESCRIPTION_PLAY_LIST = "description_play_list"
const val IMAGE_PLAY_LIST = "image_play_list"
const val ID_PLAY_LIST = "id_play_list"

class NewPlayListFragment : Fragment() {

    companion object {
        fun createArgs(
            namePlayList: String?,
            descriptionPlayList: String?,
            imagePlayList: String?,
            idPlayList: Int?
        ): Bundle = bundleOf(
            NAME_PLAY_PLAY_LIST to namePlayList,
            DESCRIPTION_PLAY_LIST to descriptionPlayList,
            IMAGE_PLAY_LIST to imagePlayList,
            ID_PLAY_LIST to idPlayList
        )
    }


    private var _binding: FragmentNewPlayListBinding? = null
    private val binding get() = _binding!!

    private lateinit var confirmDialog: MaterialAlertDialogBuilder

    private var uriPlayList = ""
    private lateinit var callback: OnBackPressedCallback

    private val viewModel: NewPlayListViewModel by viewModel()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNewPlayListBinding.inflate(inflater)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val idPlayList = requireArguments().getInt(ID_PLAY_LIST)
        viewModel.checkStateCreateAndNew(idPlayList)

        viewModel.observeStateNewAndCreatePlayList().observe(this) { state ->
            when (state) {
                is CreateNewPlayListState.CreatePlayList -> {
                    createPlayList(state.playList)
                    binding.btAddPlayList.setOnClickListener {
                        val uri = viewModel.getUri(uriPlayList)
                        viewModel.updatePlayList(
                            binding.edTextNamePlaylistInput.text.toString(),
                            binding.edTextDescriptionInput.text.toString(),
                            uri,
                            idPlayList
                        )
                        findNavController().popBackStack()
                    }
                    binding.tollBar.setOnClickListener {
                        findNavController().popBackStack()
                    }
                }

                is CreateNewPlayListState.NewPlayList -> {
                    addPlayList()
                }
            }
        }

        binding.tollBar.setOnClickListener {
            if (binding.edTextNamePlaylistInput.text.toString().isNotEmpty()
                || binding.edTextDescriptionInput.text.toString().isNotEmpty()
                || uriPlayList != ""
            ) {
                confirmDialog.show()
            } else findNavController().popBackStack()
        }

        onBackPressedDispatcher()

        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    val cornerSize = binding.root.resources.getDimensionPixelSize(R.dimen.radius_8)
                    Glide.with(binding.root)
                        .load(uri)
                        .centerCrop()
                        .transform(RoundedCorners(cornerSize))
                        .into(binding.albumImage)
                    viewModel.addImgToStorage(uri)
                    uriPlayList = "${uri.toString().reversed().substringBefore("/")}.jpg"
                }
            }
        binding.albumImage.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        confirmDialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle("Завершить?")
            .setMessage("Все несохраненные данные будут потеряны")
            .setNeutralButton("Отмена") { dialog, which ->

            }
            .setPositiveButton("Завершить") { dialog, which ->
                findNavController().popBackStack()
            }

        binding.edTextNamePlaylistInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {
                val isEnablesTextEmpty = p0.isNullOrBlank()
                binding.btAddPlayList.isEnabled = !isEnablesTextEmpty
            }
        })
        binding.btAddPlayList.isEnabled = false

        binding.apply {
            focusForEditText(edTextNamePlaylistInput, edTextNamePlaylist)
            focusForEditText(edTextDescriptionInput, edTextDescription)
        }
    }

    private fun onBackPressedDispatcher() {
        callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            binding.apply {
                if (uriPlayList != ""
                    || edTextNamePlaylistInput.text.toString().isNotEmpty()
                    || edTextDescriptionInput.text.toString().isNotEmpty()
                ) {
                    confirmDialog.show()
                } else {
                    findNavController().popBackStack()
                }
            }
        }
    }

    private fun focusForEditText(editText: EditText, otherEditText: TextInputLayout) {
        editText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                AppCompatResources.getColorStateList(requireContext(), R.color.blue)
                    ?.let {
                        otherEditText.setBoxStrokeColorStateList(it)
                        otherEditText.defaultHintTextColor = it
                    }
            } else {
                when (editText.text.isNullOrEmpty()) {
                    true -> {
                        AppCompatResources.getColorStateList(
                            requireContext(),
                            R.color.blue
                        )
                            ?.let {
                                otherEditText.setBoxStrokeColorStateList(it)
                                otherEditText.defaultHintTextColor =
                                    AppCompatResources.getColorStateList(
                                        requireContext(),
                                        R.color.black
                                    )
                            }
                    }

                    false ->
                        AppCompatResources.getColorStateList(requireContext(), R.color.blue)
                            ?.let {
                                otherEditText.setBoxStrokeColorStateList(it)
                                otherEditText.defaultHintTextColor = it
                            }
                }
            }
        }
    }

    private fun addPlayList() {
        binding.apply {
            btAddPlayList.setOnClickListener {
                val uri = viewModel.getUri(uriPlayList)
                lifecycleScope.launch {
                    viewModel.addPlayList(
                        name = edTextNamePlaylistInput.text.toString(),
                        description = edTextDescriptionInput.text.toString(),
                        uri = uri
                    )
                }
                Toast.makeText(
                    context,
                    "Плейлист ${edTextNamePlaylistInput.text} создан",
                    LENGTH_SHORT
                ).show()
                findNavController().popBackStack()
            }
        }
    }

    private fun createPlayList(playList: PlayList) {
        binding.apply {
            edTextNamePlaylistInput.setText(playList.name)
            edTextDescriptionInput.setText(playList.description)
            edTextNamePlaylistInput.requestFocus()
            edTextDescriptionInput.requestFocus()
            edTextDescriptionInput.clearFocus()
            uriPlayList = playList.uri.toString().substringAfterLast("/")

            Glide.with(binding.root)
                .load(uriPlayList)
                .centerCrop()
                .placeholder(R.drawable.error_paint_internet)
                .into(albumImage)
        }
    }
}