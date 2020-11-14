package com.handcontrol.model

data class Gesture(
    override val id: Int,
    override val name: String,
    override var isExecuted: Boolean,
    val isInfinityRepeat: Boolean,
    val repeatCount: Int?,
    val actions: MutableList<Action>
) : ExecutableItem(id, name, isExecuted)