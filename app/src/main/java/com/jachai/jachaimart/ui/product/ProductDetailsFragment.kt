package com.jachai.jachaimart.ui.product

import androidx.fragment.app.viewModels
import com.jachai.jachaimart.R
import com.jachai.jachaimart.databinding.ProductDetailsFragmentBinding
import com.jachai.jachaimart.ui.base.BaseFragment

class ProductDetailsFragment :
    BaseFragment<ProductDetailsFragmentBinding>(R.layout.product_details_fragment) {

    companion object {
        fun newInstance() = ProductDetailsFragment()
    }

    private val viewModel: ProductDetailsViewModel by viewModels()


    override fun initView() {

    }

    override fun subscribeObservers() {

    }

}