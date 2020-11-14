package com.handcontrol.ui.main.gestures

import com.handcontrol.base.BaseAdapterListener
import com.handcontrol.model.Gesture

interface GestureListener : BaseAdapterListener {
    fun onClick(item: Gesture)
    fun onPlay(item: Gesture)
}