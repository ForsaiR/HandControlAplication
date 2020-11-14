package com.handcontrol.ui.main.gestures.gesturedetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.handcontrol.model.Gesture


class GestureDetailsViewModel(item: Gesture) : ViewModel() {
    val name = item.name
    val repeatCount = item.repeatCount
    val actions = item.actions
    val isInfinity = item.isInfinityRepeat

}

class GestureDetailsViewModelFactory(private val gesture: Gesture) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return GestureDetailsViewModel(gesture) as T
    }
}
