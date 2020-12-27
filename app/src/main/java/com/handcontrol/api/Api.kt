package com.handcontrol.api

import android.content.Context
import java.lang.ref.WeakReference

object Api {
    private var token: String? = null
    private var prothesis: String = ""
    private var handlingType = HandlingType.GRPC
    private var bluetoothAddress: String? = null
    private var bluetoothHandler: BluetoothHandler? = null

    private lateinit var weakContext: WeakReference<Context>

    fun init(context: Context) {
        weakContext = WeakReference(context)
    }

    fun getApiHandler(): IApiHandler = when (handlingType) {
        HandlingType.GRPC -> GrpcHandler(weakContext.get(), token, prothesis)
        HandlingType.BLUETOOTH -> {
            if (bluetoothAddress == null)
                throw IllegalStateException("Bluetooth's MAC address haven't been set")
            if (bluetoothHandler == null)
                bluetoothHandler = BluetoothHandler(bluetoothAddress!!)
            bluetoothHandler!!
        }
    }

    fun getGrpcHandler(): GrpcHandler = GrpcHandler(weakContext.get(), token, prothesis)

    fun setToken(token: String) {
        this.token = token
    }

    fun setHandlingType(handlingType: HandlingType) {
        this.handlingType = handlingType
    }

    //сохранение uuid протеза
    fun saveProthesis(prothesis: String) {
        this.prothesis = prothesis
    }

    fun setBluetoothAddress(address: String) {
        bluetoothAddress = address
    }
}