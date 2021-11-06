package com.jachai.jachaimart.ui.order

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jachai.jachaimart.R
import com.jachai.jachaimart.databinding.OrderFragmentBinding
import com.jachai.jachaimart.ui.base.BaseFragment
import com.jachai.jachaimart.ui.order.adapter.OrderViewRowAdapter

class OrderFragment : BaseFragment<OrderFragmentBinding>(R.layout.order_fragment) {

    companion object {
        fun newInstance() = OrderFragment()
    }

    private val viewModel: OrderViewModel by viewModels()
    private lateinit var orderViewOnGoingAdapter: OrderViewRowAdapter
    private lateinit var orderViewOnCompletedAdapter: OrderViewRowAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        subscribeObservers()
    }

    override fun initView() {
        viewModel.getAllOrder()

        binding.apply {
            rvOnGoingOrder.apply {
                layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                orderViewOnGoingAdapter = OrderViewRowAdapter(requireContext(), emptyList())
                adapter = orderViewOnGoingAdapter
            }

            rvOrderCompleted.apply {
                layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                orderViewOnCompletedAdapter = OrderViewRowAdapter(requireContext(), emptyList())
                adapter = orderViewOnCompletedAdapter
            }
        }


    }

    override fun subscribeObservers() {
        viewModel.successOnGoingOrderListLiveData.observe(viewLifecycleOwner) {
            orderViewOnGoingAdapter.setList(it)
            orderViewOnGoingAdapter.notifyDataSetChanged()
        }

        viewModel.successPreviousOrderListLiveData.observe(viewLifecycleOwner) {
            orderViewOnCompletedAdapter.setList(it)
            orderViewOnCompletedAdapter.notifyDataSetChanged()
        }


    }

}