package com.example.cities.ui.list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.example.cities.databinding.FragmentListOfCitiesBinding
import com.example.cities.ui.main.SharedViewModel
import com.example.core.model.dto.ui.Result
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observe()
    }

    private fun observe() {
        sharedViewModel.uiState.onEach {
            setUpUI(it)
        }.flowWithLifecycle(lifecycle).launchIn(lifecycleScope)

        getCitiesByPageNumber()
    }

    private fun getCitiesByPageNumber(pageNumber: Int = 1) =
        sharedViewModel.getCitiesByPageNumber(pageNumber)

    private fun setUpUI(result: Result) = with(binding) {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}