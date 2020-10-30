package com.handcontrol.ui.choise

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ChoiseViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is choise Fragment"
    }
    val text: LiveData<String> = _text
}