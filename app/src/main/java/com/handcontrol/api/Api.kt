package com.handcontrol.api

import android.content.Context
import java.lang.ref.WeakReference

object Api {
    private const val PREFERENCES = "com.handcontrol.API_PREFERENCES"
    private const val KEY_TOKEN = "TOKEN"

    private var token: String? = null
    private var handlingType = HandlingType.GRPC

    private lateinit var weakContext: WeakReference<Context>

    fun init(context: Context) {
        weakContext = WeakReference(context)
        loadToken()
    }

    fun getApiHandler() : IApiHandler = when (handlingType) {
        HandlingType.GRPC -> GrpcHandler(weakContext.get(), token)
        HandlingType.BLUETOOTH -> BluetoothHandler()
    }

    fun getGrpcHandler() : GrpcHandler = GrpcHandler(weakContext.get(), token)

    fun setToken(token: String) {
        this.token = token
        saveToken()
    }

    fun clearToken() {
        token = null
        weakContext.get()?.run {
            val prefs = getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)
            with (prefs.edit()) {
                remove(KEY_TOKEN)
                apply()
            }
        }
    }

    fun isAuthorized(): Boolean = token != null

    fun setHandlingType(handlingType: HandlingType) { this.handlingType = handlingType }

    private fun saveToken() {
        weakContext.get()?.run {
            val prefs = getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)
            with (prefs.edit()) {
                putString(KEY_TOKEN, token)
                apply()
            }
        }
    }

    private fun loadToken() {
        weakContext.get()?.run {
            val prefs = getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)
            token = prefs.getString(KEY_TOKEN, null)
        }
    }
}