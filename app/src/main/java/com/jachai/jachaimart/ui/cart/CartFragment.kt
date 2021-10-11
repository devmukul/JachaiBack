package com.jachai.jachaimart.ui.cart

import androidx.fragment.app.viewModels
import com.jachai.jachaimart.R
import com.jachai.jachaimart.databinding.CartFragmentBinding
import com.jachai.jachaimart.ui.base.BaseFragment

class CartFragment : BaseFragment<CartFragmentBinding>(R.layout.cart_fragment) {

    companion object {
        fun newInstance() = CartFragment()
    }


    private val viewModel: CartViewModel by viewModels()

    override fun initView() {
        TODO("Not yet implemented")
    }

    override fun subscribeObservers() {
        TODO("Not yet implemented")
    }

}