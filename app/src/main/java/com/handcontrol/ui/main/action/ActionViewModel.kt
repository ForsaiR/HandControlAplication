package com.handcontrol.ui.main.action

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.handcontrol.model.Action

class ActionViewModel(action: Action?) : ViewModel() {
    var thumbFinger = MutableLiveData(action?.thumbFinger ?: 0)
    var pointerFinger = MutableLiveData(action?.pointerFinger ?: 0)
    var middleFinger = MutableLiveData(action?.middleFinger ?: 0)
    var ringFinger = MutableLiveData(action?.ringFinger ?: 0)
    var littleFinger = MutableLiveData(action?.littleFinger ?: 0)
}

class ActionViewModelFactory(private val action: Action?) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ActionViewModel(action) as T
    }
}