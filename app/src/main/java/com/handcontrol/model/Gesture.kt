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
    var i = 0

    constructor(gesture: Gestures.Gesture) : this(
        gesture.id.value.toInt(),
        gesture.name,
        false,
        false,
        gesture.repetitions,
        gesture.actionsList.map { Action(it) }.toMutableList()
    ) {
        actions.map { addActionName(it) }
    }

    fun getProtoModel(): Gestures.Gesture {
        return Gestures.Gesture.newBuilder()
            .setName(name)
            .setRepetitions(repeatCount ?: 0)
            .addAllActions(actions.map { it.getProtoModel() })
            .build()
    }

    private fun addActionName(action: Action): Action {
        action.name = "action$i"
        i++
        return action
    }

}