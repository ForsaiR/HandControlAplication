package com.handcontrol.ui.main.gestures

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.handcontrol.api.GetGestures
import com.handcontrol.api.PerformGesture

class GesturesViewModel : ViewModel() {
    val listData by lazy { MutableLiveData(GetGestures().invoke()) }

    fun performGesture(id: Int) {
        PerformGesture().invoke(id)
    }
}