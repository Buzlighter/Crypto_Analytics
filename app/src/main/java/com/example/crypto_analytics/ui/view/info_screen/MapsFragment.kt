package com.example.crypto_analytics.ui.view.info_screen

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.crypto_analytics.R
import com.example.crypto_analytics.data.model.ATM
import com.example.crypto_analytics.data.util.Constants
import com.example.crypto_analytics.data.util.appComponent
import com.example.crypto_analytics.databinding.FragmentMapsBinding
import com.example.crypto_analytics.ui.common.PagerContainerFragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MapsFragment : Fragment() {
    var _binding: FragmentMapsBinding? = null
    val binding get() = _binding!!

    private var listOfATM = listOf<ATM>()
    val moscow = LatLng(55.751244, 37.618423)

    private val callback = OnMapReadyCallback { googleMap ->
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(moscow))

        val region = googleMap.projection.visibleRegion

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(moscow, 8F))
        listOfATM.forEach { atmData ->
                val point = LatLng(atmData.latitude.toDouble(), atmData.lontitude.toDouble())
                googleMap.addMarker(MarkerOptions().position(point).title(atmData.name))
        }

        Log.v("bounds_loc","bottom left corner: ${region.nearLeft}" )
        Log.v("bounds_loc","top left corner ${region.farLeft}")
        Log.v("bounds_loc","bottom right corner ${region.nearRight}")
        Log.v("bounds_loc","top right corner ${region.farRight}")

        googleMap.setOnMapClickListener {coordinate->
            Log.v("bounds_loc","clicked: $coordinate")
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        appComponent.inject(this)
        _binding = FragmentMapsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        PagerContainerFragment.viewPager.isUserInputEnabled = false

        listOfATM = arguments?.getParcelableArrayList(Constants.BUNDLE_INFO_ATM_LIST) ?: arrayListOf()

        lifecycleScope.launch(Dispatchers.Default) {
            listOfATM = listOfATM.filter { atmData ->  atmData.latitude in 53F..57F && atmData.lontitude in 36F..39F }
        }

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

        Log.v("my_atm", listOfATM.size.toString())

        mapFragment?.getMapAsync { googleMap ->
            googleMap.setOnCameraMoveStartedListener(startCameraListener)
            googleMap.setOnCameraIdleListener(idleCameraListener)
        }
    }


    private val startCameraListener = GoogleMap.OnCameraMoveStartedListener {
        PagerContainerFragment.viewPager.isUserInputEnabled = false
        Log.v("motion_event", "start")
    }

    private val idleCameraListener = GoogleMap.OnCameraIdleListener {
        PagerContainerFragment.viewPager.isUserInputEnabled = true
        Log.v("motion_event", "cancel")
    }
}