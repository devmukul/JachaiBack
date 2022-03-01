package com.jachai.jachaimart.ui.order

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.RadioGroup
import androidx.activity.addCallback
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.crashlytics.ktx.setCustomKeys
import com.google.firebase.ktx.Firebase
import com.jachai.jachai_driver.utils.ToastUtils
import com.jachai.jachaimart.R
import com.jachai.jachaimart.databinding.OnGoingOrderFragmentBinding
import com.jachai.jachaimart.decorator.PayRadioButton
import com.jachai.jachaimart.model.order.details.OrderDetailsResponse
import com.jachai.jachaimart.model.request.PaymentRequest
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

    var mCheckedId = "SSL"
    private lateinit var orderId: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        orderId = args.orderID


        initView()
        subscribeObservers()

        val callback = requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            val action =
                OnGoingOrderFragmentDirections.actionOnGoingOrderFragmentToGroceriesShopFragment()
            navController.navigate(action)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getOrderDetails(orderId)
    }

    override fun initView() {

        binding.onLinePayLayout.visibility = View.GONE
        binding.toolbarMain.back.setOnClickListener {
            val action =
                OnGoingOrderFragmentDirections.actionOnGoingOrderFragmentToGroceriesShopFragment()
            navController.navigate(action)
        }

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            orderDetailsAdapter = OrderDetailsAdapter(requireContext(), emptyList(),true)
            adapter = orderDetailsAdapter
        }

        binding.callCustomer.setOnClickListener {
            phoneCall(CommonConstants.CUSTOMER_CARE_PHONE_NO)

        }
        binding.toolbarMain.helpButton.setOnClickListener {
            phoneCall(CommonConstants.CUSTOMER_CARE_PHONE_NO)
        }

        binding.onlinePaymentItems.setOnCheckedChangeListener { radioGroup, radioButton, isChecked, checkedId ->
            val payRadioButton: PayRadioButton =
                radioGroup.rootView.findViewById<PayRadioButton>(checkedId)
            mCheckedId = payRadioButton.value.name
        }


    }

    private fun updateUI(
        orderDetailsResponse
        : OrderDetailsResponse?
    ) {
        binding.apply {
            toolbarMain.subTitle.text = orderDetailsResponse?.order?.shop?.name.toString()
            address.text = orderDetailsResponse?.order?.shippingAddress.toString()
            orderID.text = orderDetailsResponse?.order?.orderId.toString()
            orderFrom.text = orderDetailsResponse?.order?.shop?.name.toString()
            itemCost.text = orderDetailsResponse?.order?.subTotal.toString()
            itemGrandTotal.text = orderDetailsResponse?.order?.total.toString()
            deliveryCharge.text = orderDetailsResponse?.order?.deliveryCharge.toString()
            totalDiscount.text = "-${orderDetailsResponse?.order?.discount ?: 0.0}"
            vat.text = orderDetailsResponse?.order?.vat.toString()
            paymentType.text = orderDetailsResponse?.order?.paymentMethod

            deliverManName.text = orderDetailsResponse?.order?.deliveryMan?.name
            comment.text = orderDetailsResponse?.order?.orderNote.toString()


            when {
                orderDetailsResponse?.order?.status?.equals(ApiConstants.ORDER_INITIATED) == true -> {
                    state1.isIndeterminate = true
                    status.text = "Initiating order"
                    statusMessage.text = "Shop will pick your order soon."
                    timeDuration.text = "55 mins - 24 hrs"

                }
                orderDetailsResponse?.order?.statusOfDeliveryMan?.equals(ApiConstants.ORDER_ACCEPTED_BY_DELIVERY_MAN) == true -> {
                    constraintLayout9.visibility = View.VISIBLE
                    state1.isIndeterminate = false
                    state1.progress = 100
                    state2.isIndeterminate = true
                    status.text = "Packaging products"
                    statusMessage.text = "Shop is arranging your product."
                    timeDuration.text = "1 hr - 3 hrs"

                }
                orderDetailsResponse?.order?.statusOfDeliveryMan?.equals(ApiConstants.ORDER_PICKED) == true -> {
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
                orderDetailsResponse?.order?.status?.equals(ApiConstants.ORDER_PROCESSING) == true -> {
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
                orderDetailsResponse?.order?.status?.equals(ApiConstants.ORDER_DELIVERED) == true -> {
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

            if (orderDetailsResponse?.order?.totalPaid == orderDetailsResponse?.order?.total) {
                paymentStatus.text = "PAID"
                paymentStatus.setTextColor(Color.parseColor("#28a745"))
                payNow.visibility = View.GONE
            } else if ((orderDetailsResponse?.order?.total!! - orderDetailsResponse.order.totalPaid) <= 0.0) {
                paymentStatus.text = "PARTIAL PAID"
                paymentStatus.setTextColor(Color.parseColor("#3A494E"))
                payNow.visibility = View.VISIBLE
            } else {
                paymentStatus.text = "UNPAID"
                paymentStatus.setTextColor(Color.parseColor("#FF0000"))
                payNow.visibility = View.VISIBLE
            }


            orderDetailsAdapter.setList(orderDetailsResponse?.order?.products)
            orderDetailsAdapter.notifyDataSetChanged()

            payNow.setOnClickListener {
                showLoader()
                viewModel.getAllPaymentMethods()
                onLinePayLayout.visibility = View.VISIBLE
                onLinePayLayout.requestFocus()
                payNow.visibility =  View.GONE

            }
            goForPayment.setOnClickListener {
                showLoader()
                val jacjaiUrl = "https://www.jachai.com"
                val order = orderDetailsResponse?.order
                val paymentRequest = PaymentRequest(
                    (order?.total!! - order.totalPaid),
                    orderId,
                    mCheckedId,
                    "$jacjaiUrl/payment/success",
                    "$jacjaiUrl/payment/fail",
                    "$jacjaiUrl/payment/cancel"
                )
                viewModel.requestPayment(paymentRequest)
            }


        }
    }

    override fun subscribeObservers() {
        viewModel.successOrderDetailsLiveData.observe(viewLifecycleOwner) {
            updateUI(it)
        }

        viewModel.successPaymentRequestLiveData.observe(viewLifecycleOwner) {
            dismissLoader()
            try {
                val action =
                    OnGoingOrderFragmentDirections.actionOnGoingOrderFragmentToPaymentFragment()
                action.orderID = orderId
                action.url = it.url
                navController.navigate(action)

            } catch (ex: Exception) {
                val crashlytics = Firebase.crashlytics
                crashlytics.setCustomKeys {
                    key("ERROR_PAYMENT", "URL-null")
                    ex.message?.let { it1 -> key("ERROR_PAYMENT", it1) }
                }
            }
        }

        viewModel.successPaymentMethodListLiveData.observe(viewLifecycleOwner) {
            dismissLoader()
            if (it != null) {
                if (!it.methods.isNullOrEmpty()) {
                    val layoutParams: LinearLayout.LayoutParams = RadioGroup.LayoutParams(
                        RadioGroup.LayoutParams.MATCH_PARENT,
                        RadioGroup.LayoutParams.WRAP_CONTENT
                    )
                    binding.onlinePaymentItems.removeAllViews()
                    for ((i, item) in it.methods.withIndex()) {
                        if (item.name == "COD" || item.title == "COD") {
                            continue
                        }
                        val newRadioButton = PayRadioButton(requireContext())
                        newRadioButton.id = i
                        newRadioButton.setValue(item)
                        if (i == 0) {
                            newRadioButton.isChecked = true
                        }
                        binding.onlinePaymentItems.addView(newRadioButton, layoutParams)
                    }
                    binding.nestedScrollView.post(Runnable {
                        binding.nestedScrollView.fullScroll(NestedScrollView.FOCUS_DOWN)
                    })
                } else {
                    ToastUtils.error(getString(R.string.text_something_went_wrong))
                }


            } else {
                ToastUtils.error(getString(R.string.text_something_went_wrong))
            }


        }

    }


}