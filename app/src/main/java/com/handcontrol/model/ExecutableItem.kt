package com.handcontrol.model

import com.handcontrol.server.protobuf.Uuid
import java.io.Serializable

open class ExecutableItem(
    open val id: Uuid.UUID?,
    open val name: String?,
    open var isExecuted: Boolean
) : Serializable