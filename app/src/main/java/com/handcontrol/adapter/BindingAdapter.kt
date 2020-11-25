package com.handcontrol.adapter

import androidx.core.widget.doOnTextChanged
import androidx.databinding.BindingAdapter
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.LineData
import com.google.android.material.textfield.TextInputLayout

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

@BindingAdapter("rangeStart", "rangeEnd")
fun TextInputLayout.rangeError(start: Int, end: Int) {
    editText?.doOnTextChanged { _, _, _, _ ->
        with(editText?.text.toString().toIntOrNull()) {
            error = if (this == null || this < start || this > end) "$start-$end" else null
        }
    }
}
