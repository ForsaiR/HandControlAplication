package com.handcontrol.api

import com.handcontrol.model.Action
import com.handcontrol.model.Gesture
import com.handcontrol.server.protobuf.Settings
import com.handcontrol.server.protobuf.Stream
import com.handcontrol.server.protobuf.Uuid

interface IApiHandler {

    suspend fun getTelemetry(): Iterator<Stream.PubReply>

    suspend fun getSettings(): Settings.GetSettings

    suspend fun setSettings(settings: Settings.SetSettings)

    suspend fun getGestures(): MutableList<Gesture>

    suspend fun saveGesture(gesture: Gesture)

    suspend fun deleteGesture(gestureId: Uuid.UUID)

    suspend fun performGestureId(gesture: Gesture)

    suspend fun performGestureRaw(gesture: Gesture)

    suspend fun setPositions(action: Action)
}