package com.jachai.jachaimart.ui.search

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.jachai.jachaimart.R
import com.jachai.jachaimart.databinding.FragmentHomeBinding
import com.jachai.jachaimart.databinding.FragmentRestaurantSearchBinding
import com.jachai.jachaimart.model.response.home.CategoriesItem
import com.jachai.jachaimart.model.response.home.ShopsItem
import com.jachai.jachaimart.ui.base.BaseFragment
import com.jachai.jachaimart.ui.home.HomeFragmentDirections
import com.jachai.jachaimart.ui.home.adapters.BannerAdapter
import com.jachai.jachaimart.ui.home.adapters.CategoryAdapter
import com.jachai.jachaimart.ui.home.adapters.DetailsShopRecyclerAdapter
import com.jachai.jachaimart.ui.home.adapters.ShopRecyclerAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class RestaurantSearchFragment : BaseFragment<FragmentRestaurantSearchBinding>(R.layout.fragment_restaurant_search), ShopRecyclerAdapter.Interaction {

    companion object {
        fun newInstance() = RestaurantSearchFragment()
    }

    private val viewModel: RestaurantSearchViewModel by viewModels()
    var isLoading = true
    var searchJob: Job? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        subscribeObservers()

    }

    override fun initView() {

        binding.apply {
            setAdapter()
            clearSearch.setOnClickListener { handleClearSearch() }

            etSearchShops.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) = Unit

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) = Unit

                override fun onTextChanged(
                    searchText: CharSequence?,
                    start: Int,
                    before: Int,
                    count: Int
                ) {
                    if (searchText != null) {
                        clearSearch.isVisible = searchText.toString().isNotEmpty()
                        val text = searchText.toString()
                        viewModel.searchText = if (text.length > 2) text else ""
                        delayAndProcessSearch()
                    }
                }
            })
        }

    }

    override fun subscribeObservers() {
    }

    override fun onShopItemSelected(position: Int, item: ShopsItem) {
        TODO("Not yet implemented")
    }

    private fun handleClearSearch() {
        binding.etSearchShops.setText("")
        viewModel.searchText = ""
        processSearch()
    }

    private fun delayAndProcessSearch() {
        searchJob?.cancel()
        searchJob = lifecycleScope.launch(Dispatchers.Main) {
            delay(300)
            processSearch()
        }
    }

    private fun processSearch(isDocListShowing: Boolean? = null) {
        viewModel.apply {
            isDocListShowing?.let { isRestaurantListShowing = it }
            clear()
            enableLoading()
//            searchDoctorOrCategory()
        }
    }

    private fun enableLoading() {
//        isLoading = true
//        updateController(loading = viewModel.getLoadingState())
    }

//    private fun updateController(
//        loading: LoadingState = LoadingState(),
//        doctorList: List<ShopsItem>? = null
//    ) {
//        controller.apply {
//            isDoctorListShowing = viewModel.isDoctorListShowing
//            loadingState = loading
//            totalDataCount = viewModel.totalDataCount
//            doctorList?.let { docList = it }
//            catList?.let { categoryList = it }
//            requestModelBuild()
//        }
//    }

    private fun setAdapter() {
//        binding.rvSearchDoctor.apply {
//            adapter = controller.adapter
//            layoutManager = LinearLayoutManager(context)
//            isSaveEnabled = true
//
//            PaginateRecyclerview(this, layoutManager) {
//                if (!isLoading && !viewModel.isAllDataFetched()) {
//                    enableLoading()
//                    viewModel.searchDoctorOrCategory()
//                }
//            }
//        }
    }

}