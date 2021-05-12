package com.handcontrol.ui.main.telemetry

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.handcontrol.api.Api
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class TelemetryViewModel : ViewModel() {
    private val api = Api.getApiHandler()

    val charge = MutableLiveData("")
    val gesture = MutableLiveData("")
    val finger1 = MutableLiveData("")
    val finger2 = MutableLiveData("")
    val finger3 = MutableLiveData("")
    val finger4 = MutableLiveData("")
    val finger5 = MutableLiveData("")
    val display = MutableLiveData("")
    val emg = MutableLiveData("")
    val motor = MutableLiveData("")
    val gyroscope = MutableLiveData("")

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val gestures = api.getGestures()
            val telemetry = api.getTelemetry()

            emg.postValue(telemetry.telemetry.emg.toString())
            display.postValue(telemetry.telemetry.displayStatus.name)
            gyroscope.postValue(telemetry.telemetry.gyroStatus.name)
            motor.postValue(telemetry.telemetry.driverStatus.name)
            val executedGesture = gestures.find {
                it.id == telemetry.telemetry.executableGesture
            }
            gesture.postValue(executedGesture?.name ?: "")
            charge.postValue(telemetry.telemetry.power.toString())
            finger1.postValue(telemetry.telemetry.thumbFingerPosition.toString())
            finger2.postValue(telemetry.telemetry.pointerFingerPosition.toString())
            finger3.postValue(telemetry.telemetry.middleFingerPosition.toString())
            finger4.postValue(telemetry.telemetry.ringFingerPosition.toString())
            finger5.postValue(telemetry.telemetry.littleFingerPosition.toString())
        }
    }
}