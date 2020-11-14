package com.handcontrol.ui.main.gestures

import com.handcontrol.base.BaseAdapterListener
import com.handcontrol.model.ExecutableItem

interface ExecutableItemListener : BaseAdapterListener {
    fun onClick(item: ExecutableItem)
    fun onPlay(item: ExecutableItem)
}