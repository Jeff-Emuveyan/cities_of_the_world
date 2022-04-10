package com.example.cities.ui.list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cities.R
import com.example.cities.databinding.FragmentListOfCitiesBinding
import com.example.cities.ui.SharedViewModel
import com.example.cities.util.EndlessRecyclerViewScrollListener
import com.example.core.model.dto.Query
import com.example.core.model.dto.QueryType
import com.example.core.model.dto.ui.UIStateType
import com.example.core.model.entity.CityEntity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import com.example.core.model.dto.Result
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ListOfCitiesFragment : Fragment() {

    private val sharedViewModel by activityViewModels<SharedViewModel>()
    private var _binding: FragmentListOfCitiesBinding? = null
    private val binding get() = _binding!!
    private var cityAdapter: CityAdapter? = null

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
        observeData()
        setUpUI(Result(UIStateType.DEFAULT))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeData() {
        sharedViewModel.uiState.onEach {
            setUpUI(it)
        }.flowWithLifecycle(lifecycle).launchIn(lifecycleScope)

        getCitiesByPageNumber()
    }

    private fun setUpUI(result: Result) = with(binding) {
        when(result.type) {
            UIStateType.LOADING -> { uiStateLoading() }
            UIStateType.SUCCESS -> { uiStateSuccess(result.cities) }
            UIStateType.NO_RESULT -> { uiStateNoResult() }
            UIStateType.NETWORK_ERROR -> { uiStateNetworkError() }
            UIStateType.DEFAULT -> {
                setUpRecyclerView()
                setUpSearchView()
            }
        }
    }

    private fun uiStateLoading() = with(binding) {
        tvInfo.visibility = View.GONE
        tvInfo.isEnabled = false
        progressBar.visibility = View.VISIBLE
    }

    private fun uiStateSuccess(cities: List<CityEntity>?) = with(binding) {
        if (cities == null) return@with
        tvInfo.visibility = View.GONE
        progressBar.visibility = View.INVISIBLE

        cityAdapter?.cities?.addAll(cities)
        cityAdapter?.notifyDataSetChanged()
    }

    private fun uiStateNoResult() = with(binding) {
        tvInfo.visibility = View.VISIBLE
        tvInfo.text = getString(R.string.msg_the_end)
        tvInfo.isEnabled = false
        progressBar.visibility = View.INVISIBLE
    }

    private fun uiStateNetworkError() = with(binding) {
        tvInfo.visibility = View.VISIBLE
        tvInfo.text = getString(R.string.msg_network_error)
        tvInfo.isEnabled = true
        tvInfo.setOnClickListener {
            getCitiesByPageNumber(sharedViewModel.getNextPageNumber())
        }
        progressBar.visibility = View.INVISIBLE
    }

    private fun getCitiesByPageNumber(pageNumber: Int = 1) =
        sharedViewModel.getCities(Query(QueryType.PAGE_NUMBER, pageNumber))

    private fun setUpRecyclerView() = with(binding.recyclerView) {
        val linearLayoutManager = LinearLayoutManager(requireContext())
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        layoutManager = linearLayoutManager
        cityAdapter = CityAdapter(requireContext(), mutableListOf()) { navigateToMap(it) }
        adapter = cityAdapter
        setHasFixedSize(true)
        addOnScrollListener(object : EndlessRecyclerViewScrollListener(linearLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                getCitiesByPageNumber(sharedViewModel.getNextPageNumber())
            }
        })
    }

    private fun setUpSearchView() = with(binding) {
        tvSearch.doAfterTextChanged {
            observeDebounce {
                handleSearchOperation(it.toString())
            }
        }

        tvSearchParent.setEndIconOnClickListener {
            tvSearch.text?.clear()
            getCitiesByPageNumber()
        }
    }

    private fun navigateToMap(cityEntity: CityEntity) {
        val latitude = cityEntity.lat?.toFloat()
        val longitude = cityEntity.lng?.toFloat()
        val cityName = cityEntity.name
        if (latitude == null || longitude == null || cityName == null) return
        val action = ListOfCitiesFragmentDirections.actionListOfCitiesFragmentToMapOfCitiesFragment(latitude, longitude, cityName)
        findNavController().navigate(action)
    }

    private fun observeDebounce(delay: Long = 1500, function: () -> Unit) = lifecycleScope.launch {
        delay(delay)
        function()
    }

    private fun handleSearchOperation(searchWord: String) {
        if (searchWord.isEmpty() || searchWord.isBlank()) {
            // for cases where the user cleared the search box manually:
            getCitiesByPageNumber()
        } else {
            cityAdapter?.cities?.clear()
            cityAdapter?.notifyDataSetChanged()
            sharedViewModel.getCities(Query(QueryType.CITY_NAME, searchWord))
        }
    }
}