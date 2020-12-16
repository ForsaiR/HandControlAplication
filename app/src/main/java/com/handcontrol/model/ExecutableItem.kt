package com.handcontrol.model

import java.io.Serializable

open class ExecutableItem(
    open val id: String?,
    open val name: String?,
    open var isExecuted: Boolean
) : Serializable