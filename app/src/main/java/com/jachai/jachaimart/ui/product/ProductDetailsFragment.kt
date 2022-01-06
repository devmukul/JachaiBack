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
import com.jachai.jachaimart.JachaiApplication
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
            cartBadge.text =
                JachaiApplication.mDatabase.daoAccess().getProductOrdersSize()
                    .toString()
            back.setOnClickListener {
                navController.popBackStack()
            }
            updateBottomCart()

            frameLay.setOnClickListener {

                val action =
                    if (JachaiApplication.mDatabase.daoAccess().getProductOrdersSize() == 0) {
                        ProductDetailsFragmentDirections.actionProductDetailsFragmentToEmptyCartFragment()
                    } else {
                        ProductDetailsFragmentDirections.actionProductDetailsFragmentToCartFragment()
                    }
                navController.navigate(action)
            }

            clCart.setOnClickListener {
                val action =
                    if (JachaiApplication.mDatabase.daoAccess().getProductOrdersSize() == 0) {
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
        if (JachaiApplication.mDatabase.daoAccess().getProductOrdersSize()>0){
            binding.bottomCart.visibility = View.VISIBLE
        }else{
            binding.bottomCart.visibility = View.GONE
        }

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
            binding.apply {
                if (it == true) {

                    cartBadge.text =
                        JachaiApplication.mDatabase.daoAccess().getProductOrdersSize()
                            .toString()


                }
            }

        })

        viewModel.errorResponseLiveData.observe(viewLifecycleOwner, {
            when {
                it.equals("failed") -> {
                    dismissLoader()
                }
            }
        })

        viewModel.successFavouriteProductResponseLiveData.observe(viewLifecycleOwner) {
            dismissLoader()
            showShortToast("Success")
        }

        viewModel.errorFavouriteProductResponseLiveData.observe(viewLifecycleOwner) {
            when {
                it.equals("failed") -> {
                    dismissLoader()
                }
            }
        }


    }


    private fun alertDialog(product: Product, quantity: Int) {
        val builder = AlertDialog.Builder(context)
        builder.setCancelable(false)
        builder.setTitle(getString(R.string.app_name_short) + " alert")
        builder.setMessage("Do have already selected products from a different shop. if you continue, your cart and selection will be removed")

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
                .placeholder(R.drawable.ic_place_holder)
                .error(R.drawable.ic_place_holder)
                .into(image)


            if (product?.description != null) {


                descriptionBody.setOnLongClickListener(object : View.OnLongClickListener {
                    override fun onLongClick(p0: View?): Boolean {
                        return true
                    }
                })
                descriptionBody.isLongClickable = false
                descriptionBody.loadData(
                    """
                <!doctype html>
                <html>
                <head>
                <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
                </head>
                <body style="margin: 0; padding: 0">
                <div>
                ${product?.description!!}
                </div>
                </body>
                </html>
                 """.trimIndent(), "text/html", "UTF-8"
                )
            } else {
                descriptionBody.visibility = View.GONE
            }
//
//
//            val htmlAsSpanned = Html.fromHtml("<html>${product?.description}</html>")

//            descriptionBody.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                Html.fromHtml("<html>${product?.description}</html>", Html.FROM_HTML_MODE_COMPACT)
//            } else {
//                Html.fromHtml("<html>${product?.description}</html>")
//            }

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

            try {
                if (product?.variations?.get(0)?.productDiscount?.flat!! > 0 || product.variations[0]?.productDiscount?.percentage!! > 0) {
                    if (product.variations[0]?.productDiscount?.flat!! > 0) {
                        discount.text =
                            "Flat ৳${product.variations[0]?.productDiscount?.flat!!} OFF"
                    } else {
                        if (product.variations[0]?.productDiscount?.percentage!! > 0) {
                            discount.text =
                                "${product.variations[0]?.productDiscount?.percentage}% OFF"
                        }
                    }

                } else {
                    discount.visibility = View.GONE
                }
            } catch (ex: Exception) {
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
                    .setText("https://jachai.com/product/${product?.slug}")
                    .startChooser()
            }

            favorite.isChecked = product?.slug?.let { viewModel.isProductFavourite(it) } == false


            favorite.setOnCheckedChangeListener { p0, isChecked ->
                if (isChecked) {
                    product?.slug?.let {
                        showLoader()
                        viewModel.requestForSetFavouriteProduct(slug = it)

                    }
                } else {
                    product?.slug?.let {
                        showLoader()
                        viewModel.requestForRemoveFavouriteProduct(slug = it)
                    }
                }
            }

            if (product?.variations?.get(0)?.stock ?: 0 > 0) {
                conlay21.visibility = View.VISIBLE
                conlay22.visibility = View.GONE

            } else {
                conlay21.visibility = View.GONE
                conlay22.visibility = View.VISIBLE

            }



            val mCount = JachaiApplication.mDatabase.daoAccess()
                .getProductOrderCount(product?.id.toString())
            if (mCount >0){
                icCount.text = mCount.toString()
            }

            var quantity = icCount.text.toString().toInt()
            ivAdd.setOnClickListener {
                val count = icCount.text.toString().toInt()
                val addCount = count + 1
                val price = product?.variations?.get(0)?.price?.mrp?.times(addCount)

//                productPrice?.text = price.toString()
                val finalCount = if (addCount <= product?.variations?.get(0)?.stock?.toInt() ?: 0) {
                    addCount
                } else {
                    showShortToast("Max limit reached")
                    product?.variations?.get(0)?.stock?.toInt() ?: 0
                }
                quantity = finalCount
                icCount.text = finalCount.toString()


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
                            JachaiApplication.mDatabase.daoAccess()
                                .isInsertionApplicable(shopID = it)
                        } == true) {
//
                        if (JachaiApplication.mDatabase.daoAccess()
                                .getProductByProductID(product.id!!, product.shop.id) > 0
                        ) {

                            showShortToast("Product already added")

                        } else {

                            showShortToast("Product added")
                        }
                        viewModel.saveProduct(it1, quantity, product.shop, true)

                    } else {
                        alertDialog(product, quantity)
                    }


                }
                updateBottomCart()

            }


        }
    }

    private fun updateBottomCart() {
        binding.apply {
            if (JachaiApplication.mDatabase.daoAccess().getProductOrdersSize()>0){
                binding.bottomCart.visibility = View.VISIBLE
            }else{
                binding.bottomCart.visibility = View.GONE
            }
            itemCount.text =
                JachaiApplication.mDatabase.daoAccess().getProductOrdersSize()
                    .toString()
            totalCount.text =
                JachaiApplication.mDatabase.daoAccess().getProductOrderDiscountedSubtotal()
                    .toString()


        }

    }


}