package com.handcontrol.api

import com.handcontrol.model.Action
import com.handcontrol.model.Gesture
import com.handcontrol.server.protobuf.Settings
import com.handcontrol.server.protobuf.Stream

interface IApiHandler {

    suspend fun getGestures(): MutableList<Gesture>

    suspend fun saveGesture(gesture: Gesture)

    suspend fun performGesture(gesture: Gesture)

    suspend fun deleteGesture(gestureId: String)

    suspend fun setPositions(action: Action)

    suspend fun getSettings(): Settings.GetSettings

    suspend fun setSettings(settings: Settings.SetSettings)

    suspend fun getTelemetry(): Iterator<Stream.PubReply>
}