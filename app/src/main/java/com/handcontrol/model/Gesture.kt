package com.handcontrol.model

import com.handcontrol.server.protobuf.Gestures
import com.handcontrol.server.protobuf.Uuid

data class Gesture(
    override val id: Uuid.UUID?,
    override val name: String,
    override var isExecuted: Boolean,
    val isInfinityRepeat: Boolean,
    val repeatCount: Int?,
    val actions: MutableList<Action>,
    val englishName: String = name
) : ExecutableItem(id, name, isExecuted) {
    var i = 0

    constructor(gesture: Gestures.Gesture) : this(
        gesture.id,
        gesture.name,
        false,
        gesture.iterable,
        gesture.repetitions,
        gesture.actionsList.map { Action(it) }.toMutableList()
    ) {
        actions.map { addActionName(it) }
    }

    fun getProtoModel(): Gestures.Gesture {
        val gesture = Gestures.Gesture.newBuilder()
            .setName(name)
            .setRepetitions(repeatCount ?: 0)
            .setIterable(isInfinityRepeat)
            .addAllActions(actions.map { it.getProtoModel() })

        id?.let {
            gesture.setId(id)
        }

        return gesture.build()
    }

    private fun addActionName(action: Action): Action {
        action.name = "action$i"
        i++
        return action
    }

}