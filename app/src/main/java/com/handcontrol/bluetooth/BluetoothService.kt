package com.handcontrol.bluetooth

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothSocket
import java.io.Closeable
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.*

class BluetoothService(private val macAddress: String) : Closeable {
    private var mBluetoothThread: BluetoothThread? = null
    @Volatile private var mState = State.DISCONNECTED
    val state get() = mState

    val mTelemetry = BaseObservable<Packet?>(null)
    val mReadPackets = ObservableList<Packet>()

    /**
     * start - метод реализующий запуск соединения с Bluetooth устройством
     */
    fun start() {
        mState = State.CONNECTING
        mBluetoothThread?.let {
            it.close()
            mBluetoothThread = null
        }
        mBluetoothThread = BluetoothThread(macAddress)
        mBluetoothThread?.start()   //TODO:Откуда взялся старт?
    }

    fun write(packet: Packet) {
        if (mState == State.CONNECTED) {
            mBluetoothThread?.write(ProtocolParser.packetToRaw(packet))
        }
    }

    /**
     * close - метод закрывающий Bluetooth соединение
     */
    override fun close() {
        closeConn()
        mState = State.DISCONNECTED
    }

    /**
     * connected - соединение установлено
     */
    private fun connected() {
        mState = State.CONNECTED
    }

    /**
     * disconnected - соединение прекращено
     */
    private fun disconnected() {
        mState = State.CONNECTED
    }

    /**
     * connectionError - ошибка соединения
     */
    private fun connectionError() {
        mState = State.FAIL
        closeConn()
    }

    /**
     * connectionLost - соединение потеряно
     */
    private fun connectionLost() {
        mState = State.LOST
        closeConn()
    }

    private fun readPackets(packets: LinkedList<Packet>) = packets.forEach {
        if (it.type == Packet.Type.TELEMETRY) {
            mTelemetry.value = it
        } else {
            mReadPackets.value.add(it)
            mReadPackets.notifyChanged()
        }
    }

    /**
     * closeConn - метод производящий закрытие Bluetooth соединения
     */
    private fun closeConn() {
        mBluetoothThread?.let {
            it.close()
            mBluetoothThread = null
        }
    }

    private inner class BluetoothThread(macAddress: String) : Thread(), Closeable {
        private val mmAdapter = BluetoothAdapter.getDefaultAdapter()

        @Volatile private var connected = false
        private var mmSocket: BluetoothSocket? = null
        private var mmInStream: InputStream? = null
        private var mmOutStream: OutputStream? = null

        private val mmParser by lazy { ProtocolParser() }

        init {
            mmAdapter.cancelDiscovery()
            val device = mmAdapter.getRemoteDevice(macAddress)
            try {
                mmSocket = device.createRfcommSocketToServiceRecord(SERVICE_UUID)
            } catch (e: IOException) {
                mmAdapter.startDiscovery()
                connectionError()
            }
        }

        //TODO: Вызывается где либо? Как Работает?
        override fun run() {
            mmSocket?.let { socket ->
                try {
                    socket.connect()
                    connected = true
                } catch (e: IOException) {
                    try {
                        socket.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                    connectionError()
                    return
                }
                connected()
            }
        }

        fun openStreams() {
            mmInStream = mmSocket?.inputStream
            mmOutStream = mmSocket?.outputStream

            mmInStream?.let { stream ->
                val buffer = ByteArray(1024)
                var bytes: Int
                while (connected) {
                    try {
                        bytes = stream.read(buffer)
                        val packets = mmParser.append(buffer, bytes)
                        readPackets(packets)
                    } catch (e: IOException) {
                        connectionLost()
                    }
                }
            }
        }

        fun write(data: ByteArray) {
            mmOutStream?.let { stream ->
                try {
                    stream.write(data)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }

        override fun close() {
            connected = false
            try {
                mmSocket?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
    }

    enum class State {
        CONNECTING,
        CONNECTED,
        DISCONNECTED,
        LOST,
        FAIL
    }

    companion object {
        private val SERVICE_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb")
    }
}