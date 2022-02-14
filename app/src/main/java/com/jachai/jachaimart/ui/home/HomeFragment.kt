package com.jachai.jachaimart.ui.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import bd.com.evaly.ehealth.models.common.CurrentLocation
import com.jachai.jachaimart.R
import com.jachai.jachaimart.databinding.FragmentHomeBinding
import com.jachai.jachaimart.model.response.home.CategoriesItem
import com.jachai.jachaimart.model.response.home.ShopsItem
import com.jachai.jachaimart.ui.base.BaseFragment
import com.jachai.jachaimart.ui.home.adapters.BannerAdapter
import com.jachai.jachaimart.ui.home.adapters.CategoryAdapter
import com.jachai.jachaimart.ui.home.adapters.DetailsShopRecyclerAdapter
import com.jachai.jachaimart.ui.home.adapters.ShopRecyclerAdapter
import com.jachai.jachaimart.utils.constant.CommonConstants

class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home),
    CategoryAdapter.Interaction, ShopRecyclerAdapter.Interaction {

    private val viewModel: HomeViewModel by viewModels()

    private lateinit var bannerAdapter: BannerAdapter
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var shopRecyclerAdapter: ShopRecyclerAdapter
    private lateinit var detailsShopRecyclerAdapter: DetailsShopRecyclerAdapter

    private lateinit var navController: NavController
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        subscribeObservers()
        navController = Navigation.findNavController(binding.root)


    }

    override fun initView() {
        viewModel.requestForBanners(CommonConstants.JC_FOOD_TYPE)
        viewModel.requestForCategories()

        fetchCurrentLocation { location: CurrentLocation? ->
            if (location != null) {
                viewModel.requestForRestaurantAroundYou(
                    location.latitude,
                    location.longitude

                )
            }
        }


        binding.apply {
            recyclerView.apply {
                layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
                bannerAdapter =
                    BannerAdapter(requireContext(), emptyList())
                adapter = bannerAdapter
            }


            include4.categoryTitle.text = getString(R.string.home_category_title)
            include4.recyclerView.apply {
                layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
                categoryAdapter =
                    CategoryAdapter(requireContext(), emptyList(), this@HomeFragment)
                adapter = categoryAdapter
            }

            include2.categoryShopTitle.text = "Your Restaurants"
            include2.recyclerView.apply {
                layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
                shopRecyclerAdapter =
                    ShopRecyclerAdapter(requireContext(), emptyList(), this@HomeFragment)
                adapter = shopRecyclerAdapter
            }


            title.text = "Restaurants around you"
            recyclerView2.apply {
                layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                detailsShopRecyclerAdapter =
                    DetailsShopRecyclerAdapter(requireContext(), emptyList(), this@HomeFragment)
                adapter = detailsShopRecyclerAdapter
            }


        }


    }

    override fun subscribeObservers() {

        viewModel.successBannerResponseLiveData.observe(viewLifecycleOwner) {
            bannerAdapter.setList(it?.banners)
            bannerAdapter.notifyDataSetChanged()
        }

        viewModel.successCategoryResponseLiveData.observe(viewLifecycleOwner) {
            categoryAdapter.setList(it?.categories)
            categoryAdapter.notifyDataSetChanged()
        }

        viewModel.successRestaurantAroundYouResponseLiveData.observe(viewLifecycleOwner) {
            shopRecyclerAdapter.setList(it?.shops)
            shopRecyclerAdapter.notifyDataSetChanged()

            val count = if (it?.shops?.size == null) {
                0
            } else {
                it.shops.size
            }
            binding.title.text = "$count Restaurants around you"
            detailsShopRecyclerAdapter.setList(it?.shops)
            detailsShopRecyclerAdapter.notifyDataSetChanged()

        }
    }

    override fun onCategoryItemSelected(position: Int, item: CategoriesItem?) {

        val action = HomeFragmentDirections.actionNavHomeToShopListFragment()
        action.categoryId = item?.id.toString()
        action.categoryName = item?.title.toString()
        navController.navigate(action)
    }

    override fun onShopItemSelected(position: Int, item: ShopsItem) {
        val action = HomeFragmentDirections.actionNavHomeToShopFragment(item)
        navController.navigate(action)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }




}