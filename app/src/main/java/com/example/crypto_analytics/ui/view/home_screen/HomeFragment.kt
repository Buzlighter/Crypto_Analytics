package com.example.crypto_analytics.ui.view.home_screen

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.crypto_analytics.App
import com.example.crypto_analytics.R
import com.example.crypto_analytics.data.util.DataState
import com.example.crypto_analytics.data.util.appComponent
import com.example.crypto_analytics.data.util.getErrorSnackBar
import com.example.crypto_analytics.data.util.hasInternetConnectivity
import com.example.crypto_analytics.databinding.FragmentHomeBinding
import com.example.crypto_analytics.ui.common.PagerContainerFragment
import com.example.crypto_analytics.ui.view.MainActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import lecho.lib.hellocharts.formatter.SimpleAxisValueFormatter
import lecho.lib.hellocharts.gesture.ContainerScrollType
import lecho.lib.hellocharts.gesture.ZoomType
import lecho.lib.hellocharts.model.*
import javax.inject.Inject


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    companion object {
        const val AXIS_X_NAME = "Количество прошедших суток"
        const val AXIS_Y_NAME = "Цена"
    }

    private var snackBarError: Snackbar? = null

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


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        appComponent.inject(this)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        intervalList = resources.getStringArray(R.array.interval_arr).toMutableList()
        cryptoList = resources.getStringArray(R.array.crypto_currency_arr).toMutableList()
        fiatList = resources.getStringArray(R.array.fiat_currency_arr).toMutableList()

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                getGraphParameters()
            }
        }

        binding.homeRefresher.setOnRefreshListener(refreshListener)
        binding.notificationInputText.addTextChangedListener(textInputWatcher)
    }

    override fun onResume() {
        (activity as MainActivity).binding.mainToolbar.title = resources.getString(R.string.home_screen_name)
        super.onResume()
        binding.apply {
            setDropDown(intervalList, graphDataLayout.intervalLayout.timeRangeDropdown)
            setDropDown(cryptoList, graphDataLayout.currencyLayout.cryptoCurrencyDropdown)
            setDropDown(fiatList, graphDataLayout.fiatLayout.fiatCurrencyDropdown)
        }
    }

    override fun onPause() {
        super.onPause()
        if (snackBarError != null) {
            snackBarError?.dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private suspend fun getGraphParameters() {
        homeViewModel.cryptoGraphDataState.collect { state ->
            when(state) {
                is DataState.Success -> {
                    viewStateSuccess()
                    listOfPrices = state.data!!.paramsList
                    setLineChartData()
                }
                is DataState.Loading -> viewStateLoading()
                is DataState.Error -> viewStateError()
            }
        }
    }

    private val refreshListener = SwipeRefreshLayout.OnRefreshListener {
        homeViewModel.getCryptoCurrency(daysRange, cryptoCurrency, fiatCurrency)
        binding.homeRefresher.isRefreshing = false
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
        binding.graphDataLayout.fiatLayout.fiatCurrencyDropdown.setOnItemClickListener { _, _, position, _ ->
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
        binding.graphDataLayout.intervalLayout.timeRangeDropdown.setOnItemClickListener { _, _, position, _ ->
            when (position) {
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
        binding.graphDataLayout.currencyLayout.cryptoCurrencyDropdown.setOnItemClickListener { _, _, position, _ ->
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
            color = ContextCompat.getColor(requireContext().applicationContext, R.color.green)
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
            axisXBottom.lineColor = Color.WHITE
            axisYLeft.lineColor = Color.WHITE

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

    private val textInputWatcher = object : TextWatcher {

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

        override fun afterTextChanged(editCost: Editable?) {
            if (editCost?.isNotEmpty() == true)  {
                App.notificationPrice = editCost.toString().toFloat()
            }
        }
    }


    //View States

    private fun viewStateSuccess() {
        binding.chart.visibility = View.VISIBLE
        binding.graphDataLayout.root.visibility = View.VISIBLE
        binding.notificationInputLayout.visibility = View.VISIBLE
        binding.homeErrorLayout.root.visibility = View.GONE
        binding.homeLoading.root.visibility = View.GONE

        if (snackBarError != null) {
            snackBarError?.dismiss()
        }
    }

    private fun viewStateLoading() {
        binding.homeLoading.root.visibility = View.VISIBLE
        binding.graphDataLayout.root.visibility = View.VISIBLE
        binding.homeErrorLayout.root.visibility = View.GONE
        binding.homeErrorLayout.root.visibility = View.GONE
        binding.notificationInputLayout.visibility = View.INVISIBLE
    }

    private fun viewStateError() {
        binding.homeErrorLayout.root.visibility = View.VISIBLE
        binding.chart.visibility = View.INVISIBLE
        binding.homeLoading.root.visibility = View.GONE
        binding.notificationInputLayout.visibility = View.INVISIBLE
        binding.graphDataLayout.root.visibility = View.GONE

        if (hasInternetConnectivity(requireContext()).not()) {
            snackBarError = getErrorSnackBar(PagerContainerFragment.bottomNavigationView)
                .setAction(R.string.repeat) {
                    homeViewModel.getCryptoCurrency(daysRange, cryptoCurrency, fiatCurrency)
                }
            snackBarError?.show()
        }
    }
}