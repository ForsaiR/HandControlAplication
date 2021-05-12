package com.handcontrol.api

import com.handcontrol.model.Gesture
import com.handcontrol.server.protobuf.*
import java.util.*

interface IApiHandler {
    suspend fun getSettings(): Settings.GetSettings

    suspend fun setSettings(settings: Settings.SetSettings)

    suspend fun getGestures(): MutableList<Gesture>

    suspend fun saveGesture(saveGesture: Gestures.SaveGesture)

    suspend fun deleteGesture(deleteGesture: Gestures.DeleteGesture)

    suspend fun performGestureId(performGestureById: Gestures.PerformGestureById)

    suspend fun performGestureRaw(performGestureRaw: Gestures.PerformGestureRaw)

    suspend fun setPositions(setPositions: Gestures.SetPositions)

    suspend fun getTelemetry(): TelemetryOuterClass.GetTelemetry

    suspend fun startTelemetry(observer: Observer)

    suspend fun stopTelemetry()
}