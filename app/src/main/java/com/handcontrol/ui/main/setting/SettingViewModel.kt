package com.handcontrol.ui.main.setting

import android.app.Application
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.material.snackbar.Snackbar
import com.handcontrol.R
import com.handcontrol.api.Api
import com.handcontrol.server.protobuf.Enums
import com.handcontrol.server.protobuf.Enums.ModeType.*
import com.handcontrol.server.protobuf.Settings
import io.grpc.StatusRuntimeException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SettingViewModel(app: Application) : AndroidViewModel(app) {
    private val api = Api.getApiHandler()
    private var typeWork: Enums.ModeType? = null

    val loading = MutableLiveData(true)

    val mode = MutableLiveData<String>()
    val frequency = MutableLiveData("1")
    val display = MutableLiveData<Boolean>()
    val emg = MutableLiveData<Boolean>()
    val motor = MutableLiveData<Boolean>()
    val gyroscope = MutableLiveData<Boolean>()

    init {
        viewModelScope.launch {
            try {
                val settings = api.getSettings()
                typeWork = settings.typeWork
                mode.value = when (settings.typeWork) {
                    MODE_MIO -> getString(R.string.mode_mio)
                    MODE_COMMANDS -> getString(R.string.mode_commands)
                    MODE_AUTO -> getString(R.string.mode_auto)
                    else -> ""
                }
                display.value = settings.enableDisplay
                emg.value = settings.enableEmg
                motor.value = settings.enableDriver
                gyroscope.value = settings.enableGyro
                withContext(Dispatchers.IO) {
                    val stream = api.getTelemetry()
                    if (stream.hasNext())
                        frequency.postValue(stream.next().telemetry.telemetryFrequency.toString())
                }
                loading.value = false
            } catch (e: StatusRuntimeException) {
                Toast.makeText(getApplication(), "error", Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            }
        }
    }

    fun apply(view: View) {
        viewModelScope.launch {
            Snackbar.make(view, "wait...", Snackbar.LENGTH_INDEFINITE).show()
            try {
                val settings = Settings.SetSettings.newBuilder()
                    .setTypeWork(typeWork)
                    .setTelemetryFrequency(frequency.value!!.toInt())
                    .setEnableDisplay(display.value!!)
                    .setEnableEmg(emg.value!!)
                    .setEnableDriver(motor.value!!)
                    .setEnableGyro(gyroscope.value!!)
                    .build()
                api.setSettings(settings)
                Snackbar.make(view, "Saved", Snackbar.LENGTH_SHORT).show()
            } catch (e: StatusRuntimeException) {
                Snackbar.make(view, "error", Snackbar.LENGTH_SHORT).show()
                e.printStackTrace()
            }
        }
    }

    private fun getString(@StringRes id: Int): String {
        return getApplication<Application>().resources.getString(id)
    }
}