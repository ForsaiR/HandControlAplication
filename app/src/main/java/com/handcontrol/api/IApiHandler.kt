package com.handcontrol.api

import com.handcontrol.model.Action
import com.handcontrol.model.Gesture
import com.handcontrol.server.protobuf.*
import java.util.*

interface IApiHandler {
    suspend fun getSettings(): Settings.GetSettings

    suspend fun setSettings(settings: Settings.SetSettings)

    suspend fun getGestures(): MutableList<Gesture>

    suspend fun saveGesture(gesture: Gesture)

    suspend fun deleteGesture(gestureId: Uuid.UUID)

    suspend fun performGestureId(gesture: Gesture)

    suspend fun performGestureRaw(gesture: Gesture)

    suspend fun setPositions(action: Action)

    suspend fun getTelemetry(): TelemetryOuterClass.Telemetry

    suspend fun startTelemetry(observer: Observer)

    suspend fun stopTelemetry()
}