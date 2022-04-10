package com.example.cities.util

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class EndlessRecyclerViewScrollListener(layoutManager: LinearLayoutManager) : RecyclerView.OnScrollListener() {
    private var mLayoutManager: RecyclerView.LayoutManager = layoutManager
    private val visibleThreshold = 5
    var page = 0
        private set
    private var previousTotalItemCount = 0
    private var loading = true
    private var startingPageIndex = 0

    override fun onScrolled(view: RecyclerView, dx: Int, dy: Int) {
        var lastVisibleItemPosition = 0
        val totalItemCount = mLayoutManager.itemCount
        lastVisibleItemPosition = (mLayoutManager as LinearLayoutManager).findLastVisibleItemPosition()
        if (totalItemCount < previousTotalItemCount) {
            page = startingPageIndex
            previousTotalItemCount = totalItemCount
            if (totalItemCount == 0) {
                loading = true
            }
        }
        if (loading && totalItemCount > previousTotalItemCount) {
            loading = false
            previousTotalItemCount = totalItemCount
        }
        if (!loading && lastVisibleItemPosition + visibleThreshold > totalItemCount) {
            page++
            onLoadMore(page, totalItemCount, view)
            loading = true
        }
    }

    fun resetState() {
        page = startingPageIndex
        previousTotalItemCount = 0
        loading = true
    }

    fun nextIndex(i: Int) {
        startingPageIndex = i
    }

    fun setCurrentPageIndex(i: Int) {
        page = i
    }

    abstract fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?)
}