package com.example.crypto_analytics.ui.view.home_screen

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.crypto_analytics.R
import com.example.crypto_analytics.data.util.appComponent
import com.example.crypto_analytics.databinding.FragmentHomeBinding
import com.example.crypto_analytics.ui.view.MainActivity
import lecho.lib.hellocharts.formatter.SimpleAxisValueFormatter
import lecho.lib.hellocharts.gesture.ContainerScrollType
import lecho.lib.hellocharts.gesture.ZoomType
import lecho.lib.hellocharts.model.*
import timber.log.Timber
import javax.inject.Inject


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    companion object {
        const val AXIS_X_NAME = "Количество прошедших суток"
        const val AXIS_Y_NAME = "Цена"
    }

    @Inject
    lateinit var serviceFactory: HomeViewModelFactory.ServiceFactory

    private var listOfPrices = mutableListOf<Float>()
    private lateinit var intervalList: MutableList<String>
    private lateinit var cryptoList: MutableList<String>
    private lateinit var fiatList: MutableList<String>

    var daysRange = 30
    var cryptoCurrency = "bitcoin"
    var fiatCurrency = "usd"
    private val homeViewModel: HomeViewModel by activityViewModels {
        serviceFactory.create(daysRange, cryptoCurrency, fiatCurrency)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        appComponent.inject(this)
        binding.graphDataFragment.button.setOnClickListener {
            findNavController().navigate(R.id.action_pagerContainerFragment_to_blankFragment)
        }

        intervalList = resources.getStringArray(R.array.interval_arr).toMutableList()
        cryptoList = resources.getStringArray(R.array.crypto_currency_arr).toMutableList()
        fiatList = resources.getStringArray(R.array.fiat_currency_arr).toMutableList()

        homeViewModel.cryptoLiveData.observe(viewLifecycleOwner) {
            listOfPrices = it.data?.priceList as ArrayList<Float>
            setLineChartData()
        }
    }

    override fun onResume() {
        (activity as MainActivity).binding.mainToolbar.title = resources.getString(R.string.home_screen_name)
        super.onResume()
        setDropDown(intervalList, binding.graphDataFragment.timeRangeDropdown)
        setDropDown(cryptoList, binding.graphDataFragment.cryptoCurrencyDropdown)
        setDropDown(fiatList, binding.graphDataFragment.fiatCurrencyDropdown)
    }

    private fun setDropDown (dataList: MutableList<String>, dropDown: AutoCompleteTextView) {
        val dropAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, dataList)
        dropDown.setAdapter(dropAdapter)
    }

    private fun setLineChartData() {
        binding.chart.apply {
            visibility = View.VISIBLE
            isInteractive = true
            ZoomType.HORIZONTAL_AND_VERTICAL
            setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL)
            isValueSelectionEnabled = true
            isViewportCalculationEnabled = false
        }
        val valuesEntry = mutableListOf<PointValue>()
        listOfPrices.forEachIndexed {index,  price ->
            valuesEntry.add(PointValue((index + 1).toFloat(), price))
        }

        setDaysRange()
        setCryptoCurrency()
        setFiatCurrency()

        setAxis(valuesEntry)
    }

    private fun setFiatCurrency() {
        binding.graphDataFragment.fiatCurrencyDropdown.setOnItemClickListener { _, _, position, _ ->
            when(position) {
                0 -> {
                    fiatCurrency = fiatList[0]
                    homeViewModel.getCryptoCurrency(daysRange, cryptoCurrency, fiatCurrency)
                }
                1 -> {
                    fiatCurrency = fiatList[1]
                    homeViewModel.getCryptoCurrency(daysRange, cryptoCurrency, fiatCurrency)
                }
                2 -> {
                    fiatCurrency= fiatList[2]
                    homeViewModel.getCryptoCurrency(daysRange, cryptoCurrency, fiatCurrency)
                }
            }
        }
    }

    private fun setDaysRange() {
        binding.graphDataFragment.timeRangeDropdown.setOnItemClickListener { _, _, position, _ ->
            when(position) {
                0 -> {
                    daysRange = intervalList[0].toInt()
                    homeViewModel.getCryptoCurrency(daysRange, cryptoCurrency, fiatCurrency)
                }
                1 -> {
                    daysRange = intervalList[1].toInt()
                    homeViewModel.getCryptoCurrency(daysRange, cryptoCurrency, fiatCurrency)
                }
            }
        }
    }

    private fun setCryptoCurrency() {
        binding.graphDataFragment.cryptoCurrencyDropdown.setOnItemClickListener { _, _, position, _ ->
            when(position) {
                0 -> {
                    cryptoCurrency = cryptoList[0]
                    homeViewModel.getCryptoCurrency(daysRange, cryptoCurrency, fiatCurrency)
                }
                1 -> {
                    cryptoCurrency = cryptoList[1]
                    homeViewModel.getCryptoCurrency(daysRange, cryptoCurrency, fiatCurrency)
                }
                2 -> {
                    cryptoCurrency = cryptoList[2]
                    homeViewModel.getCryptoCurrency(daysRange, cryptoCurrency, fiatCurrency)
                }
                3 -> {
                    cryptoCurrency = cryptoList[3]
                    homeViewModel.getCryptoCurrency(daysRange, cryptoCurrency, fiatCurrency)
                }
                4 -> {
                    cryptoCurrency = cryptoList[4]
                    homeViewModel.getCryptoCurrency(daysRange, cryptoCurrency, fiatCurrency)
                }
                5 -> {
                    cryptoCurrency = cryptoList[5]
                    homeViewModel.getCryptoCurrency(daysRange, cryptoCurrency, fiatCurrency)
                }
            }
        }
    }

    private lateinit var lines: MutableList<Line>
    private fun setAxis(valuesEntry: MutableList<PointValue>) {
        val line = Line(valuesEntry)
        lines = mutableListOf()

        line.apply {
            shape = ValueShape.CIRCLE
            setHasLabelsOnlyForSelected(true)
            color = Color.BLUE
            isCubic = true
            isFilled = true
        }

        lines.add(line)

        val data = LineChartData(lines)
        val axisX = Axis()
        val axisY = Axis()
        axisX.apply {
            formatter = SimpleAxisValueFormatter().setDecimalDigitsNumber(0)
            axisX.name = AXIS_X_NAME
            textColor = Color.BLACK
        }
        axisY.apply {
            setHasLines(true)
            name = AXIS_Y_NAME
            maxLabelChars = 7
            textColor = Color.BLACK
        }
        data.apply {
            axisXBottom = axisX
            axisYLeft = axisY
            axisXBottom.lineColor = Color.BLACK
            axisYLeft.lineColor = Color.BLACK

        }
        binding.chart.lineChartData = data

        setViewPort(valuesEntry)
    }

    private fun setViewPort(valuesEntry: MutableList<PointValue>) {
        val viewPort = Viewport(binding.chart.maximumViewport)
        val minAxisValue = listOfPrices.minOrNull() ?: 0F
        val maxAxisValue = listOfPrices.maxOrNull() ?: 0F
        viewPort.bottom = minAxisValue.minus(minAxisValue/100)
        viewPort.top = maxAxisValue.plus(maxAxisValue/100)
        viewPort.left = 1f
        viewPort.right = (valuesEntry.size).toFloat()
        binding.chart.apply {
            maximumViewport = viewPort
            currentViewport = viewPort
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}