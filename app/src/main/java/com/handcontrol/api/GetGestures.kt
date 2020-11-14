package com.handcontrol.api

import com.handcontrol.model.Action
import com.handcontrol.model.Gesture

class GetGestures {
    //todo get from api: {id}/data/gestures
    operator fun invoke(): MutableList<Gesture> =
        mutableListOf(
            Gesture(
                1, "gesture1", false, false, 2, mutableListOf(
                    Action(1, "action1", false, 100, 100, 100, 100, 100),
                    Action(2, "action2", false, 100, 100, 100, 100, 100),
                    Action(3, "action3", false, 100, 100, 100, 100, 100),
                    Action(4, "action4", false, 100, 100, 100, 100, 100)
                )
            ),
            Gesture(
                2, "gesture2", false, false, 2, mutableListOf(
                    Action(777, "action0", false, 100, 100, 100, 100, 100),
                )
            )
        )
}