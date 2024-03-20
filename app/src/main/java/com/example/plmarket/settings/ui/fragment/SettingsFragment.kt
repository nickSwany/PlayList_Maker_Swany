package com.example.plmarket.settings.ui.fragment

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import com.example.pl_market.databinding.FragmentSettingsBinding
import com.example.plmarket.App
import com.example.plmarket.DARK_THEME
import com.example.plmarket.THEME_PREFS
import com.example.plmarket.settings.ui.viewModel.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private lateinit var sharedPreferencesHistory: SharedPreferences
    private val viewModel: SettingsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.themeSwitch.isChecked = (requireContext().applicationContext as App).switchOn

        binding.themeSwitch.setOnCheckedChangeListener { _, checked ->
            (requireContext().applicationContext as App).switch_Theme(checked)
            sharedPreferencesHistory = requireContext().getSharedPreferences(
                THEME_PREFS,
                AppCompatActivity.MODE_PRIVATE
            )
            sharedPreferencesHistory.edit {
                putBoolean(DARK_THEME, checked)
            }
        }

        binding.share.setOnClickListener {
            viewModel.shareCourseLink()
        }
        binding.support.setOnClickListener {
            viewModel.writeSupport()
        }
        binding.userAgreement.setOnClickListener {
            viewModel.openUserAgreement()
        }
    }
}
