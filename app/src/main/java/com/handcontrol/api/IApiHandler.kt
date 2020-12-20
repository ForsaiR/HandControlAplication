package com.handcontrol.api

import com.handcontrol.model.Action
import com.handcontrol.model.Gesture
import com.handcontrol.server.protobuf.Uuid

interface IApiHandler {

    suspend fun getGestures(): MutableList<Gesture>

    suspend fun saveGesture(gesture: Gesture)

    suspend fun performGesture(gesture: Gesture)

    suspend fun deleteGesture(gestureId: Uuid.UUID)

    suspend fun setPositions(action: Action)
}