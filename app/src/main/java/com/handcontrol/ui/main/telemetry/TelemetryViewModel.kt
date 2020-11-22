package com.handcontrol.ui.main.telemetry

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TelemetryViewModel : ViewModel() {
    val charge = MutableLiveData("99%")
    val gesture = MutableLiveData("Gesture1")
    val frequency = MutableLiveData("666 Hz")
    val finger1 = MutableLiveData("100")
    val finger2 = MutableLiveData("100")
    val finger3 = MutableLiveData("100")
    val finger4 = MutableLiveData("100")
    val finger5 = MutableLiveData("100")
    val display = MutableLiveData("ON")
    val emg = MutableLiveData("ON")
    val motor = MutableLiveData("ON")
    val gyroscope = MutableLiveData("ON")
}