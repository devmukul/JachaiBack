package com.jachai.jachaimart.ui.favourite

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import com.jachai.jachai_driver.utils.showShortToast
import com.jachai.jachaimart.JachaiApplication
import com.jachai.jachaimart.R
import com.jachai.jachaimart.databinding.FavouriteFragmentBinding
import com.jachai.jachaimart.model.response.product.Product
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
                onBackPressed()
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

    override fun onProductAddToCartX(product: Product?, quantity: Int) {
        product?.let { it1 ->

            if (product.hub?.id?.let {
                    JachaiApplication.mDatabase.daoAccess()
                        .isInsertionApplicableByHubId(hubId = it)
                } == true) {
//
                if (JachaiApplication.mDatabase.daoAccess()
                        .getProductByProductIDByHub(product.id!!, product.hub.id) > 0
                ) {

                    showShortToast("Product already added")

                } else {

                    showShortToast("Product added")
                }
                viewModel.saveProductByHub(it1, quantity, product.hub, true)

            } else {
                alertDialog(product, quantity)
            }
            favouriteProductAdapter.notifyDataSetChanged()


        }


        viewModel.successAddToCartData.observe(viewLifecycleOwner, {
            binding.apply {
                if (it == true) {



                }
            }

        })
    }

    private fun alertDialog(product: Product, quantity: Int) {
        val builder = AlertDialog.Builder(context)
        builder.setCancelable(false)
        builder.setTitle(getString(R.string.app_name_short) + " alert")
        builder.setMessage("Do have already selected products from a different shop. if you continue, your cart and selection will be removed")

        builder.setPositiveButton("Continue") { _, _ ->

            viewModel.saveProductByHub(product, quantity, product.hub, false)
        }

        builder.setNegativeButton("Close") { dialog, which ->
            dialog.dismiss()
        }
        builder.show()
    }


}