package com.example.crypto_analytics.ui.view.info_screen

import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.crypto_analytics.R
import com.example.crypto_analytics.data.model.ATM
import com.example.crypto_analytics.data.util.*
import com.example.crypto_analytics.databinding.FragmentInfoBinding
import com.example.crypto_analytics.ui.common.PagerContainerFragment
import com.example.crypto_analytics.ui.view.MainActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import javax.inject.Inject

class InfoFragment : Fragment() {
    private var _binding: FragmentInfoBinding? = null
    private val binding get() = _binding!!

//    lateinit var infoFragment: InfoFragment

    private var listOfATM = listOf<ATM>()
    private var bundleOfATM = Bundle()
    private var snackBarError: Snackbar? = null
    private var isClicked = false

    @Inject
    lateinit var infoViewModelFactory: InfoViewModelFactory

    val infoViewModel: InfoViewModel by viewModels {
        infoViewModelFactory
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        appComponent.inject(this)
       _binding = FragmentInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.replaceBtn.setOnClickListener(mapPresentClickListener)
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).binding.mainToolbar.title = resources.getString(R.string.info_screen_name)
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


    private val mapPresentClickListener = View.OnClickListener {
        isClicked = isClicked.not()
        if (isClicked) {
            showMap()
            lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    collectCoinMapValues()
                }
            }
        } else {
            hideMap()
        }
    }


    private suspend fun collectCoinMapValues() {
        infoViewModel.coinMapStateFlow.collect { state ->
            when(state) {
                is DataState.Success -> {
                    viewStateSuccess()
                    listOfATM = state.data ?: arrayListOf()
                    bundleOfATM.putParcelableArrayList(Constants.BUNDLE_INFO_ATM_LIST, listOfATM as ArrayList<out Parcelable>)

                    Log.i("my_atm", listOfATM.size.toString())

                    childFragmentManager.commit {
                        setReorderingAllowed(true)
                        replace(R.id.map_container, MapsFragment::class.java, bundleOfATM)
                        addToBackStack(null)
                    }
                }
                is DataState.Loading -> { viewStateLoading() }
                is DataState.Error -> { viewStateError() }
            }
        }
    }

    private fun viewStateSuccess() {
        binding.mapContainer.visibility = View.VISIBLE
        binding.newsLoadingLayout.root.visibility = View.GONE

        if (snackBarError != null) {
            snackBarError?.dismiss()
        }
    }

    private fun viewStateLoading() {
        binding.newsLoadingLayout.root.visibility = View.VISIBLE
        binding.mapContainer.visibility = View.GONE
    }

    private fun viewStateError() {
        binding.mapContainer.visibility = View.GONE
        binding.mapContainer.visibility = View.GONE
        if (hasInternetConnectivity(requireContext()).not()) {
            snackBarError = getErrorSnackBar(PagerContainerFragment.bottomNavigationView)
                .setAction(R.string.repeat) {
                    infoViewModel.getCoinMapList()
                }
            snackBarError?.show()
        }
    }

    private fun showMap() {
        binding.mapContainer.visibility = View.VISIBLE
        binding.newsLoadingLayout.root.visibility = View.GONE
    }

    private fun hideMap() {
        binding.mapContainer.visibility = View.GONE
        binding.mapContainer.visibility = View.GONE
    }
}