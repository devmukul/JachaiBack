package com.jachai.jachaimart.ui.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.jachai.jachaimart.R
import com.jachai.jachaimart.databinding.FragmentRestaurantSearchBinding
import com.jachai.jachaimart.model.response.home.ShopsItem
import com.jachai.jachaimart.ui.base.BaseFragment
import com.jachai.jachaimart.ui.home.adapters.ShopRecyclerAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class GroceriesSearchFragment : BaseFragment<FragmentRestaurantSearchBinding>(R.layout.fragment_restaurant_search), ShopRecyclerAdapter.Interaction {

    companion object {
        fun newInstance() = GroceriesSearchFragment()
    }

    private val viewModel: GroceriesSearchViewModel by viewModels()
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
            searchGrocery("potato")
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