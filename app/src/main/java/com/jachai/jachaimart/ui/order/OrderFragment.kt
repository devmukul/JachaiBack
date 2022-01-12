package com.jachai.jachaimart.ui.order

import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jachai.jachai_driver.utils.showShortToast
import com.jachai.jachaimart.R
import com.jachai.jachaimart.databinding.OrderFragmentBinding
import com.jachai.jachaimart.model.order.base_order.BaseOrder
import com.jachai.jachaimart.ui.base.BaseFragment
import com.jachai.jachaimart.ui.order.adapter.OrderViewRowAdapter

class OrderFragment : BaseFragment<OrderFragmentBinding>(R.layout.order_fragment),
    OrderViewRowAdapter.Interaction {

    companion object {
        fun newInstance() = OrderFragment()
    }

    private lateinit var navController: NavController


    private val viewModel: OrderViewModel by viewModels()
    private lateinit var orderViewOnGoingAdapter: OrderViewRowAdapter
    private lateinit var orderViewOnCompletedAdapter: OrderViewRowAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        initView()
        subscribeObservers()

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            val action = OrderFragmentDirections.actionOrderFragmentToGroceriesShopFragment()
            navController.navigate(action)
        }
    }

    override fun initView() {
        viewModel.getAllOrder()
        showLoader()

        binding.apply {
            toolbarMain.title.text = "Orders"
            toolbarMain.back.setOnClickListener {
                val action = OrderFragmentDirections.actionOrderFragmentToGroceriesShopFragment()
                navController.navigate(action)
            }

            rvOnGoingOrder.apply {
                layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                orderViewOnGoingAdapter =
                    OrderViewRowAdapter(requireContext(), emptyList(), true, this@OrderFragment)
                adapter = orderViewOnGoingAdapter
            }

            rvOrderCompleted.apply {
                layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                orderViewOnCompletedAdapter =
                    OrderViewRowAdapter(requireContext(), emptyList(), false, this@OrderFragment)
                adapter = orderViewOnCompletedAdapter
            }
        }


    }

    override fun subscribeObservers() {

        viewModel.successOnGoingOrderListLiveData.observe(viewLifecycleOwner) {
            dismissLoader()

            orderViewOnGoingAdapter.setList(it)
            orderViewOnGoingAdapter.notifyDataSetChanged()
        }

        viewModel.successPreviousOrderListLiveData.observe(viewLifecycleOwner) {
            orderViewOnCompletedAdapter.setList(it)
            orderViewOnCompletedAdapter.notifyDataSetChanged()
        }
        viewModel.errorOrderDetailsLiveData.observe(viewLifecycleOwner) {
            dismissLoader()
        }


    }

    override fun onOrderSelected(order: BaseOrder?, isOnGoingOrder: Boolean) {
        if (order != null) {
            if (isOnGoingOrder) {
//                val action = OrderFragmentDirections.actionOrderFragmentToOnGoingOrderFragment(order.orderId)
//                navController.navigate(action)
                val action = OrderFragmentDirections.actionOrderFragmentToMultiOrderPackFragment()
                action.orderID = order.baseOrderId
                navController.navigate(action)

            } else {
                val action = OrderFragmentDirections.actionOrderFragmentToMultiOrderPackFragment()
                action.orderID = order.baseOrderId
                navController.navigate(action)
//                val action =
//                    OrderFragmentDirections.actionOrderFragmentToOrderDetailsFragment(order.orderId)
//                navController.navigate(action)
            }
        } else {
            showShortToast("Order id is is missing or something wrong")
        }
    }

}