package com.handcontrol.model

import com.handcontrol.server.protobuf.Gestures

data class Gesture(
    override val id: Int?,
    override val name: String,
    override var isExecuted: Boolean,
    val isInfinityRepeat: Boolean,
    val repeatCount: Int?,
    val actions: MutableList<Action>
) : ExecutableItem(id, name, isExecuted) {
    constructor(gesture: Gestures.Gesture) : this(
        gesture.id.value.toInt(),
        gesture.name,
        false,
        false,
        gesture.repetitions,
        gesture.actionsList.map { Action(it) }.toMutableList()
    )
}