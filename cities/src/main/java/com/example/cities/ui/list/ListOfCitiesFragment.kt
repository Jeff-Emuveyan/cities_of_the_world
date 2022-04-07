package com.example.cities.ui.list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.cities.R
import com.example.cities.databinding.FragmentListOfCitiesBinding
import com.example.cities.databinding.MainFragmentBinding
import com.example.cities.ui.SharedViewModel

class ListOfCitiesFragment : Fragment() {

    private val sharedViewModel by activityViewModels<SharedViewModel>()
    private var _binding: FragmentListOfCitiesBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListOfCitiesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}