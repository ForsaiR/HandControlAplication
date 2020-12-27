package com.handcontrol.bluetooth

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothSocket
import java.io.Closeable
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.*

class BluetoothService(
    private val macAddress: String
) : Closeable {
    private var mConnectingThread: ConnectingThread? = null
    private var mConnectedThread: ConnectedThread? = null
    @Volatile private var mState = State.DISCONNECTED
    val state
        get() = mState

    val mTelemetry = BaseObservable<Packet?>(null)
    val mReadPackets = ObservableList<Packet>()

    fun start() {
        mState = State.CONNECTING
        mConnectingThread?.let {
            it.close()
            mConnectingThread = null
        }
        mConnectedThread?.let {
            it.close()
            mConnectedThread = null
        }
        mConnectingThread = ConnectingThread(macAddress)
        mConnectingThread?.start()
    }

    private fun connected(socket: BluetoothSocket) {
        mConnectingThread?.let {
            it.close()
            mConnectingThread = null
        }
        mConnectedThread = ConnectedThread(socket)
        mConnectedThread?.start()
        mState = State.CONNECTED
    }

    private fun connectionError() {
        mState = State.FAIL
        close()
    }

    private fun connectionLost() {
        mState = State.FAIL
        close()
    }

    private fun readPackets(packets: LinkedList<Packet>) = packets.forEach {
        if (it.type == Packet.Type.TELEMETRY) {
            mTelemetry.value = it
        } else {
            mReadPackets.value.add(it)
            mReadPackets.notifyChanged()
        }
    }

    override fun close() {
        mState = State.DISCONNECTED
        mConnectingThread?.let {
            it.close()
            mConnectingThread = null
        }
        mConnectedThread?.let {
            it.close()
            mConnectedThread = null
        }
    }

    fun write(packet: Packet) {
        if (mState == State.CONNECTED) {
            mConnectedThread?.write(ProtocolParser.packetToRaw(packet))
        }
    }

    private inner class ConnectingThread(macAddress: String) : Thread(), Closeable {
        private val mmAdapter = BluetoothAdapter.getDefaultAdapter()
        private var mmSocket: BluetoothSocket? = null

        init {
            val device = mmAdapter.getRemoteDevice(macAddress)
            try {
                mmSocket = device.createRfcommSocketToServiceRecord(SERVICE_UUID)
            } catch (e: IOException) {
                connectionError()
            }
        }

        override fun run() {
            mmSocket?.let { socket ->
                mmAdapter.cancelDiscovery()
                try {
                    socket.connect()
                } catch (e: IOException) {
                    try {
                        socket.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                    connectionError()
                    return
                }
                connected(socket)
            }
        }

        override fun close() {
            try {
                mmSocket?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private inner class ConnectedThread(
        private val mmSocket: BluetoothSocket
    ) : Thread(), Closeable {
        private var mmInStream: InputStream? = null
        private var mmOutStream: OutputStream? = null

        private val mmParser by lazy { ProtocolParser() }

        @Volatile private var connected = true

        init {
            try {
                mmInStream = mmSocket.inputStream
                mmOutStream = mmSocket.outputStream
            } catch (e: IOException) {
                connectionError()
            }
        }

        override fun run() {
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
                mmSocket.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    enum class State {
        CONNECTING,
        CONNECTED,
        DISCONNECTED,
        FAIL
    }

    companion object {
        private val SERVICE_UUID = UUID.fromString("c1366517-2680-4c8b-9153-918c8584b45a")
    }
}