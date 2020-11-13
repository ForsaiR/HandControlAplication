package com.handcontrol.ui.main.telemetry

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TelemetryViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is choise Fragment"
    }
    val text: LiveData<String> = _text
}