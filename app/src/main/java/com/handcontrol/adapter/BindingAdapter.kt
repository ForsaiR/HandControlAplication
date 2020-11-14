package com.handcontrol.adapter

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.LineData

@BindingAdapter("data", "interval")
fun addChartData(chart: LineChart, lineData: LineData, interval: Int) {
    chart.data = lineData
    val dataSet = lineData.getDataSetByIndex(0)
    val entryCount = dataSet.entryCount
    val entry = if (entryCount > 0)
        dataSet.getEntryForIndex(entryCount - 1)
    else
        null
    if (interval != 0 && entry != null) {
        chart.xAxis.axisMinimum = if (entry.x > interval) entry.x - interval else 0f
        chart.xAxis.axisMaximum = if (entry.x > interval) entry.x else interval.toFloat()
    } else {
        chart.xAxis.axisMinimum = 0f
        chart.xAxis.axisMaximum = entry?.x ?: 1f
    }
    chart.invalidate()
}

@BindingAdapter("android:text", "infinity")
fun TextView.setIntText(intText: Int, isInfinity: Boolean?) {
    text = if (isInfinity == true) "∞"
    else intText.toString()
}