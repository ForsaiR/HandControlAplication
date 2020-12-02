package com.handcontrol.api

import com.handcontrol.server.protobuf.Gestures

interface IApiHandler {
    suspend fun getGestures(): MutableList<Gestures.Gesture>
}