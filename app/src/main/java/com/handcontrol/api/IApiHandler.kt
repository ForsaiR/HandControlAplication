package com.handcontrol.api

import com.handcontrol.model.Gesture

interface IApiHandler {

    suspend fun getGestures(): MutableList<Gesture>

    suspend fun saveGesture(gesture: Gesture)

    suspend fun performGesture(gesture: Gesture)
}