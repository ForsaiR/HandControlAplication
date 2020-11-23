package com.handcontrol.ui.main.gesturedetails

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.handcontrol.api.SaveGesture
import com.handcontrol.model.Gesture


class GestureDetailsViewModel(item: Gesture?, var isCreationMode: Boolean) : ViewModel() {
    val id = item?.id
    val name = MutableLiveData(item?.name ?: "")
    val repeatCount = MutableLiveData(item?.repeatCount?.toString() ?: "")
    val actions = MutableLiveData(item?.actions ?: mutableListOf())
    val isInfinity = MutableLiveData(item?.isInfinityRepeat ?: false)

    private val infinityObserver =
        Observer<Boolean> { v -> if (v) repeatCount.value = "∞" else repeatCount.value = "" }

    init {
        isInfinity.observeForever(infinityObserver)
    }

    fun saveGesture() {
        isCreationMode = false
        SaveGesture().invoke(
            Gesture(
                id,
                name.value!!,
                false,
                isInfinity.value!!,
                repeatCount.value!!.toIntOrNull(),
                actions.value!!
            )
        )
    }

    override fun onCleared() {
        isInfinity.removeObserver(infinityObserver)
        super.onCleared()
    }
}

class GestureDetailsViewModelFactory(
    private val gesture: Gesture?,
    private val isCreationMode: Boolean
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return GestureDetailsViewModel(gesture, isCreationMode) as T
    }
}
