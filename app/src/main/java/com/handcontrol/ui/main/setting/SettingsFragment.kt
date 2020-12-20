package com.handcontrol.ui.main.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.handcontrol.R
import com.handcontrol.databinding.FragmentSettingsBinding
import com.handcontrol.ui.main.Navigation

class SettingsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentSettingsBinding.bind(view)
        binding.lifecycleOwner = this
        val viewModel: SettingViewModel by viewModels()
        binding.viewModel = viewModel
        val radioGroup = getView()?.findViewById<RadioGroup>(R.id.recognition_lang)
        radioGroup?.setOnCheckedChangeListener { _, id ->
            Navigation.recognitionLocale = when (radioGroup.findViewById<RadioButton>(id).text) {
                "Русский" -> Navigation.RUSSIAN_LOCALE_TEXT
                else -> Navigation.ENGLISH_LOCALE_TEXT
            }
        }
    }
}