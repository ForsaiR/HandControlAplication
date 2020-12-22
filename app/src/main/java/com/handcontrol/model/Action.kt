package com.handcontrol.model

import com.handcontrol.server.protobuf.Gestures
import com.handcontrol.server.protobuf.Uuid

data class Action(
    override var id: Uuid.UUID?,
    override var name: String?,
    override var isExecuted: Boolean,
    var thumbFinger: Int,
    var pointerFinger: Int,
    var middleFinger: Int,
    var ringFinger: Int,
    var littleFinger: Int,
    var delay: Int
) : ExecutableItem(id, name, isExecuted) {
    constructor(action: Gestures.GestureAction) : this(
        null,
        null,
        false,
        action.thumbFingerPosition,
        action.pointerFingerPosition,
        action.middleFingerPosition,
        action.ringFingerPosition,
        action.littleFingerPosition,
        action.delay
    )

    fun getProtoModel(): Gestures.GestureAction =
        Gestures.GestureAction.newBuilder()
            .setThumbFingerPosition(thumbFinger)
            .setPointerFingerPosition(pointerFinger)
            .setMiddleFingerPosition(middleFinger)
            .setRingFingerPosition(ringFinger)
            .setLittleFingerPosition(littleFinger)
            .setDelay(delay)
            .build()
}