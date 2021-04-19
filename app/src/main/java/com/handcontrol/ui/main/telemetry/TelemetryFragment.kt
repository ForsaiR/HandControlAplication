package com.handcontrol.ui.main.main.telemetry

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.handcontrol.R
import com.handcontrol.databinding.FragmentTelemetryBinding

class TelemetryFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_telemetry, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentTelemetryBinding.bind(view)
        binding.lifecycleOwner = this
        val viewModel: TelemetryViewModel by viewModels()
        binding.viewModel = viewModel
    }
}