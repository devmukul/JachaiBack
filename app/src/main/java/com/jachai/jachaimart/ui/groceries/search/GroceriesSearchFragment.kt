package com.jachai.jachaimart.ui.search

import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView.OnEditorActionListener
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.jachai.jachaimart.R
import com.jachai.jachaimart.databinding.FragmentProductSearchBinding
import com.jachai.jachaimart.model.response.category.Product
import com.jachai.jachaimart.model.response.home.ShopsItem
import com.jachai.jachaimart.ui.base.BaseFragment
import com.jachai.jachaimart.ui.groceries.search.adapter.PopularTagAdapter
import com.jachai.jachaimart.ui.groceries.search.adapter.SearchSuggetionAdapter
import com.jachai.jachaimart.ui.home.adapters.ShopRecyclerAdapter
import com.vikas.paging3.view.loader.adapter.LoaderDoggoImageAdapter
import com.vikas.paging3.view.loader.adapter.LoaderStateAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

@ExperimentalPagingApi
class GroceriesSearchFragment : BaseFragment<FragmentProductSearchBinding>(R.layout.fragment_product_search),
    ShopRecyclerAdapter.Interaction, PopularTagAdapter.Interaction, SearchSuggetionAdapter.Interaction, LoaderDoggoImageAdapter.Interaction {

    lateinit var loaderViewModel: GroceriesSearchViewModel
    lateinit var adapter: LoaderDoggoImageAdapter
    lateinit var loaderStateAdapter: LoaderStateAdapter
    private lateinit var navController: NavController

    var isLoading = true
    var searchJob: Job? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val policy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)

        initMembers()
        initView()
        subscribeObservers()
        loaderViewModel.searchPopularSearch()

    }

    private fun fetchDoggoImages(key: String) {
        binding.recyclerView.visibility = View.VISIBLE
        binding.popularSerachView.visibility = View.GONE
        binding.recentSerachView.visibility = View.GONE
        binding.searchSuggetionView.visibility = View.GONE
        lifecycleScope.launch {
            loaderViewModel.fetchDoggoImages(key).distinctUntilChanged().collectLatest {
                adapter.submitData(it)
            }
        }
    }

    private fun initMembers() {
        loaderViewModel = defaultViewModelProviderFactory.create(GroceriesSearchViewModel::class.java)
        adapter = LoaderDoggoImageAdapter(this)
        loaderStateAdapter = LoaderStateAdapter { adapter.retry() }
    }



    override fun initView() {

        binding.apply {
            etSearchShops.requestFocus()
            recyclerView.layoutManager = GridLayoutManager(context, 2)
            recyclerView.adapter = adapter.withLoadStateFooter(loaderStateAdapter)

            clearSearch.setOnClickListener { handleClearSearch() }


            icSearch.setOnClickListener {
                fetchDoggoImages(etSearchShops.text.toString())
            }

            searchSuggetionTitle.setOnClickListener {
                fetchDoggoImages(etSearchShops.text.toString())
            }

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
                        if(text.length>1) {
                            recyclerView.visibility = View.GONE
                            recentSerachView.visibility  = View.GONE
                            popularSerachView.visibility  = View.GONE
                            searchSuggetionView.visibility = View.VISIBLE
                            searchSuggetionTitle.text = "Search for $text"
                            delayAndProcessSearch(text)
                        }else{
                            recyclerView.visibility = View.GONE
                            recentSerachView.visibility  = View.GONE
                            popularSerachView.visibility  = View.VISIBLE
                            searchSuggetionView.visibility = View.GONE
                        }
                    }
                }
            })

            etSearchShops.setOnEditorActionListener(OnEditorActionListener { v, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    fetchDoggoImages(etSearchShops.text.toString())
                    return@OnEditorActionListener true
                }
                false
            })

        }

    }

    override fun subscribeObservers() {
        loaderViewModel.popularKeywordLiveData.observe(viewLifecycleOwner){

            if(it.popular.isNotEmpty()) {
                binding.popularSerachView.visibility = View.VISIBLE
                val layoutManager = FlexboxLayoutManager(requireContext())
                layoutManager.flexWrap = FlexWrap.WRAP
                layoutManager.flexDirection = FlexDirection.ROW
                layoutManager.alignItems = AlignItems.STRETCH
                binding.popularSerachRV.layoutManager = layoutManager
                val adapter = PopularTagAdapter(requireContext(), it.popular, this)
                binding.popularSerachRV.adapter = adapter
            }else{
                binding.popularSerachView.visibility = View.GONE
            }
        }

        loaderViewModel.searhKeywordLiveData.observe(viewLifecycleOwner){
            val searchSuggetionAdapter = SearchSuggetionAdapter(requireContext(), it.keys, this)
            binding.searchSuggetionRV.layoutManager = LinearLayoutManager(requireContext())
            binding.searchSuggetionRV.adapter = searchSuggetionAdapter
            searchSuggetionAdapter.notifyDataSetChanged()
        }
    }

    override fun onShopItemSelected(position: Int, item: ShopsItem) {
    }

    private fun handleClearSearch() {
        binding.etSearchShops.setText("")
        binding.recyclerView.visibility = View.GONE
        binding.popularSerachView.visibility = View.VISIBLE
        binding.recentSerachView.visibility = View.GONE
        binding.searchSuggetionView.visibility = View.GONE
    }

    override fun onItemSelected(product: String) {
        binding.etSearchShops.setText(product)
        fetchDoggoImages(product)
    }

    private fun delayAndProcessSearch(key: String) {
        searchJob?.cancel()
        searchJob = lifecycleScope.launch(Dispatchers.Main) {
            delay(300)
            processSearch(key)
        }
    }

    private fun processSearch(key: String) {
        loaderViewModel.apply {
            searchSuggetion(key)
        }
    }

    override fun onItemSelected(product: Product?) {
        val action =
            GroceriesSearchFragmentDirections.actionGroceriesSearchFragmentToProductDetailsFragment()

        action.productId = product?.slug
        navController.navigate(action)

    }
}