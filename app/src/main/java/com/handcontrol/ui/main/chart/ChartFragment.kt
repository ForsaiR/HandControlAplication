package com.handcontrol.ui.main.chart

import android.content.res.Configuration
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.handcontrol.R
import com.handcontrol.databinding.FragmentChartBinding
import kotlinx.android.synthetic.main.fragment_chart.*

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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE)
            enterFullScreen()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE)
            exitFullScreen()
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

    private fun enterFullScreen() {
        activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        (activity as AppCompatActivity?)?.supportActionBar?.hide()
        activity?.findViewById<BottomNavigationView>(R.id.nav_view)?.visibility = View.GONE
        chart?.let {
            scrollView?.post {
                scrollView?.scrollTo(0, it.top)
            }
        }
    }

    private fun exitFullScreen() {
        activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        (activity as AppCompatActivity?)?.supportActionBar?.show()
        activity?.findViewById<BottomNavigationView>(R.id.nav_view)?.visibility = View.VISIBLE
    }
}