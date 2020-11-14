package com.handcontrol.api

import com.handcontrol.model.Gesture

class GetGestures {
    //todo get from api: {id}/data/gestures
    operator fun invoke(): MutableList<Gesture> =
        mutableListOf(
            Gesture(
                1, "gesture1", false, false, 2, mutableListOf()
            ),
            Gesture(
                2, "gesture2", false, false, 2, mutableListOf()
            )
        )
}