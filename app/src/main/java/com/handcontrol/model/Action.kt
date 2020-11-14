package com.handcontrol.model

data class Action(
    override val id: Int,
    override val name: String,
    override var isExecuted: Boolean,
    val thumbFinger: Int,
    val pointerFinger: Int,
    val middleFinger: Int,
    val ringFinger: Int,
    val littleFinger: Int
) : ExecutableItem(id, name, isExecuted)