package com.handcontrol.ui.main.chart

import android.graphics.Color
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.handcontrol.api.Api
import com.handcontrol.bluetooth.HandlingException
import com.handcontrol.bluetooth.Packet
import com.handcontrol.server.protobuf.TelemetryOuterClass
import kotlinx.coroutines.*
import java.util.*

class ChartViewModel : ViewModel() {
    private val api by lazy { Api.getApiHandler() }
    private var started = false
    private var background: Job? = null

    val interval = MutableLiveData(0)
    val lineData: MutableLiveData<LineData>
    val currentGesture = MutableLiveData<String>()

    init {
        val dataSet = LineDataSet(mutableListOf(), "telemetry")
        dataSet.setDrawCircles(false)
        dataSet.setDrawValues(false)
        dataSet.color = Color.RED
        lineData = MutableLiveData(LineData(dataSet))
    }

    fun start() {
        if (!started) {
            started = true
            lineData.value?.getDataSetByIndex(0)?.clear()
            background = viewModelScope.launch(Dispatchers.IO) {
                val gestures = api.getGestures()
                var time = 0f
                var lastTime = System.currentTimeMillis()
                val observer = object : Observer {
                    override fun update(p0: Observable?, list: Any?) {
                        if (list is Packet) {
                            if (list.type == Packet.Type.TELEMETRY) {
                                if (isActive) {
                                    val currentTime = System.currentTimeMillis()
                                    time += (currentTime - lastTime).toFloat() / 1000
                                    lastTime = currentTime
                                    val telemetry = TelemetryOuterClass.Telemetry.parseFrom(list.
                                        payload.toByteArray())
                                    lineData.value?.addEntry(
                                        Entry(time, telemetry.emg.toFloat()),
                                        0
                                    )
                                    lineData.postValue(lineData.value)
                                    val executedGesture = gestures.find {
                                        it.id == telemetry.executableGesture
                                    }
                                    currentGesture.postValue(executedGesture?.name ?: "")
                                }
                            }
                        }
                    }
                }
                api.startTelemetry(observer)
            }
        }
    }

    fun stop() {
        started = false
        background?.cancel()
        runBlocking {
            api.stopTelemetry()
        }
    }
}