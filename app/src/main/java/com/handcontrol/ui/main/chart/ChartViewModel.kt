package com.handcontrol.ui.main.main.chart

import android.graphics.Color
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.handcontrol.api.Api
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

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
                val stream = api.getTelemetry()
                var time = 0f
                var lastTime = System.currentTimeMillis()
                for (data in stream) {
                    if (!isActive)
                        break
                    val currentTime = System.currentTimeMillis()
                    time += (currentTime - lastTime).toFloat() / 1000
                    lastTime = currentTime
                    lineData.value?.addEntry(
                        Entry(time, data.telemetry.emg.toFloat()),
                        0
                    )
                    lineData.postValue(lineData.value)
                    val executedGesture = gestures.find {
                        it.id == data.telemetry.executableGesture
                    }
                    currentGesture.postValue(executedGesture?.name ?: "")
                }
            }
        }
    }

    fun stop() {
        started = false
        background?.cancel()
    }
}