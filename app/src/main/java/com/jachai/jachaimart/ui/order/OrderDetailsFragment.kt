package com.jachai.jachaimart.ui.order

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.jachai.jachaimart.R
import com.jachai.jachaimart.databinding.OrderDetailsFragmentBinding
import com.jachai.jachaimart.ui.base.BaseFragment

class OrderDetailsFragment :
    BaseFragment<OrderDetailsFragmentBinding>(R.layout.order_details_fragment) {

    companion object {
        fun newInstance() = OrderDetailsFragment()
    }

    private val args: OrderDetailsFragmentArgs by navArgs()

    private val viewModel: OrderDetailsViewModel by viewModels()
    private lateinit var orderId: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        orderId = args.orderId

    }

    override fun initView() {


    }

    override fun subscribeObservers() {
    }

}