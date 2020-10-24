package com.handcontrol.ui.execution

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ExecutionViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is execution Fragment"
    }
    val text: LiveData<String> = _text
}