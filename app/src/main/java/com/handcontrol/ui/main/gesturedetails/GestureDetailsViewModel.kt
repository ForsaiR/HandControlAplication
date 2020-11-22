package com.handcontrol.ui.main.gesturedetails

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.handcontrol.model.Gesture


class GestureDetailsViewModel(item: Gesture?) : ViewModel() {
    val id = item?.id
    val name = MutableLiveData(item?.name ?: "")
    val repeatCount = MutableLiveData(
        if (item?.isInfinityRepeat == true) "∞"
        else item?.repeatCount?.toString() ?: ""
    )
    val actions = MutableLiveData(item?.actions ?: mutableListOf())
    val isInfinity = MutableLiveData<Boolean>(item?.isInfinityRepeat ?: false)

}

class GestureDetailsViewModelFactory(private val gesture: Gesture?) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return GestureDetailsViewModel(gesture) as T
    }
}
