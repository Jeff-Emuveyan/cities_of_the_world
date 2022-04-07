package com.example.cities.ui

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.example.cities.databinding.MainFragmentBinding
import com.example.cities.ui.list.ListOfCitiesFragment
import com.example.cities.ui.map.MapOfCitiesFragment

class MainFragment : Fragment() {

    private lateinit var viewModel: SharedViewModel
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
        viewModel = ViewModelProvider(this).get(SharedViewModel::class.java)
        setUpUi()
    }

    private fun setUpUi() = with(binding) {
        val adapter = Adapter(this@MainFragment)
        adapter.addFragment(ListOfCitiesFragment())
        adapter.addFragment(MapOfCitiesFragment())

        viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        viewPager.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}