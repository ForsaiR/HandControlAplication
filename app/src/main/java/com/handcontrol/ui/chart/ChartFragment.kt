package com.handcontrol.ui.chart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.handcontrol.R
import com.handcontrol.databinding.FragmentChartBinding

class ChartFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_chart, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentChartBinding.bind(view)
        binding.lifecycleOwner = this
        val viewModel: ChartViewModel by viewModels()
        val chart = binding.chart
        initChart(chart)
        binding.viewModel = viewModel
    }

    private fun initChart(chart: LineChart) {
        chart.apply {
            setTouchEnabled(false)
            description.isEnabled = false
            setDrawBorders(true)
            axisLeft.axisMinimum = 0f
            axisRight.isEnabled = false
            xAxis.position = XAxis.XAxisPosition.BOTTOM
        }
    }
}