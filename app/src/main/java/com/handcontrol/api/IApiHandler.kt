package com.handcontrol.api

import com.handcontrol.model.Action
import com.handcontrol.model.Gesture

interface IApiHandler {

    suspend fun getGestures(): MutableList<Gesture>

    suspend fun saveGesture(gesture: Gesture)

    suspend fun performGesture(gesture: Gesture)

    suspend fun deleteGesture(gestureId: Int)

    suspend fun setPositions(action: Action)
}