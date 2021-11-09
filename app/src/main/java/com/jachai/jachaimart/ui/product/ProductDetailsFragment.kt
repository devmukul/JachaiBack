package com.jachai.jachaimart.ui.product

import android.app.AlertDialog
import android.graphics.Paint
import android.os.Bundle
import android.view.View
import androidx.core.app.ShareCompat
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.jachai.jachai_driver.utils.JachaiLog
import com.jachai.jachai_driver.utils.showShortToast
import com.jachai.jachaimart.JachaiFoodApplication
import com.jachai.jachaimart.R
import com.jachai.jachaimart.databinding.ProductDetailsFragmentBinding
import com.jachai.jachaimart.model.response.product.Product
import com.jachai.jachaimart.ui.base.BaseFragment

class ProductDetailsFragment :
    BaseFragment<ProductDetailsFragmentBinding>(R.layout.product_details_fragment) {

    companion object {
        fun newInstance() = ProductDetailsFragment()
    }

    private val viewModel: ProductDetailsViewModel by viewModels()

    private lateinit var productSlug: String

    private lateinit var navController: NavController

    private val args: ProductDetailsFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        productSlug = args.productId.toString()
        initView()
        subscribeObservers()
    }

    override fun initView() {
        viewModel.requestForProductDetails(productSlug)
        showLoader()

        binding.apply {

            back.setOnClickListener {
                navController.popBackStack()
            }

            frameLay.setOnClickListener {

                val action =
                    if (JachaiFoodApplication.mDatabase.daoAccess().getProductOrdersSize() == 0) {
                        ProductDetailsFragmentDirections.actionProductDetailsFragmentToEmptyCartFragment()
                    } else {
                        ProductDetailsFragmentDirections.actionProductDetailsFragmentToCartFragment()
                    }
                navController.navigate(action)
            }


        }
    }

    override fun onResume() {
        super.onResume()

    }

    override fun subscribeObservers() {
        viewModel.successProductDetailsResponseLiveData.observe(
            viewLifecycleOwner,
            { productDetailsResponse ->
                productDetailsResponse?.let {
                    showProduct(productDetailsResponse.product)
                    dismissLoader()

                }
            })

        viewModel.successAddToCartData.observe(viewLifecycleOwner, {
            if (it == true) {
                binding.apply {
                    cartBadge.text =
                        JachaiFoodApplication.mDatabase.daoAccess().getProductOrdersSize()
                            .toString()

                    showShortToast("Product Added")


                }
            }

        })

        viewModel.errorResponseLiveData.observe(viewLifecycleOwner, {
            when{
                it.equals("failed")->{
                    dismissLoader()
                }
            }
        })

    }


    private fun alertDialog(product: Product, quantity: Int) {
        val builder = AlertDialog.Builder(context)
        builder.setCancelable(false)
        builder.setTitle(getString(R.string.app_name_short) + " alert")
        builder.setMessage("Do have already selected products from a different restaurant. if you continue, your cart and selection will be removed")

        builder.setPositiveButton("Continue") { _, _ ->

            viewModel.saveProduct(product, quantity, product.shop, false)
        }

        builder.setNegativeButton("Close") { dialog, which ->
            dialog.dismiss()
        }
        builder.show()
    }

    private fun showProduct(
        product: Product?
    ) {
        binding.apply {
            toolbarTItle.text = product?.name ?: " "
            name.text = product?.name ?: " "
            Glide.with(requireContext())
                .load(product?.productImage)
                .into(image)

            descriptionBody.text = product?.description

            val mPrice = product?.variations?.get(0)?.price?.mrp ?: 0.0
            val mDiscountedPrice = product?.variations?.get(0)?.price?.discountedPrice ?: 0.0


            if (mDiscountedPrice != 0.0 && mDiscountedPrice < mPrice) {
                price.text = "৳${mDiscountedPrice.toFloat()}"
                oldPrice.text = "৳${mPrice.toFloat()}"
                oldPrice.paintFlags = oldPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                price.text = "৳${mPrice.toFloat()}"
                oldPrice.text = "৳${mDiscountedPrice.toFloat()}"
                oldPrice.visibility = View.GONE
            }

            if (product?.variations?.get(0)?.productDiscount?.flat!! > 0 || product.variations[0]?.productDiscount?.percentage!! > 0) {
                if (product.variations[0]?.productDiscount?.flat!! > 0) {
                    discount.text = "Flat ৳${product.variations[0]?.productDiscount?.flat!!} OFF"
                } else {
                    if (product.variations[0]?.productDiscount?.percentage!! > 0) {
                        discount.text = "${product.variations[0]?.productDiscount?.percentage}% OFF"
                    }
                }

            } else {
                discount.visibility = View.GONE
            }


            val rateNumCount = product?.numberOfRating ?: 0
            rateCount.text = rateNumCount.toInt().toString()

            val count = product?.rating ?: 0
            rateText.text = count.toFloat().toString()

            ratingBar.rating = count.toFloat()

            share.setOnClickListener {
                ShareCompat.IntentBuilder.from(requireActivity())
                    .setType("text/plain")
                    .setChooserTitle("Share via")
                    .setText("https://jachai.com/products/${product?.slug}")
                    .startChooser()
            }


            var quantity = 1
            ivAdd.setOnClickListener {
                val count = icCount.text.toString().toInt()
                val addCount = count + 1
                val price = product?.variations?.get(0)?.price?.mrp?.times(addCount)
                quantity = addCount
                icCount.text = addCount.toString()
//                productPrice?.text = price.toString()

            }


            icSub.setOnClickListener {
                val count = icCount.text.toString().toInt()
                val addCount = count - 1
                val finalCount = if (addCount <= 0) {
                    1
                } else {
                    addCount
                }
                val price = product?.variations?.get(0)?.price?.mrp?.times(finalCount)
                quantity = finalCount
                icCount.text = finalCount.toString()
//                productPrice?.text = price.toString()


            }


            addToCart.setOnClickListener {
                JachaiLog.d("SHOP", quantity.toString())
                product?.let { it1 ->

                    if (product.shop?.id?.let {
                            JachaiFoodApplication.mDatabase.daoAccess()
                                .isInsertionApplicable(shopID = it)
                        } == true) {
//

                        viewModel.saveProduct(it1, quantity, product.shop, true)

                    } else {
                        alertDialog(product, quantity)
                    }


                }

            }


        }
    }


}