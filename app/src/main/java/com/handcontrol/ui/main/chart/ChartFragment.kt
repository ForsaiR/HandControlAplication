package com.handcontrol.ui.main.chart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.handcontrol.R
import com.handcontrol.databinding.FragmentChartBinding

class ChartFragment : Fragment() {
    private var hideMenuItem = true

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
        setHasOptionsMenu(true)
    }

    override fun onStart() {
        super.onStart()
        hideMenuItem = true
        activity?.invalidateOptionsMenu()
    }

    override fun onStop() {
        super.onStop()
        hideMenuItem = false
        activity?.invalidateOptionsMenu()
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        val item = menu.findItem(R.id.navigation_chart)
        item?.isEnabled = !hideMenuItem
    }

    private fun initChart(chart: LineChart) {
        chart.apply {
            setTouchEnabled(false)
            description.isEnabled = false
            legend.isEnabled = false
            setDrawBorders(true)
            axisLeft.axisMinimum = 0f
            axisRight.isEnabled = false
            xAxis.position = XAxis.XAxisPosition.BOTTOM
        }
    }
}