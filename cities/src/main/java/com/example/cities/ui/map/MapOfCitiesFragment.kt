package com.example.cities.ui.map

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.example.cities.R
import com.example.cities.databinding.FragmentMapOfCitiesBinding
import com.example.cities.ui.SharedViewModel
import com.example.cities.util.getAddress
import com.example.core.model.dto.Query
import com.example.core.model.dto.QueryType
import com.example.core.model.entity.CityEntity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import com.example.core.model.dto.ui.Result
import com.example.core.model.dto.ui.UIStateType

@AndroidEntryPoint
class MapOfCitiesFragment : Fragment(), OnMapReadyCallback {

    private val sharedViewModel by activityViewModels<SharedViewModel>()
    private var _binding: FragmentMapOfCitiesBinding? = null
    private val binding get() = _binding!!
    private var googleMap: GoogleMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapOfCitiesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpUI(Result(UIStateType.DEFAULT))
        observeData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onMapReady(mMap: GoogleMap) {
        mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
        googleMap = mMap
    }

    private fun setUpUI(result: Result) {
        when(result.type) {
            UIStateType.LOADING -> { uiStateLoading() }
            UIStateType.SUCCESS -> { uiStateSuccess(result.cities) }
            UIStateType.NO_RESULT -> { uiStateNoResult() }
            UIStateType.NETWORK_ERROR -> { uiStateNetworkError() }
            UIStateType.DEFAULT -> { setUpMap() }
        }
    }

    private fun uiStateLoading() = with(binding) {
        tvInfo.visibility = View.VISIBLE
        tvInfo.isEnabled = false
        tvInfo.text = getString(R.string.msg_loading)
    }

    private fun uiStateSuccess(cities: List<CityEntity>?) = with(binding) {
        if (cities == null) return@with
        tvInfo.visibility = View.INVISIBLE
        googleMap?.let { displayCities(requireContext(), it, cities) }
    }

    private fun uiStateNoResult() = with(binding) {
        tvInfo.visibility = View.INVISIBLE
        tvInfo.isEnabled = false
    }

    private fun uiStateNetworkError() = with(binding)  {
        tvInfo.visibility = View.VISIBLE
        tvInfo.text = getString(R.string.msg_network_error)
        tvInfo.isEnabled = true
        tvInfo.setOnClickListener {
            getCitiesByPageNumber(sharedViewModel.getNextPageNumber())
        }
    }

    private fun observeData() {
        sharedViewModel.uiState.onEach {
            setUpUI(it)
        }.flowWithLifecycle(lifecycle).launchIn(lifecycleScope)
    }

    private fun getCitiesByPageNumber(pageNumber: Int = 1) =
        sharedViewModel.getCities(Query(QueryType.PAGE_NUMBER, pageNumber))

    private fun setUpMap() {
        val supportMapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        supportMapFragment?.getMapAsync(this)
    }

    private fun displayCities(context: Context, googleMap: GoogleMap, cities: List<CityEntity>) {
        cities.forEach { addCityOnMap(context, googleMap, it) }
    }

    private fun addCityOnMap(context: Context, googleMap: GoogleMap, city: CityEntity) {
        val latitude = city.lat ?: 0.0
        val longitude = city.lng ?: 0.0
        googleMap.addMarker(
            MarkerOptions()
                .position(LatLng(latitude, longitude))
                .title(city.name)
                .snippet("Address: ${getAddress(context, latitude,longitude)}")
        )
        zoomCameraToLocation(latitude, longitude)
    }

    private fun zoomCameraToLocation(latitude: Double, longitude: Double) {
        val googlePlex = CameraPosition.builder()
            .target(LatLng(latitude, longitude))
            .zoom(16f)
            .bearing(0f)
            .tilt(45f)
            .build()

        googleMap?.animateCamera(CameraUpdateFactory.newCameraPosition(googlePlex), 3000, null)
    }

    private fun handleZoomInOnCityRequest(city: CityEntity) {
       zoomCameraToLocation(city.lat ?: 0.0, city.lng ?: 0.0)
    }
}