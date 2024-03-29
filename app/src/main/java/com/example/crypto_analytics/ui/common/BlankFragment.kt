package com.example.crypto_analytics.ui.common

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.crypto_analytics.R
import lecho.lib.hellocharts.animation.ChartAnimationListener
import lecho.lib.hellocharts.gesture.ZoomType
import lecho.lib.hellocharts.listener.LineChartOnValueSelectListener
import lecho.lib.hellocharts.model.*
import lecho.lib.hellocharts.util.ChartUtils
import lecho.lib.hellocharts.view.LineChartView


class BlankFragment : Fragment() {

    private lateinit var chartExample: LineChartView
    private var data: LineChartData? = null
    private var numberOfLines = 1
    private val maxNumberOfLines = 4
    private val numberOfPoints = 20

    var randomNumbersTab = Array(maxNumberOfLines) {
        FloatArray(
            numberOfPoints
        )
    }

    private var hasAxes = true
    private var hasAxesNames = true
    private var hasLines = true
    private var hasPoints = true
    private var shape = ValueShape.CIRCLE
    private var isFilled = false
    private var hasLabels = false
    private var isCubic = false
    private var hasLabelForSelected = false
    private var pointsHaveDifferentColor = false
    private var hasGradientToTransparent = false
    fun PlaceholderFragment() {}

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        val rootView: View = inflater.inflate(R.layout.fragment_blank, container, false)
        chartExample = rootView.findViewById<View>(R.id.chart) as LineChartView
        chartExample.setOnValueTouchListener(ValueTouchListener())

        // Generate some random values.
        generateValues()
        generateData()

        // Disable viewport recalculations, see toggleCubic() method for more info.
        chartExample.setViewportCalculationEnabled(false)
        resetViewport()
        return rootView
    }

    // MENU
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.line_chart, menu);
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.action_reset) {
            reset()
            generateData()
            return true
        }
        if (id == R.id.action_add_line) {
            addLineToData()
            return true
        }
        if (id == R.id.action_toggle_lines) {
            toggleLines()
            return true
        }
        if (id == R.id.action_toggle_points) {
            togglePoints()
            return true
        }
        if (id == R.id.action_toggle_gradient) {
            toggleGradient()
            return true
        }
        if (id == R.id.action_toggle_cubic) {
            toggleCubic()
            return true
        }
        if (id == R.id.action_toggle_area) {
            toggleFilled()
            return true
        }
        if (id == R.id.action_point_color) {
            togglePointColor()
            return true
        }
        if (id == R.id.action_shape_circles) {
            setCircles()
            return true
        }
        if (id == R.id.action_shape_square) {
            setSquares()
            return true
        }
        if (id == R.id.action_shape_diamond) {
            setDiamonds()
            return true
        }
        if (id == R.id.action_toggle_labels) {
            toggleLabels()
            return true
        }
        if (id == R.id.action_toggle_axes) {
            toggleAxes()
            return true
        }
        if (id == R.id.action_toggle_axes_names) {
            toggleAxesNames()
            return true
        }
        if (id == R.id.action_animate) {
            prepareDataAnimation()
            chartExample.startDataAnimation()
            return true
        }
        if (id == R.id.action_toggle_selection_mode) {
            toggleLabelForSelected()
            Toast.makeText(
                activity,
                "Selection mode set to " + chartExample.isValueSelectionEnabled() + " select any point.",
                Toast.LENGTH_SHORT
            ).show()
            return true
        }
        if (id == R.id.action_toggle_touch_zoom) {
            chartExample.setZoomEnabled(!chartExample.isZoomEnabled())
            Toast.makeText(activity, "IsZoomEnabled " + chartExample.isZoomEnabled(), Toast.LENGTH_SHORT).show()
            return true
        }
        if (id == R.id.action_zoom_both) {
            chartExample.setZoomType(ZoomType.HORIZONTAL_AND_VERTICAL)
            return true
        }
        if (id == R.id.action_zoom_horizontal) {
            chartExample.setZoomType(ZoomType.HORIZONTAL)
            return true
        }
        if (id == R.id.action_zoom_vertical) {
            chartExample.setZoomType(ZoomType.VERTICAL)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun generateValues() {
        for (i in 0 until maxNumberOfLines) {
            for (j in 0 until numberOfPoints) {
                randomNumbersTab[i][j] = Math.random().toFloat() * 100f
            }
        }
    }

    private fun reset() {
        numberOfLines = 1
        hasAxes = true
        hasAxesNames = true
        hasLines = true
        hasPoints = true
        shape = ValueShape.CIRCLE
        isFilled = false
        hasLabels = false
        isCubic = false
        hasLabelForSelected = false
        pointsHaveDifferentColor = false
        chartExample.setValueSelectionEnabled(hasLabelForSelected)
        resetViewport()
    }

    private fun resetViewport() {
        // Reset viewport height range to (0,100)
        val v = Viewport(chartExample.maximumViewport)
        v.bottom = 0f
        v.top = 100f
        v.left = 0f
        v.right = (numberOfPoints - 1).toFloat()
        chartExample.setMaximumViewport(v)
        chartExample.setCurrentViewport(v)
    }

    private fun generateData() {
        val lines: MutableList<Line> = java.util.ArrayList()
        for (i in 0 until numberOfLines) {
            val values: MutableList<PointValue> = java.util.ArrayList()
            for (j in 0 until numberOfPoints) {
                values.add(PointValue(j.toFloat(), randomNumbersTab[i][j]))
            }
            val line = Line(values)
            line.color = ChartUtils.COLORS[i]
            line.shape = shape
            line.isCubic = isCubic
            line.isFilled = isFilled
            line.setHasLabels(hasLabels)
            line.setHasLabelsOnlyForSelected(hasLabelForSelected)
            line.setHasLines(hasLines)
            line.setHasPoints(hasPoints)
            if (pointsHaveDifferentColor) {
                line.pointColor = ChartUtils.COLORS[(i + 1) % ChartUtils.COLORS.size]
            }
            lines.add(line)
        }
        data = LineChartData(lines)
        if (hasAxes) {
            val axisX = Axis()
            val axisY = Axis().setHasLines(true)
            if (hasAxesNames) {
                axisX.name = "Axis X"
                axisY.name = "Axis Y"
            }
            data!!.axisXBottom = axisX
            data!!.axisYLeft = axisY
        } else {
            data!!.axisXBottom = null
            data!!.axisYLeft = null
        }
        data!!.baseValue = Float.NEGATIVE_INFINITY
        chartExample.setLineChartData(data)
    }

    /**
     * Adds lines to data, after that data should be set again with
     * [LineChartView.setLineChartData]. Last 4th line has non-monotonically x values.
     */
    private fun addLineToData() {
        if (data!!.lines.size >= maxNumberOfLines) {
            Toast.makeText(activity, "Samples app uses max 4 lines!", Toast.LENGTH_SHORT).show()
            return
        } else {
            ++numberOfLines
        }
        generateData()
    }

    private fun toggleLines() {
        hasLines = !hasLines
        generateData()
    }

    private fun togglePoints() {
        hasPoints = !hasPoints
        generateData()
    }

    private fun toggleGradient() {
        hasGradientToTransparent = !hasGradientToTransparent
        generateData()
    }

    private fun toggleCubic() {
        isCubic = !isCubic
        generateData()
        if (isCubic) {
            // It is good idea to manually set a little higher max viewport for cubic lines because sometimes line
            // go above or below max/min. To do that use Viewport.inest() method and pass negative value as dy
            // parameter or just set top and bottom values manually.
            // In this example I know that Y values are within (0,100) range so I set viewport height range manually
            // to (-5, 105).
            // To make this works during animations you should use Chart.setViewportCalculationEnabled(false) before
            // modifying viewport.
            // Remember to set viewport after you call setLineChartData().
            val v = Viewport(chartExample.getMaximumViewport())
            v.bottom = -5f
            v.top = 105f
            // You have to set max and current viewports separately.
            chartExample.setMaximumViewport(v)
            // I changing current viewport with animation in this case.
            chartExample.setCurrentViewportWithAnimation(v)
        } else {
            // If not cubic restore viewport to (0,100) range.
            val v = Viewport(chartExample.getMaximumViewport())
            v.bottom = 0f
            v.top = 100f

            // You have to set max and current viewports separately.
            // In this case, if I want animation I have to set current viewport first and use animation listener.
            // Max viewport will be set in onAnimationFinished method.
            chartExample.setViewportAnimationListener(object : ChartAnimationListener {
                override fun onAnimationStarted() {
                    // TODO Auto-generated method stub
                }

                override fun onAnimationFinished() {
                    // Set max viewpirt and remove listener.
                    chartExample.setMaximumViewport(v)
                    chartExample.setViewportAnimationListener(null)
                }
            })
            // Set current viewpirt with animation;
            chartExample.setCurrentViewportWithAnimation(v)
        }
    }

    private fun toggleFilled() {
        isFilled = !isFilled
        generateData()
    }

    private fun togglePointColor() {
        pointsHaveDifferentColor = !pointsHaveDifferentColor
        generateData()
    }

    private fun setCircles() {
        shape = ValueShape.CIRCLE
        generateData()
    }

    private fun setSquares() {
        shape = ValueShape.SQUARE
        generateData()
    }

    private fun setDiamonds() {
        shape = ValueShape.DIAMOND
        generateData()
    }

    private fun toggleLabels() {
        hasLabels = !hasLabels
        if (hasLabels) {
            hasLabelForSelected = false
            chartExample.setValueSelectionEnabled(hasLabelForSelected)
        }
        generateData()
    }

    private fun toggleLabelForSelected() {
        hasLabelForSelected = !hasLabelForSelected
        chartExample.setValueSelectionEnabled(hasLabelForSelected)
        if (hasLabelForSelected) {
            hasLabels = false
        }
        generateData()
    }

    private fun toggleAxes() {
        hasAxes = !hasAxes
        generateData()
    }

    private fun toggleAxesNames() {
        hasAxesNames = !hasAxesNames
        generateData()
    }

    /**
     * To animate values you have to change targets values and then call [Chart.startDataAnimation]
     * method(don't confuse with View.animate()). If you operate on data that was set before you don't have to call
     * [LineChartView.setLineChartData] again.
     */
    private fun prepareDataAnimation() {
        for (line in data!!.lines) {
            for (value in line.values) {
                // Here I modify target only for Y values but it is OK to modify X targets as well.
                value.setTarget(value.x, Math.random().toFloat() * 100)
            }
        }
    }

    private class ValueTouchListener : LineChartOnValueSelectListener {
        override fun onValueSelected(lineIndex: Int, pointIndex: Int, value: PointValue) {

        }

        override fun onValueDeselected() {
            // TODO Auto-generated method stub
        }
    }
}