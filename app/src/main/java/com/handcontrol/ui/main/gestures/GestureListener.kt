package com.handcontrol.ui.main.gestures

import com.handcontrol.base.BaseRecyclerAdapter
import com.handcontrol.model.Gesture

interface GestureListener : BaseRecyclerAdapter.BaseAdapterListener {
    fun onClick(item: Gesture)
    fun onPlay(item: Gesture)
}