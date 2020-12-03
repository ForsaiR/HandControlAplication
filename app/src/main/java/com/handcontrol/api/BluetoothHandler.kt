package com.handcontrol.api

import com.handcontrol.model.Gesture

class BluetoothHandler : IApiHandler {
    override suspend fun getGestures(): MutableList<Gesture> {
        TODO("Not yet implemented")
    }

    override suspend fun saveGesture(gesture: Gesture) {
        TODO("Not yet implemented")
    }
}