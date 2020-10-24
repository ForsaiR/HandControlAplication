package com.handcontrol.ui.chart

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.handcontrol.api.Telemetry
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ChartViewModel : ViewModel() {
    private val telemetry by lazy { Telemetry() }
    private var time = 0f
    private var started = false
    private var background: Job? = null

    val lineData: MutableLiveData<LineData>

    init {
        val dataSet = LineDataSet(mutableListOf(), "telemetry")
        dataSet.setDrawCircles(false)
        dataSet.setDrawValues(false)
        lineData = MutableLiveData(LineData(dataSet))
    }

    fun start() {
        if (!started) {
            started = true
            time = 0f
            lineData.value?.getDataSetByIndex(0)?.clear()
            background = viewModelScope.launch {
                while (started) {
                    lineData.value?.addEntry(Entry(time, telemetry.getValue().toFloat()), 0)
                    lineData.postValue(lineData.value)
                    time += INTERVAL.toFloat() / 1000
                    delay(INTERVAL)
                }
            }
        }
    }

    fun stop() {
        started = false
        background?.cancel()
    }

    companion object {
        private const val INTERVAL = 200L
    }
}