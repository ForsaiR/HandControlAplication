package com.handcontrol.ui.main.action

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.handcontrol.model.Action

class ActionViewModel(action: Action?) : ViewModel() {
    val id = action?.id
    val name = action?.name
    val thumbFinger = MutableLiveData(action?.thumbFinger?.toString() ?: "0")
    val pointerFinger = MutableLiveData(action?.pointerFinger?.toString() ?: "0")
    val middleFinger = MutableLiveData(action?.middleFinger?.toString() ?: "0")
    val ringFinger = MutableLiveData(action?.ringFinger?.toString() ?: "0")
    val littleFinger = MutableLiveData(action?.littleFinger?.toString() ?: "0")
}

class ActionViewModelFactory(private val action: Action?) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ActionViewModel(action) as T
    }
}