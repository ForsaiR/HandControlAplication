package com.handcontrol.api

import com.handcontrol.server.protobuf.Gestures

class BluetoothHandler : IApiHandler {
    override suspend fun getGestures(): MutableList<Gestures.Gesture> {
        TODO("Not yet implemented")
    }
}