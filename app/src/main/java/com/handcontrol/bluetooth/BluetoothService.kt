package com.handcontrol.bluetooth

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothSocket
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import java.io.Closeable
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.*

class BluetoothService(private val macAddress: String) : Closeable {
    companion object {
        private val SERVICE_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb")
    }

    private var mBluetoothThread: BluetoothThread? = null
    @Volatile private var mState = State.DISCONNECTED

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
        mBluetoothThread = BluetoothThread(macAddress).apply { start() }
    }

    /**
     * isConnected - метод отображения состояния подключения
     */
    fun isConnected() = mBluetoothThread?.isConnected()

    /**
     * close - метод закрывающий Bluetooth соединение
     */
    override fun close() {
        mBluetoothThread?.let {
            it.close()
            mBluetoothThread = null
        }
        mState = State.DISCONNECTED
    }

    /**
     * request - запрос к устройству
     */
    @Synchronized
    suspend fun request(request: Packet): Packet {
        if (mBluetoothThread?.isConnected() == true) {
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
            mReadPackets.addObserver(observer)
            write(request)
            response?.let { return it }
            var attempt = 0
            while (attempt < 50)
            {
                delay(200)
                response?.let { return it }
                if (mState == State.DISCONNECTED)
                    throw DisconnectedException()
                attempt += 1
            }
            throw TimeoutException()
        }

        throw ConnectingFailedException()
    }

    /**
     * write - метод отправки сообщения типа Packet протезу по Bluetooth
     */
    private fun write(packet: Packet) {
        if (mState == State.CONNECTED) {
            mBluetoothThread?.write(ProtocolParser.packetToRaw(packet))
        }
    }

    //TODO: Переделать
    private fun readPackets(packets: LinkedList<Packet>) = packets.forEach {
        if (it.type == Packet.Type.TELEMETRY) {
            mTelemetry.value = it
        } else {
            mReadPackets.value.add(it)
            mReadPackets.notifyChanged()
        }
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
        close()
    }

    /**
     * connectionLost - соединение потеряно
     */
    private fun connectionLost() {
        mState = State.LOST
        close()
    }

    /**
     * BluetoothThread - класс обеспечивающий чтение/записть по Bluetooth соединению
     */
    private inner class BluetoothThread(macAddress: String) : Thread(), Closeable {
        private val mmAdapter = BluetoothAdapter.getDefaultAdapter()

        @Volatile private var connected = false
        private var mmSocket: BluetoothSocket? = null
        private var mmInStream: InputStream? = null
        private var mmOutStream: OutputStream? = null

        private val mmParser by lazy { ProtocolParser() }

        /**
         * init - прекращаем процесс обнаружения устройства и создаем RfcommSocket с устройством
         * SERVICE_UUID
         */
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

        /**
         * run - при зупуске труда открываем соединение и запускаем обнаружение Streams чтения и
         * записи
         */
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
                runBlocking {
                    openStreams()
                }
            }
        }

        /**
         * openStreams - открываем стримы чтения и записи. Запускием цикл чтения по 1024 байта из
         * стрима и расшифровываем полученное сообщение
         */
        private fun openStreams() {
            mmInStream = mmSocket?.inputStream
            mmOutStream = mmSocket?.outputStream

            connected()

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

        /**
         * write - отправка данных по RfcommSocket
         */
        @Synchronized
        fun write(data: ByteArray) {
            mmOutStream?.let { stream ->
                try {
                    stream.write(data)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }

        fun isConnected(): Boolean {
            return connected
        }

        /**
         * close - закрыте RfcommSocket
         */
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
}