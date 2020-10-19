package com.handcontrol.ui.redactor

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RedactorViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is redactor Fragment"
    }
    val text: LiveData<String> = _text
}