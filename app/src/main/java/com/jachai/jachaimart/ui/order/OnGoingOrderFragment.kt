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
import com.jachai.jachaimart.model.order.details.OrderDetailsResponse
import com.jachai.jachaimart.ui.base.BaseFragment
import com.jachai.jachaimart.ui.order.adapter.OrderDetailsAdapter
import com.jachai.jachaimart.utils.constant.ApiConstants
import com.jachai.jachaimart.utils.constant.CommonConstants

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
        binding.callCustomer.setOnClickListener {
            phoneCall(CommonConstants.CUSTOMER_CARE_PHONE_NO)

        }





    }

    private fun updateUI(it: OrderDetailsResponse?) {
        binding.apply {
            toolbarMain.subTitle.text = it?.order?.shop?.name.toString()
            address.text = it?.order?.shippingAddress.toString()
            orderID.text = it?.order?.orderId.toString()
            orderFrom.text = it?.order?.shop?.name.toString()
            itemCost.text = it?.order?.subTotal.toString()
            itemGrandTotal.text = it?.order?.total.toString()
            deliveryCharge.text = it?.order?.deliveryCharge.toString()
            totalDiscount.text = "-${it?.order?.discount ?: 0.0}"
            vat.text = it?.order?.vat.toString()
            paymentType.text = it?.order?.paymentMethod

            deliverManName.text = it?.order?.deliveryMan?.name

            when {
                it?.order?.status?.equals(ApiConstants.ORDER_INITIATED) == true -> {
                    state1.isIndeterminate = true
                    status.text = "Initiating order"
                    statusMessage.text = "Shop will pick your order soon."
                    timeDuration.text = "25 - 30 mins"

                }
                it?.order?.statusOfDeliveryMan?.equals(ApiConstants.ORDER_ACCEPTED_BY_DELIVERY_MAN) == true -> {
                    constraintLayout9.visibility = View.VISIBLE
                    state1.isIndeterminate = false
                    state1.progress = 100
                    state2.isIndeterminate = true
                    status.text = "Packaging products"
                    statusMessage.text = "Shop is arranging your product."
                    timeDuration.text = "22 - 28 mins"

                }
                it?.order?.statusOfDeliveryMan?.equals(ApiConstants.ORDER_PICKED) == true -> {
                    constraintLayout9.visibility = View.VISIBLE
                    state1.isIndeterminate = false
                    state1.progress = 100
                    state2.isIndeterminate = false
                    state2.progress = 100
                    state3.isIndeterminate = true
                    status.text = "Rider on the way"
                    statusMessage.text = "Rider just picked your order from the shop"
                    timeDuration.text = "15 - 23 mins"


                }
                it?.order?.status?.equals(ApiConstants.ORDER_PROCESSING) == true -> {
                    constraintLayout9.visibility = View.VISIBLE
                    state1.isIndeterminate = false
                    state1.progress = 100
                    state2.isIndeterminate = false
                    state2.progress = 100
                    state3.isIndeterminate = false
                    state3.progress = 100
                    state4.isIndeterminate = true
                    status.text = "Rider on the way"
                    statusMessage.text = "Your order is on the way for delivery"
                    timeDuration.text = "0 - 5 mins"
                }
                it?.order?.status?.equals(ApiConstants.ORDER_DELIVERED) == true -> {
                    constraintLayout9.visibility = View.VISIBLE
                    state1.isIndeterminate = false
                    state1.progress = 100
                    state2.isIndeterminate = false
                    state2.progress = 100
                    state3.isIndeterminate = false
                    state3.progress = 100
                    state4.isIndeterminate = false
                    state4.progress = 100
                    status.text = "Please collect order"
                    statusMessage.text = "Thanks for shopping with JaChai Mart"
                    timeDuration.text = "Delivered"
                    textView14.text = "ARRIVED"
                }


            }


            orderDetailsAdapter.setList(it?.order?.products)
            orderDetailsAdapter.notifyDataSetChanged()


        }
    }

    override fun subscribeObservers() {
        viewModel.successOrderDetailsLiveData.observe(viewLifecycleOwner) {
            updateUI(it)
        }
    }


}