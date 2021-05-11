package com.handcontrol.ui.main.chart

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.handcontrol.R
import com.handcontrol.api.Api
import com.handcontrol.databinding.FragmentChartBinding
import kotlinx.android.synthetic.main.fragment_chart.*
import kotlinx.coroutines.runBlocking

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
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE)
            enterFullScreen()
    }

    override fun onStop() {
        super.onStop()
        hideMenuItem = false
        activity?.invalidateOptionsMenu()
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE)
            exitFullScreen()
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

    private fun enterFullScreen() {
        activity?.window?.decorView?.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION)
        (activity as AppCompatActivity?)?.supportActionBar?.hide()
        activity?.findViewById<BottomNavigationView>(R.id.nav_view)?.visibility = View.GONE
        chart?.let {
            scrollView?.post {
                scrollView?.scrollTo(0, it.top)
            }
        }
    }

    private fun exitFullScreen() {
        activity?.window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
        (activity as AppCompatActivity?)?.supportActionBar?.show()
        activity?.findViewById<BottomNavigationView>(R.id.nav_view)?.visibility = View.VISIBLE
        runBlocking {
            Api.getApiHandler().stopTelemetry()
        }
    }
}