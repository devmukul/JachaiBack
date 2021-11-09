package com.jachai.jachaimart.ui.order

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.jachai.jachaimart.R
import com.jachai.jachaimart.databinding.OrderDetailsFragmentBinding
import com.jachai.jachaimart.model.order.details.OrderDetailsResponse
import com.jachai.jachaimart.ui.base.BaseFragment
import com.jachai.jachaimart.ui.order.adapter.OrderDetailsAdapter
import com.jachai.jachaimart.utils.constant.CommonConstants

class OrderDetailsFragment :
    BaseFragment<OrderDetailsFragmentBinding>(R.layout.order_details_fragment) {

    companion object {
        fun newInstance() = OrderDetailsFragment()
    }

    private lateinit var navController: NavController
    private val args: OrderDetailsFragmentArgs by navArgs()
    private lateinit var orderDetailsAdapter: OrderDetailsAdapter

    private val viewModel: OrderDetailsViewModel by viewModels()
    private lateinit var orderId: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        orderId = args.orderId

        initView()
        subscribeObservers()
    }

    override fun initView() {
        viewModel.getOrderDetails(orderId)

        binding.toolbarMain.back.setOnClickListener {
            navController.popBackStack()
        }
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            orderDetailsAdapter = OrderDetailsAdapter(requireContext(), emptyList())
            adapter = orderDetailsAdapter
        }


    }

    override fun subscribeObservers() {
        viewModel.successOrderDetailsLiveData.observe(viewLifecycleOwner) {
            updateUI(it)


        }
    }

    private fun updateUI(it: OrderDetailsResponse?) {
        binding.apply {


            restaurantName.text = it?.order?.shop?.name
            Glide.with(requireContext())
                .load(it?.order?.shop?.logo)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(restaurantImage)
            restaurantSubHead.text = it?.order?.shop?.address

            toolbarMain.subTitle.text = it?.order?.shop?.name.toString()
            address.text = it?.order?.shippingAddress.toString()
            orderID.text = it?.order?.orderId.toString()
            orderFrom.text = it?.order?.shop?.name.toString()
            itemCost.text = it?.order?.subTotal.toString()
            itemGrandTotal.text = it?.order?.total.toString()
            deliveryCharge.text = it?.order?.deliveryCharge.toString()
            totalDiscount.text = it?.order?.discount.toString()
            vat.text = it?.order?.vat.toString()
            deliverManName.text = it?.order?.deliveryMan?.name
            orderDetailsAdapter.setList(it?.order?.products)
            orderDetailsAdapter.notifyDataSetChanged()

            if (it?.order?.orderNote.isNullOrEmpty() || it?.order?.orderNote.isNullOrBlank()) {
                comment.visibility = View.GONE
            } else {
                comment.text = it?.order?.orderNote.toString()

            }
            callCustomer.setOnClickListener {
                phoneCall(CommonConstants.CUSTOMER_CARE_PHONE_NO)

            }
            toolbarMain.helpButton.setOnClickListener {
                phoneCall(CommonConstants.CUSTOMER_CARE_PHONE_NO)
            }

        }

    }


}