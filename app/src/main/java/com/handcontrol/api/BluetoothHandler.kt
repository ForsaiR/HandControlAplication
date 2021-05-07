package com.handcontrol.api

import com.handcontrol.bluetooth.*
import com.handcontrol.model.Action
import com.handcontrol.model.Gesture
import com.handcontrol.server.protobuf.Gestures
import com.handcontrol.server.protobuf.Settings
import com.handcontrol.server.protobuf.Stream
import com.handcontrol.server.protobuf.Uuid
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.util.*

/**
 * BluetoothHandler - класс обработчика Bluetooth соединения
 */
class BluetoothHandler(private val macAddress: String) : IApiHandler {
    private val bluetoothService =  BluetoothService(macAddress).apply { start() }

    /**
     * isConnected - функция подтверждения соединения
     */
    fun isConnected() : Boolean {
        if (bluetoothService.state == BluetoothService.State.CONNECTED) {
            return true
        }

        return false
    }

    fun close() = bluetoothService.close()

    //TODO: переместить в BluetoothService
    private suspend fun request(request: Packet): Packet {
        if (isConnected()) {
            var response: Packet? = null
            val observer = object : Observer {
                override fun update(p0: Observable?, list: Any?) {
                    if (list is MutableList<*>) {
                        list.forEach {
                            if (it is Packet) {
                                if (it.type == request.type) {
                                    response = it
                                    list.remove(it)
                                    return
                                } else if (it.type == Packet.Type.ERR) {
                                    list.remove(it)
                                    throw HandlingException()
                                }
                            }
                        }
                    }
                }
            }
            bluetoothService.mReadPackets.addObserver(observer)
            bluetoothService.write(request)
            response?.let { return it }
            repeat(25) {
                delay(200)
                response?.let { return it }
                if (bluetoothService.state == BluetoothService.State.DISCONNECTED)
                    throw DisconnectedException()
            }
            throw TimeoutException()
        }

        throw ConnectingFailedException()
    }

    //Самая страшная хрень, непонятно как ее реализовывать
    override suspend fun getTelemetry(): Iterator<Stream.PubReply> {
        TODO("Not yet implemented")
    }

    override suspend fun getSettings(): Settings.GetSettings {
        return withContext(Dispatchers.IO) {
            Settings.GetSettings.parseFrom(request(Packet(Packet.Type.GET_SETTINGS, emptyList())).
            payload.toByteArray())
        }
    }

    override suspend fun setSettings(settings: Settings.SetSettings) {
        request(Packet(Packet.Type.SET_SETTINGS, settings.toByteArray().toList()))
    }

    override suspend fun getGestures(): MutableList<Gesture> {
        return withContext(Dispatchers.IO) {
            val gestures: MutableList<Gesture> = mutableListOf<Gesture>()

            for (gesture in Gestures.GetGestures.parseFrom(
                request(
                    Packet(
                        Packet.Type.GET_GESTURES,
                        emptyList()))
                    .payload.toByteArray())
                .gesturesList) {
                gestures.add(Gesture(gesture))
            }

            gestures
        }
    }

    override suspend fun saveGesture(gesture: Gesture) {
        return withContext(Dispatchers.IO) {
            request(Packet(Packet.Type.SAVE_GESTURE, gesture.getProtoModel().toByteArray().toList()))
        }
    }

    //TODO: Скорее всего не правильно реалзован
    override suspend fun deleteGesture(gestureId: Uuid.UUID) {
        request(Packet(Packet.Type.DELETE_GESTURE, gestureId.toByteArray().toList()))
    }

    //TODO: Скорее всего не правильно реалзован
    override suspend fun performGestureId(gesture: Gesture) {
        return withContext(Dispatchers.IO) {
            request(Packet(Packet.Type.PERFORM_GESTURE_ID, gesture.getProtoModel().toByteArray().toList()))
        }
    }

    //TODO: Скорее всего не правильно реалзован
    override suspend fun performGestureRaw(gesture: Gesture) {
        return withContext(Dispatchers.IO) {
            request(Packet(Packet.Type.PERFORM_GESTURE_RAW, gesture.getProtoModel().toByteArray().toList()))
        }
    }

    override suspend fun setPositions(action: Action) {
        return withContext(Dispatchers.IO) {
            request(Packet(Packet.Type.SET_POSITIONS, action.getProtoModel().toByteArray().toList()))
        }
    }
}