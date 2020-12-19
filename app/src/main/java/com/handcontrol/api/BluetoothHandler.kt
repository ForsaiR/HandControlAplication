package com.handcontrol.api

import com.handcontrol.model.Action
import com.handcontrol.model.Gesture
import com.handcontrol.server.protobuf.Settings
import com.handcontrol.server.protobuf.Stream

class BluetoothHandler : IApiHandler {
    override suspend fun getGestures(): MutableList<Gesture> {
        TODO("Not yet implemented")
    }

    override suspend fun saveGesture(gesture: Gesture) {
        TODO("Not yet implemented")
    }

    override suspend fun performGesture(gesture: Gesture) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteGesture(gestureId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun setPositions(action: Action) {
        TODO("Not yet implemented")
    }

    override suspend fun getSettings(): Settings.GetSettings {
        TODO("Not yet implemented")
    }

    override suspend fun setSettings(settings: Settings.SetSettings) {
        TODO("Not yet implemented")
    }

    override suspend fun getTelemetry(): Iterator<Stream.PubReply> {
        TODO("Not yet implemented")
    }
}