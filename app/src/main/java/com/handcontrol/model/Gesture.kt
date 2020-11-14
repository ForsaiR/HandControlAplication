package com.handcontrol.model

import java.io.Serializable

data class Gesture(
    val id: Int,
    val name: String,
    val isExecuted: Boolean,
    val isInfinityRepeat: Boolean,
    val repeatCount: Int,
    val actions: MutableList<Action>
) : Serializable