package com.jachai.jachaimart.ui.favourite

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import com.jachai.jachai_driver.utils.showShortToast
import com.jachai.jachaimart.R
import com.jachai.jachaimart.databinding.FavouriteFragmentBinding
import com.jachai.jachaimart.model.response.category.Product
import com.jachai.jachaimart.ui.base.BaseFragment
import com.jachai.jachaimart.ui.favourite.adapters.FavouriteProductAdapter

class FavouriteFragment :
    BaseFragment<FavouriteFragmentBinding>(R.layout.favourite_fragment),
    FavouriteProductAdapter.Interaction {

    companion object {
        fun newInstance() = FavouriteFragment()
    }

    private lateinit var navController: NavController
    private lateinit var favouriteProductAdapter: FavouriteProductAdapter
    private val viewModel: FavouriteViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)


        initView()
        subscribeObservers()

    }

    override fun initView() {
        viewModel.getAllFavouriteProducts()

        binding.apply {

            toolbarMain.title.text = "Favourite Products"
            toolbarMain.back.setOnClickListener {
                navController.popBackStack(R.id.groceriesShopFragment, true)
            }

            rvFavs.apply {
                layoutManager = GridLayoutManager(context, 2)
                favouriteProductAdapter =
                    FavouriteProductAdapter(requireContext(), emptyList(), this@FavouriteFragment)
                adapter = favouriteProductAdapter
            }
        }


    }

    override fun subscribeObservers() {
        viewModel.successSlugProductResponseLiveData.observe(viewLifecycleOwner) {
            favouriteProductAdapter.setList(it?.products as List<Product>?)
            favouriteProductAdapter.notifyDataSetChanged()

        }

        viewModel.errorSlugProductResponseLiveData.observe(viewLifecycleOwner) {
            when {
                it.equals("failed") -> {
                    showShortToast("Failed Network response")
                }
                it.equals("empty") -> {
                    showShortToast("No product found")

                }
            }
        }

    }

    override fun onProductSelected(product: Product?) {
        if (product != null) {
            val action =
                FavouriteFragmentDirections.actionFavouriteFragmentToProductDetailsFragment()
            action.productId = product.slug
            navController.navigate(action)
        }

    }

}