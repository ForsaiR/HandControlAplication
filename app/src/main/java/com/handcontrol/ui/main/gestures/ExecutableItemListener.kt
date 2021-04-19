package com.handcontrol.ui.main.main.gestures

import com.handcontrol.base.BaseAdapterListener
import com.handcontrol.model.ExecutableItem

interface ExecutableItemListener : BaseAdapterListener {
    fun onClick(item: ExecutableItem, position: Int)
    fun onPlay(item: ExecutableItem, position: Int)
}