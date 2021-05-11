package com.handcontrol.api

import com.handcontrol.bluetooth.*
import com.handcontrol.model.Action
import com.handcontrol.model.Gesture
import com.handcontrol.server.protobuf.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
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

    override suspend fun saveGesture(gesture: Gesture) {
        control()
        occupied = true
            bluetoothService.request(Packet(Packet.Type.SAVE_GESTURE, gesture.getProtoModel()
                .toByteArray().toList()))
        occupied = false
    }

    override suspend fun deleteGesture(gestureId: Uuid.UUID) {
        control()
        occupied = true
        bluetoothService.request(Packet(Packet.Type.DELETE_GESTURE, gestureId.toByteArray().toList()))
        occupied = false
    }

    override suspend fun performGestureId(gesture: Gesture) {
        control()
        occupied = true
            bluetoothService.request(Packet(Packet.Type.PERFORM_GESTURE_ID, gesture.getProtoModel()
                .toByteArray().toList()))
        occupied = false
    }

    override suspend fun performGestureRaw(gesture: Gesture) {
        control()
        occupied = true
            bluetoothService.request(Packet(Packet.Type.PERFORM_GESTURE_RAW, gesture.getProtoModel()
                .toByteArray().toList()))
        occupied = false
    }

    override suspend fun setPositions(action: Action) {
        control()
        occupied = true
            bluetoothService.request(Packet(Packet.Type.SET_POSITIONS, action.getProtoModel()
                .toByteArray().toList()))
        occupied = false
    }

    override suspend fun getTelemetry(): TelemetryOuterClass.Telemetry {
        control()
        occupied = true
        val resp = TelemetryOuterClass.Telemetry.parseFrom(bluetoothService.
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