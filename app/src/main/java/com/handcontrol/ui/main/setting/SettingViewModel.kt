package com.handcontrol.ui.main.setting

import android.app.Activity
import android.app.Application
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.material.snackbar.Snackbar
import com.handcontrol.R
import com.handcontrol.api.Api
import com.handcontrol.server.protobuf.Enums
import com.handcontrol.server.protobuf.Settings
import io.grpc.StatusRuntimeException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SettingViewModel(app: Application) : AndroidViewModel(app) {
    private val api = Api.getApiHandler()

    val loading = MutableLiveData(true)

    val display = MutableLiveData<Boolean>()
    val emg = MutableLiveData<Boolean>()
    val motor = MutableLiveData<Boolean>()
    val gyroscope = MutableLiveData<Boolean>()

    init {
        viewModelScope.launch {
            try {
                val settings = api.getSettings()
                display.value = settings.enableDisplay
                emg.value = settings.enableEmg
                motor.value = settings.enableDriver
                gyroscope.value = settings.enableGyro
                loading.value = false
            } catch (e: StatusRuntimeException) {
                Toast.makeText(getApplication(), "Ошибка", Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            }
        }
    }

    fun apply(view: View) {
        viewModelScope.launch {
            Toast.makeText(view.context, "Сохранение...", Toast.LENGTH_SHORT).show()
            try {
                val settings = Settings.SetSettings.newBuilder()
                    .setEnableDisplay(display.value!!)
                    .setEnableEmg(emg.value!!)
                    .setEnableDriver(motor.value!!)
                    .setEnableGyro(gyroscope.value!!)
                    .setPowerOff(false)
                    .build()
                api.setSettings(settings)
                Toast.makeText(view.context, "Сохранено", Toast.LENGTH_SHORT).show()
            } catch (e: StatusRuntimeException) {
                Toast.makeText(view.context, "Ошибка", Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            }
        }
    }

    fun powerOff(v: View) {
        AlertDialog.Builder(v.context)
            .setMessage(v.context.getString(R.string.power_off_confirmation))
            .setPositiveButton(v.context.getString(R.string.yes)) { _, _ ->
                viewModelScope.launch {
                    Toast.makeText(v.context, "Выключение", Toast.LENGTH_SHORT).show()
                    try {
                        val settings = Settings.SetSettings.newBuilder()
                            .setEnableDisplay(display.value!!)
                            .setEnableEmg(emg.value!!)
                            .setEnableDriver(motor.value!!)
                            .setEnableGyro(gyroscope.value!!)
                            .setPowerOff(true)
                            .build()
                        api.setSettings(settings)
                        (v.context as Activity).finish()
                    } catch (e: StatusRuntimeException) {
                        Toast.makeText(v.context, "Ошибка", Toast.LENGTH_SHORT).show()
                        e.printStackTrace()
                    }
                }
            }
            .setNegativeButton(v.context.getString(R.string.no), null)
            .show()
    }

    private fun getString(@StringRes id: Int): String {
        return getApplication<Application>().resources.getString(id)
    }
}