package com.handcontrol.ui.main.gestures.gesturedetails

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.handcontrol.model.Gesture


class GestureDetailsViewModel(item: Gesture?) : ViewModel() {
    val name = MutableLiveData(item?.name ?: "")
    val repeatCount = MutableLiveData(item?.repeatCount ?: 0)
    val actions = MutableLiveData(item?.actions ?: mutableListOf())
    val isInfinity = MutableLiveData<Boolean>(item?.isInfinityRepeat ?: false)

}

class GestureDetailsViewModelFactory(private val gesture: Gesture?) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return GestureDetailsViewModel(gesture) as T
    }
}
