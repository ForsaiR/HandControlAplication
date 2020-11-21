package com.handcontrol.model

data class Action(
    override val id: Int,
    override val name: String,
    override var isExecuted: Boolean,
    var thumbFinger: Int,
    var pointerFinger: Int,
    var middleFinger: Int,
    var ringFinger: Int,
    var littleFinger: Int
) : ExecutableItem(id, name, isExecuted)