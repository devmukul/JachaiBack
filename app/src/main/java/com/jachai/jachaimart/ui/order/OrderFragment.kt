package com.jachai.jachaimart.ui.order

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.jachai.jachaimart.R
import com.jachai.jachaimart.databinding.OrderFragmentBinding
import com.jachai.jachaimart.ui.base.BaseFragment

class OrderFragment : BaseFragment<OrderFragmentBinding>(R.layout.order_fragment) {

    companion object {
        fun newInstance() = OrderFragment()
    }

    private val viewModel: OrderViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        subscribeObservers()
    }

    override fun initView() {

    }

    override fun subscribeObservers() {

    }

}