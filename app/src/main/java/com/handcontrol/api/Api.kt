package com.handcontrol.api

import android.content.Context
import java.lang.ref.WeakReference

object Api {
    private var prothesis: String = ""
    private var handlingType = HandlingType.BLUETOOTH
    private var bluetoothAddress: String? = null
    private var bluetoothHandler: BluetoothHandler? = null

    private lateinit var weakContext: WeakReference<Context>

    fun init(context: Context) {
        weakContext = WeakReference(context)
    }

    /**
     * getApiHandler - функция получения интерфейса обработчика запросов к протезу
     */
    fun getApiHandler(): IApiHandler {
        if (bluetoothHandler == null) {
            bluetoothHandler = BluetoothHandler(bluetoothAddress!!)
        }

        return bluetoothHandler!!
    }

    /**
     * setHandlingType - функция установки способа подключения к протезу
     */
    fun setHandlingType(handlingType: HandlingType) {
        this.handlingType = handlingType
    }

    /**
     * saveProthesis - функция устанавливающая значение uuid протеза
     */
    fun saveProthesis(prothesis: String) {
        this.prothesis = prothesis
    }

    /**
     * setBluetoothAddress - фукнкция обеспечивающая установку адреса Bluetooth устройства
     */
    fun setBluetoothAddress(address: String) {
        bluetoothAddress = address
    }
}