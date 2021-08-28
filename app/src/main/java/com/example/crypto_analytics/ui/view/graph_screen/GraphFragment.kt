package com.example.crypto_analytics.ui.view.graph_screen

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.crypto_analytics.R
import com.example.crypto_analytics.ui.viewmodel.GraphViewModel
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.XAxis.XAxisPosition
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet

class GraphFragment : Fragment(R.layout.fragment_graph) {
    private lateinit var chart: LineChart
    val graphFragment: GraphViewModel by viewModels()

    companion object {
        fun newInstance() = GraphFragment()
    }

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

        val lineEntry2 = mutableListOf(
            Entry(3f, 10f),
            Entry(5f, 200f),
            Entry(3f, 10f),
            Entry(3f, 110f),
            Entry(1f, 50f)
        )


        val dataSet = LineDataSet(lineEntry, "USD")
        val dataSet2 = LineDataSet(lineEntry2, "RU")

        dataSet.apply {
            mode = LineDataSet.Mode.LINEAR
            setDrawFilled(false)
            color = requireActivity().let { ContextCompat.getColor(it, R.color.purple_200) }
            lineWidth = 3f
            circleRadius = 6f
//            fillColor = ContextCompat.getColor(requireContext(), R.color.purple_200)
//            fillDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.chart_background_color)
        }

        dataSet2.apply {
            mode = LineDataSet.Mode.LINEAR
            setDrawFilled(false)
            color = requireActivity().let { ContextCompat.getColor(it, R.color.black) }
            lineWidth = 3f
            circleRadius = 6f
        }

        val listOfCharts = mutableListOf<ILineDataSet>(dataSet, dataSet2)

        val lineData = LineData(listOfCharts)

        chart.let {
            it.data = lineData
            it.animateXY(3000, 3000)
            it.description.isEnabled = false
            val xAxis: XAxis = it.xAxis
            xAxis.position = XAxisPosition.BOTTOM
            xAxis.setDrawGridLines(false)
            xAxis.setDrawAxisLine(true)

            val leftAxis: YAxis = it.axisLeft
            leftAxis.setLabelCount(5, false)
            leftAxis.axisMinimum = 0f


            val rightAxis: YAxis = it.axisRight
            rightAxis.setLabelCount(5, false)
            rightAxis.setDrawGridLines(false)
            rightAxis.axisMinimum = 0f

        }
//        MainScope().launch {
//            delay(1000)
//
//        }
    }
}