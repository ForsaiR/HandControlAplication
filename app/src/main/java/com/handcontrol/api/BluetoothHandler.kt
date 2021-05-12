package com.handcontrol.api

import com.handcontrol.bluetooth.*
import com.handcontrol.model.Gesture
import com.handcontrol.server.protobuf.*
import kotlinx.coroutines.delay
import java.util.*

/**
 * BluetoothHandler - класс обработчика Bluetooth соединения
 */
class BluetoothHandler(btService: BluetoothService) : IApiHandler {
    private val bluetoothService =  btService
    private var occupied = false

    fun close() = bluetoothService.close()

    private suspend fun control() {
        if (occupied) {
            var attempt = 0
            while(attempt < 200) {
                delay(25)
                if (!occupied) {
                    return
                }
                attempt += 1
            }
            throw TimeoutException()
        }
    }

    override suspend fun getSettings(): Settings.GetSettings {
        control()
        occupied = true
        val resp = Settings.GetSettings.parseFrom(bluetoothService.
        request(Packet(Packet.Type.GET_SETTINGS, emptyList())).payload.toByteArray())
        occupied = false
        return resp
    }

    override suspend fun setSettings(settings: Settings.SetSettings) {
        control()
        occupied = true
        bluetoothService.request(Packet(Packet.Type.SET_SETTINGS, settings.toByteArray().toList()))
        occupied = false
    }

    override suspend fun getGestures(): MutableList<Gesture> {
        control()
        occupied = true
        val gestures: MutableList<Gesture> = mutableListOf<Gesture>()

        for (gesture in Gestures.GetGestures.parseFrom(
            bluetoothService.request(
                Packet(
                    Packet.Type.GET_GESTURES,
                    emptyList()))
                .payload.toByteArray())
            .gesturesList) {
            gestures.add(Gesture(gesture))
        }
        occupied = false
        return gestures
    }

    override suspend fun saveGesture(saveGesture: Gestures.SaveGesture) {
        control()
        occupied = true
            bluetoothService.request(Packet(Packet.Type.SAVE_GESTURE, saveGesture.toByteArray().toList()))
        occupied = false
    }

    override suspend fun deleteGesture(deleteGesture: Gestures.DeleteGesture) {
        control()
        occupied = true
        bluetoothService.request(Packet(Packet.Type.DELETE_GESTURE, deleteGesture.toByteArray()
            .toList()))
        occupied = false
    }

    override suspend fun performGestureId(performGestureById: Gestures.PerformGestureById) {
        control()
        occupied = true
            bluetoothService.request(Packet(Packet.Type.PERFORM_GESTURE_ID, performGestureById
                .toByteArray()
                .toList()))
        occupied = false
    }

    override suspend fun performGestureRaw(performGestureRaw: Gestures.PerformGestureRaw) {
        control()
        occupied = true
            bluetoothService.request(Packet(Packet.Type.PERFORM_GESTURE_RAW, performGestureRaw
                .toByteArray()
                .toList()))
        occupied = false
    }

    override suspend fun setPositions(setPositions: Gestures.SetPositions) {
        control()
        occupied = true
            bluetoothService.request(Packet(Packet.Type.SET_POSITIONS, setPositions
                .toByteArray()
                .toList()))
        occupied = false
    }

    override suspend fun getTelemetry(): TelemetryOuterClass.GetTelemetry {
        control()
        occupied = true
        val resp = TelemetryOuterClass.GetTelemetry.parseFrom(bluetoothService.
        request(Packet(Packet.Type.GET_TELEMETRY,emptyList())).payload.toByteArray())
        occupied = false
        return resp
    }

    override suspend fun startTelemetry(observer: Observer) {
        control()
        occupied = true
        bluetoothService.stream(Packet(Packet.Type.START_TELEMETRY,emptyList()),observer)
        occupied = false
    }

    override suspend fun stopTelemetry() {
        control()
        occupied = true
        bluetoothService.request(Packet(Packet.Type.STOP_TELEMETRY,emptyList()))
        occupied = false
    }
}