package com.handcontrol.ui.main.setting

import android.view.View
import androidx.core.app.ActivityCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.snackbar.Snackbar

class SettingViewModel : ViewModel() {
    val mode = MutableLiveData("Auto")
    val frequency = MutableLiveData("450")
    val display = MutableLiveData(false)
    val emg = MutableLiveData(false)
    val motor = MutableLiveData(false)
    val gyroscope = MutableLiveData(true)

    fun apply(view: View) {
        Snackbar.make(view, "Saved", Snackbar.LENGTH_SHORT).show()
    }
}