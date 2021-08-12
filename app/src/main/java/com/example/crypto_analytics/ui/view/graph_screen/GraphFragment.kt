package com.example.crypto_analytics.ui.view.graph_screen

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.crypto_analytics.R
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class GraphFragment : Fragment(R.layout.fragment_graph) {
    private lateinit var chart: LineChart


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        chart = view.findViewById(R.id.chart) as LineChart
        setLineChartData()

    }


    private fun setLineChartData() {
        val lineEntry = mutableListOf(
            Entry(1f, 100f),
            Entry(2f, 200f),
            Entry(3f, 600f),
            Entry(4f, 300f),
            Entry(5f, 50f)
        )

        val dataSet = LineDataSet(lineEntry, "USD")

        dataSet.apply {
            mode = LineDataSet.Mode.CUBIC_BEZIER
            setDrawFilled(true)
            color = requireActivity().let { ContextCompat.getColor(it, R.color.purple_200) }
            fillColor = ContextCompat.getColor(requireContext(), R.color.purple_200)
            fillDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.chart_background_color)
        }

        val lineData = LineData(dataSet)

        chart.let {
            it.data = lineData
            it.animateXY(3000, 3000)
            it.description.isEnabled = false
        }
//        MainScope().launch {
//            delay(1000)
//
//        }
    }
}