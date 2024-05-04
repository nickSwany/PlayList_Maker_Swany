package com.example.plmarket.media.ui.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.pl_market.R
import com.example.pl_market.databinding.FragmentNewPlayListBinding
import com.example.plmarket.App
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout

class FragmentNewPlayList : Fragment() {

    private var _binding: FragmentNewPlayListBinding? = null
    private val binding get() = _binding!!

    private lateinit var confirmDialog: MaterialAlertDialogBuilder

    private var uriPlayer = ""
    private lateinit var callback: OnBackPressedCallback


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
        val bottomNavigation =
            requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigation.isVisible = true
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bottomNavigation =
            requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigation.isVisible = false

        binding.tollBar.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.apply {
            appCompatButton2.isEnabled = edTextNamePlaylistInput.text != null
        }

        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    val cornerSize = binding.root.resources.getDimensionPixelSize(R.dimen.radius_8)
                    //binding.albumImage.setImageURI(uri)
                    Glide.with(binding.root)
                        .load(uri)
                        .centerCrop()
                        .transform(RoundedCorners(cornerSize))
                        .into(binding.albumImage)
                    uriPlayer = "${uri.toString().reversed().substringBefore("/")}.jpg"
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
                val isEnablesTextEmpty = p0.isNullOrEmpty()
                binding.appCompatButton2.isEnabled = !isEnablesTextEmpty
            }
        })
        binding.appCompatButton2.isEnabled = false

        onBackPressedDispatcher()

        binding.apply {
            focusForEditText(edTextNamePlaylistInput, edTextNamePlaylist)
            focusForEditText(edTextOpisanieInput, edTextOpisanie)
        }
    }

    private fun onBackPressedDispatcher() {
        callback = requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            binding.apply {
                if (uriPlayer != ""
                    || edTextNamePlaylistInput.toString().isNotEmpty()
                    || edTextOpisanieInput.toString().isNotEmpty()
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
                        AppCompatResources.getColorStateList(requireContext(), R.color.blue) //нужно посмотреть, возможно понадобится сделать двойно цвет, в зависимости от состояния
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

}