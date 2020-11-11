package com.handcontrol.ui.main.telemetry

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.handcontrol.R

class TelemetryFragment : Fragment() {

    private lateinit var telemetryViewModel: TelemetryViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        telemetryViewModel =
            ViewModelProvider(this).get(TelemetryViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_telemetry, container, false)
        val textView: TextView = root.findViewById(R.id.text_choise)
        telemetryViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}