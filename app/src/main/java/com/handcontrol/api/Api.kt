package com.handcontrol.api

import android.content.Context
import java.lang.ref.WeakReference

object Api {
    private var prothesis: String = ""
    private var handlingType = HandlingType.BLUETOOTH
    private var bluetoothAddress: String? = null
    private var handler: IApiHandler? = null

    private lateinit var weakContext: WeakReference<Context>

    fun init(context: Context) {
        weakContext = WeakReference(context)
    }

    /**
     * setBluetoothAddress - фукнкция обеспечивающая установку адреса Bluetooth устройства
     */
    fun setBluetoothAddress(address: String) {
        bluetoothAddress = address
    }

    /**
     * setHandlingType - функция установки способа подключения к протезу
     */
    fun setHandlingType(handlingType: HandlingType) {
        this.handlingType = handlingType
    }

    /**
     * getApiHandler - функция получения интерфейса обработчика запросов к протезу
     */
    fun getApiHandler(): IApiHandler {
//        if (handler == null) {
//            handler = BluetoothHandler(bluetoothAddress!!)
//        }

        return handler!!
    }

    /**
     * setApiHandler - функция установки значения интерфейса обработчика запросов
     */
    fun setApiHandler(handler : IApiHandler) {
        this.handler = handler
    }

    /**
     * saveProthesis - функция устанавливающая значение uuid протеза
     */
    fun saveProthesis(prothesis: String) {
        this.prothesis = prothesis
    }
}