package com.example.cities.ui.map

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.example.cities.R
import com.example.cities.databinding.FragmentMapOfCitiesBinding
import com.example.cities.util.getAddress
import com.example.core.model.dto.MapObject
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapOfCitiesFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentMapOfCitiesBinding? = null
    private val binding get() = _binding!!
    private var googleMap: GoogleMap? = null
    private val args: MapOfCitiesFragmentArgs by navArgs()

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
        setUpMap()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onMapReady(mMap: GoogleMap) {
        mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
        googleMap = mMap
        setUpUI(args)
    }

    private fun setUpUI(args: MapOfCitiesFragmentArgs) {
        val latitude = args.latitude
        val longitude = args.longitude
        val cityName = args.cityName
        val countryName = args.countryName
        if (latitude == 0.0f || longitude == 0.0f) {
            uiStateNoResult()
        } else {
            uiStateSuccess(MapObject(latitude.toDouble(), longitude.toDouble(), cityName, countryName))
        }
    }

    private fun setUpMap() {
        val supportMapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        supportMapFragment?.getMapAsync(this)
    }

    private fun uiStateNoResult() = with(binding) {
        tvInfo.text = getString(R.string.no_location_found)
    }

    private fun uiStateSuccess(mapObject: MapObject) = with(binding) {
        val countryName = mapObject.countryName
        val city = mapObject.cityName
        tvInfo.text = getString(R.string.msg_location, countryName, city)
        googleMap?.let { addCityOnMap(requireContext(), it, mapObject) }
    }

    private fun addCityOnMap(context: Context, googleMap: GoogleMap, mapObject: MapObject) {
        googleMap.addMarker(
            MarkerOptions()
                .position(LatLng(mapObject.latitude, mapObject.longitude))
                .title(mapObject.cityName)
                .snippet("Address: ${getAddress(context,mapObject.latitude, mapObject.longitude)}")
        )
        zoomCameraToLocation(mapObject.latitude, mapObject.longitude)
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
}