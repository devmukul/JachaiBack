package com.jachai.jachaimart.ui.order

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jachai.jachaimart.R
import com.jachai.jachaimart.databinding.OnGoingOrderFragmentBinding
import com.jachai.jachaimart.model.order.OrderDetailsResponse
import com.jachai.jachaimart.ui.base.BaseFragment
import com.jachai.jachaimart.ui.order.adapter.OrderDetailsAdapter
import com.jachai.jachaimart.utils.constant.ApiConstants

class OnGoingOrderFragment :
    BaseFragment<OnGoingOrderFragmentBinding>(R.layout.on_going_order_fragment) {

    companion object {
        fun newInstance() = OnGoingOrderFragment()
    }

    private lateinit var navController: NavController
    private lateinit var orderDetailsAdapter: OrderDetailsAdapter
    private val args: OnGoingOrderFragmentArgs by navArgs()
    private val viewModel: OnGoingOrderViewModel by viewModels()


    private lateinit var orderId: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        orderId = args.orderID


        initView()
        subscribeObservers()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getOrderDetails(orderId)
    }

    override fun initView() {
        binding.toolbarMain.back.setOnClickListener {
            val action =
                OnGoingOrderFragmentDirections.actionOnGoingOrderFragmentToGroceriesShopFragment()
            navController.navigate(action)
        }

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            orderDetailsAdapter = OrderDetailsAdapter(requireContext(), emptyList())
            adapter = orderDetailsAdapter
        }

        viewModel.successOrderDetailsLiveData.observe(viewLifecycleOwner) {
            updateUI(it)


        }


    }

    private fun updateUI(it: OrderDetailsResponse?) {
        binding.apply {
            orderID.text = it?.order?.orderId.toString()
            orderFrom.text = it?.order?.shop?.name.toString()
            itemCost.text = it?.order?.subTotal.toString()
            itemGrandTotal.text = it?.order?.total.toString()
            deliverManName.text = it?.order?.deliveryMan.toString()

            when {
                it?.order?.status?.equals(ApiConstants.ORDER_INITIATED) == true -> {

                }
                it?.order?.status?.equals(ApiConstants.ORDER_ACCEPTED_BY_DELIVERY_MAN) == true -> {
                    constraintLayout9.visibility = View.VISIBLE

                }
                it?.order?.status?.equals(ApiConstants.ORDER_PROCESSING) == true -> {
                    constraintLayout9.visibility = View.VISIBLE

                }
                it?.order?.status?.equals(ApiConstants.ORDER_PICKED) == true -> {
                    constraintLayout9.visibility = View.VISIBLE

                }
                it?.order?.status?.equals(ApiConstants.ORDER_DELIVERED) == true -> {
                    constraintLayout9.visibility = View.VISIBLE

                }


            }


            orderDetailsAdapter.setList(it?.order?.products)
            orderDetailsAdapter.notifyDataSetChanged()


        }
    }

    override fun subscribeObservers() {

    }


}