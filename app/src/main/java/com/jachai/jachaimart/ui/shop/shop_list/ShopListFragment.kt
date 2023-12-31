package com.jachai.jachaimart.ui.shop.shop_list

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jachai.jachaimart.R
import com.jachai.jachaimart.databinding.ShopListFragmentBinding
import com.jachai.jachaimart.model.response.home.ShopsItem
import com.jachai.jachaimart.ui.base.BaseFragment
import com.jachai.jachaimart.ui.home.adapters.DetailsShopRecyclerAdapter
import com.jachai.jachaimart.ui.home.adapters.ShopRecyclerAdapter

class ShopListFragment : BaseFragment<ShopListFragmentBinding>(R.layout.shop_list_fragment),
    ShopRecyclerAdapter.Interaction {

    companion object {
        fun newInstance() = ShopListFragment()
    }

    private lateinit var categoryId: String
    private lateinit var categoryName: String

    private lateinit var navController: NavController

    private lateinit var detailsShopRecyclerAdapter: DetailsShopRecyclerAdapter


    private val args: ShopListFragmentArgs by navArgs()
    private val viewModel: ShopListViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        categoryId = args.categoryId
        categoryName = args.categoryName?: " "

        initView()
        subscribeObservers()
    }

    override fun initView() {

        fetchCurrentLocation {
            viewModel.requestForShopsByCategories(
                categoryId,
                it?.latitude,
                it?.longitude
            )
        }

        binding.apply {
            include3.topTitle.text = categoryName
            include3.back.setOnClickListener {
                navController.popBackStack()
            }

            title.text = " "
            rvShops.apply {
                layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                detailsShopRecyclerAdapter =
                    DetailsShopRecyclerAdapter(requireContext(), emptyList(), this@ShopListFragment)
                adapter = detailsShopRecyclerAdapter
            }
        }


    }

    override fun subscribeObservers() {
        viewModel.successShopsByCategoryResponseLiveData.observe(viewLifecycleOwner) {
            val count = it?.shops?.size ?: 0

            binding.title.text =
                "$count Restaurants Delivering $categoryName"
            detailsShopRecyclerAdapter.setList(it?.shops)
            detailsShopRecyclerAdapter.notifyDataSetChanged()

        }

    }

    override fun onShopItemSelected(position: Int, item: ShopsItem) {
        val action = ShopListFragmentDirections.actionShopListFragmentToShopFragment(item)
        navController.navigate(action)
    }

}