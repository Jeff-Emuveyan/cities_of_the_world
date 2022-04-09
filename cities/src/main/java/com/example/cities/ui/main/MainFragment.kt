package com.example.cities.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.example.cities.databinding.MainFragmentBinding
import com.example.cities.ui.list.ListOfCitiesFragment
import com.example.cities.ui.map.MapOfCitiesFragment
import com.example.core.model.entity.CityEntity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class MainFragment : Fragment() {

    private val sharedViewModel by activityViewModels<SharedViewModel>()
    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MainFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setUpUi()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setUpUi() = with(binding) {
        val adapter = Adapter(this@MainFragment)
        adapter.addFragment(ListOfCitiesFragment())
        adapter.addFragment(MapOfCitiesFragment())

        viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        viewPager.adapter = adapter
    }

    private fun observe() {
        sharedViewModel.zoomInOnCityFlow.onEach {
            handleZoomInOnCityRequest(it)
        }.flowWithLifecycle(lifecycle).launchIn(lifecycleScope)
    }

    private fun handleZoomInOnCityRequest(it: Pair<Boolean, CityEntity>) = with(binding)  {
        val zoom = it.first
        if (zoom) {
            viewPager.setCurrentItem(1, true)
        }
    }
}