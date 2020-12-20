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
    val frequency = MutableLiveData("")
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
            val stream = api.getTelemetry()
            for (data in stream) {
                if (!isActive)
                    break
                with (data) {
                    frequency.postValue(telemetry.telemetryFrequency.toString())
                    emg.postValue(telemetry.emgStatus.name)
                    display.postValue(telemetry.displayStatus.name)
                    gyroscope.postValue(telemetry.gyroStatus.name)
                    motor.postValue(telemetry.driverStatus.name)
                    val executedGesture = gestures.find {
                        it.id == telemetry.executableGesture
                    }
                    gesture.postValue(executedGesture?.name ?: "")
                    charge.postValue(telemetry.power.toString())
                    finger1.postValue(telemetry.thumbFingerPosition.toString())
                    finger2.postValue(telemetry.pointerFingerPosition.toString())
                    finger3.postValue(telemetry.middleFingerPosition.toString())
                    finger4.postValue(telemetry.ringFingerPosition.toString())
                    finger5.postValue(telemetry.littleFingerPosition.toString())
                }
            }
        }
    }
}