package com.handcontrol.ui.main.action

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.handcontrol.model.Action
import com.handcontrol.repository.GestureRepository

class ActionViewModel(action: Action?, private val position: Int? = null) : ViewModel() {
    val id = action?.id
    val name = action?.name
    val thumbFinger = MutableLiveData(action?.thumbFinger?.toString() ?: "0")
    val pointerFinger = MutableLiveData(action?.pointerFinger?.toString() ?: "0")
    val middleFinger = MutableLiveData(action?.middleFinger?.toString() ?: "0")
    val ringFinger = MutableLiveData(action?.ringFinger?.toString() ?: "0")
    val littleFinger = MutableLiveData(action?.littleFinger?.toString() ?: "0")
    val delay = MutableLiveData(action?.delay?.toString() ?: "0")

    fun saveAction() {
        GestureRepository.saveAction(
            //todo generating name
            Action(
                id, name, false,
                thumbFinger.value?.toInt() ?: 0,
                pointerFinger.value?.toInt() ?: 0,
                middleFinger.value?.toInt() ?: 0,
                ringFinger.value?.toInt() ?: 0,
                littleFinger.value?.toInt() ?: 0,
                delay.value?.toInt() ?: 0
            ), position
        )
    }
}

class ActionViewModelFactory(private val action: Action?, private val pos: Int?) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ActionViewModel(action, pos) as T
    }
}