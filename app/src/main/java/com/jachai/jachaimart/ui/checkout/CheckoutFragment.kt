package com.jachai.jachaimart.ui.checkout

import androidx.fragment.app.viewModels
import com.jachai.jachaimart.R
import com.jachai.jachaimart.databinding.CheckoutFragmentBinding
import com.jachai.jachaimart.ui.base.BaseFragment

class CheckoutFragment : BaseFragment<CheckoutFragmentBinding>(R.layout.checkout_fragment) {

    companion object {
        fun newInstance() = CheckoutFragment()
    }

    private val viewModel: CheckoutViewModel by viewModels()
    override fun initView() {

    }

    override fun subscribeObservers() {

    }


}