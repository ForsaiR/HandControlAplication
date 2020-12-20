package com.handcontrol.api

import android.content.Context
import java.lang.ref.WeakReference

object Api {
    private const val PREFERENCES = "com.handcontrol.API_PREFERENCES"
    private const val KEY_TOKEN = "TOKEN"
    private const val KEY_PROTHESIS = "ONLINE_PROTHESIS"
    private const val KEY_PROTOS = "ALL_ONLINE_PROTHESIS"


    private var token: String? = null
    private var prothesis: String? = null
    private var protos: String? = null
    private var handlingType = HandlingType.GRPC

    private lateinit var weakContext: WeakReference<Context>

    fun init(context: Context) {
        weakContext = WeakReference(context)
        loadToken()
    }

    fun getApiHandler(): IApiHandler = when (handlingType) {
        HandlingType.GRPC -> GrpcHandler(weakContext.get(), token)
        HandlingType.BLUETOOTH -> BluetoothHandler()
    }

    fun getGrpcHandler(): GrpcHandler = GrpcHandler(weakContext.get(), token)

    fun setToken(token: String) {
        this.token = token
        saveToken()
    }

    fun clearToken() {
        token = null
        weakContext.get()?.run {
            val prefs = getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)
            with(prefs.edit()) {
                remove(KEY_TOKEN)
                apply()
            }
        }
    }

    fun clearProthesis() {
        prothesis = null
        weakContext.get()?.run {
            val prefs = getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)
            with(prefs.edit()) {
                remove(KEY_PROTHESIS)
                apply()
            }
        }
    }

    fun clearProtos() {
        prothesis = null
        weakContext.get()?.run {
            val prefs = getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)
            with(prefs.edit()) {
                remove(KEY_PROTOS)
                apply()
            }
        }
    }

    fun isAuthorized(): Boolean = token != null

    fun isRegistrated(): Boolean = token != null

    fun setHandlingType(handlingType: HandlingType) {
        this.handlingType = handlingType
    }

    private fun saveToken() {
        weakContext.get()?.run {
            val prefs = getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)
            with(prefs.edit()) {
                putString(KEY_TOKEN, token)
                apply()
            }
        }
    }

    //сохранение uuid протеза
    fun saveProthesis(prothesis: String) {
        weakContext.get()?.run {
            val prefs = getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)
            with(prefs.edit()) {
                putString(KEY_PROTHESIS, prothesis)
                apply()
            }
        }
    }

    //сохранение протезв
    fun saveProtos(prothesis: String) {
        weakContext.get()?.run {
            val prefs = getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)
            with(prefs.edit()) {
                putString(KEY_PROTOS, prothesis)
                println("Save in " + prothesis)
                apply()
            }
        }
    }

    fun getProthesis(): String? {
        return weakContext.get()?.run {
            getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)
                .getString(KEY_PROTHESIS, "000")
        }
    }

    fun getProtos(): String? {
        weakContext.get()?.run {
            val prefs = getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)
            protos = prefs.getString(KEY_PROTOS, null)
            println("Getting: "+protos)
        }
        return protos;
    }

    fun loadToken(): String? {
        weakContext.get()?.run {
            val prefs = getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)
            token = prefs.getString(KEY_TOKEN, null)
        }
        return token;
    }

}