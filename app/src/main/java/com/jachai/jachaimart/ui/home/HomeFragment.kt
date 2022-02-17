package com.jachai.jachaimart.ui.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import bd.com.evaly.ehealth.models.common.CurrentLocation
import com.jachai.jachaimart.JachaiApplication
import com.jachai.jachaimart.R
import com.jachai.jachaimart.databinding.FragmentHomeBinding
import com.jachai.jachaimart.model.response.address.Address
import com.jachai.jachaimart.model.response.home.CategoriesItem
import com.jachai.jachaimart.model.response.home.ShopsItem
import com.jachai.jachaimart.ui.base.BaseFragment
import com.jachai.jachaimart.ui.home.adapters.BannerAdapter
import com.jachai.jachaimart.ui.home.adapters.CategoryAdapter
import com.jachai.jachaimart.ui.home.adapters.DetailsShopRecyclerAdapter
import com.jachai.jachaimart.ui.home.adapters.ShopRecyclerAdapter
import com.jachai.jachaimart.utils.SharedPreferenceUtil
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

    override fun onResume() {
        super.onResume()
        updateCartData()
    }

    private fun updateCartData() {
        binding.cartBadge.text =
            JachaiApplication.mDatabase.daoAccess().getProductOrdersSize().toString()
    }

    override fun initView() {
        viewModel.requestForBanners(CommonConstants.JC_FOOD_TYPE)
        viewModel.requestForCategories()
        loadShopByLocation()

        binding.apply {
            back.setOnClickListener {
                navController.popBackStack()

            }
            frameLay.setOnClickListener {
                val action =
                    if (JachaiApplication.mDatabase.daoAccess().getProductOrdersSize() == 0) {
                        HomeFragmentDirections.actionNavHomeToEmptyCartFragment()
                    } else {
                        HomeFragmentDirections.actionNavHomeToCartFragment()
                    }
                navController.navigate(action)

            }

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

    private fun loadShopByLocation() {
        if (SharedPreferenceUtil.isConfirmDeliveryAddress()) {
            SharedPreferenceUtil.getDeliveryAddress().let { it1 ->
                if (it1 != null) {
                    loadRestaurantFromDeliveryLocation(it1)
                } else {
                    loadRestaurantFromCurrentLocation()
                }
            }
        } else {
            val address = SharedPreferenceUtil.getCurrentAddress()
            if (address != null) {
                loadRestaurantFromDeliveryLocation(address)
            } else {
                loadRestaurantFromCurrentLocation()
            }
        }

    }

    private fun loadRestaurantFromCurrentLocation() {
        fetchCurrentLocation { location: CurrentLocation? ->
            if (location != null) {
                viewModel.requestForRestaurantAroundYou(
                    location.latitude,
                    location.longitude

                )
                showLoader()
            }
        }
    }

    fun loadRestaurantFromDeliveryLocation(address: Address) {
        viewModel.requestForRestaurantAroundYou(
            address.location.latitude,
            address.location.longitude

        )
        showLoader()

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
            dismissLoader()
            val count = if (it?.shops?.size == null) {
                0
            } else {
                it.shops.size
            }
            if (count == 0) {
                val action =
                    HomeFragmentDirections.actionNavHomeToSericeNotFoundFragment()
                action.type = CommonConstants.JC_FOOD_TYPE
                navController.navigate(action)

            }
            shopRecyclerAdapter.setList(it?.shops)
            shopRecyclerAdapter.notifyDataSetChanged()



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