package com.handcontrol.model

import java.io.Serializable

open class ExecutableItem(
    open val id: Int,
    open val name: String,
    open val isExecuted: Boolean
) : Serializable