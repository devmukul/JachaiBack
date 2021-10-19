package com.jachai.jachaimart.ui.search

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jachai.jachaimart.R
import com.jachai.jachaimart.databinding.FragmentHomeBinding
import com.jachai.jachaimart.model.response.home.CategoriesItem
import com.jachai.jachaimart.model.response.home.ShopsItem
import com.jachai.jachaimart.ui.base.BaseFragment
import com.jachai.jachaimart.ui.home.HomeFragmentDirections
import com.jachai.jachaimart.ui.home.adapters.BannerAdapter
import com.jachai.jachaimart.ui.home.adapters.CategoryAdapter
import com.jachai.jachaimart.ui.home.adapters.DetailsShopRecyclerAdapter
import com.jachai.jachaimart.ui.home.adapters.ShopRecyclerAdapter

class RestaurantSearchFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home), ShopRecyclerAdapter.Interaction {

    companion object {
        fun newInstance() = RestaurantSearchFragment()
    }

    private val viewModel: RestaurantSearchViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        subscribeObservers()

    }

    override fun initView() {

    }

    override fun subscribeObservers() {
    }

    override fun onShopItemSelected(position: Int, item: ShopsItem) {
        TODO("Not yet implemented")
    }

}