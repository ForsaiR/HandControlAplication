package com.handcontrol.adapter

import android.text.SpannableStringBuilder
import android.widget.EditText
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.LineData
import java.lang.Integer.parseInt

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
fun TextView.setInfinitySymbolOrIntText(intText: Int, isInfinity: Boolean?) {
    text = if (isInfinity == true) "∞"
    else intText.toString()
}

@BindingAdapter("intText")
fun TextView.setIntText(intText: Int) {
    text = intText.toString()
}

@BindingAdapter("android:text")
fun EditText.setIntText(intText: Int) {
    text = SpannableStringBuilder(intText.toString())
}

@InverseBindingAdapter(attribute = "android:text")
fun EditText.getIntText(): Int {
    return parseInt(text.toString())
}

//
//@BindingAdapter("android:enable")
//fun View.setValueEnable( isEnabled: Boolean?) {
//    isEnabled?.let { setEnabled(isEnabled)}
//}
//
//@InverseBindingAdapter(attribute = "android:enable")
//fun View.getValueEnable(): Boolean = isEnabled
