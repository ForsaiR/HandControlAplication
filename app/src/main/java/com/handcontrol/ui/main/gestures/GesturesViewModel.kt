package com.handcontrol.ui.main.gestures

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GesturesViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is execution Fragment"
    }
    val text: LiveData<String> = _text
}