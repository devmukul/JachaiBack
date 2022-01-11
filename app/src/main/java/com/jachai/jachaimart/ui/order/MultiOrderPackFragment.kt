package com.jachai.jachaimart.ui.order

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.RadioGroup
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
import com.jachai.jachaimart.databinding.MultiOrderPackFragmentBinding
import com.jachai.jachaimart.decorator.PayRadioButton
import com.jachai.jachaimart.model.order.multi_order.BaseOrderDetailsResponse
import com.jachai.jachaimart.model.order.multi_order.SubOrder
import com.jachai.jachaimart.model.request.PaymentRequest
import com.jachai.jachaimart.ui.base.BaseFragment
import com.jachai.jachaimart.ui.order.adapter.SubOrderDetailsAdapter
import com.jachai.jachaimart.utils.constant.ApiConstants
import com.jachai.jachaimart.utils.constant.CommonConstants

class MultiOrderPackFragment :
    BaseFragment<MultiOrderPackFragmentBinding>(R.layout.multi_order_pack_fragment) {

    companion object {
        fun newInstance() = MultiOrderPackFragment()
    }

    private lateinit var navController: NavController
    private lateinit var subOrderDetailsAdapter: SubOrderDetailsAdapter
    private lateinit var orderId: String
    private val args: MultiOrderPackFragmentArgs by navArgs()

    private val viewModel: MultiOrderPackViewModel by viewModels()
    var mCheckedId = "SSL"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        orderId = args.orderID
        initView()
        subscribeObservers()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getMultiOrderDetails(orderId)
    }

    override fun initView() {
        binding.apply {
            toolbarMain.back.setOnClickListener {
                val action =
                    MultiOrderPackFragmentDirections.actionMultiOrderPackFragmentToOrderFragment()
                navController.navigate(action)
            }
            recyclerView.apply {
                layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
                subOrderDetailsAdapter = SubOrderDetailsAdapter(requireContext(), emptyList())
                adapter = subOrderDetailsAdapter
            }
            callCustomer.setOnClickListener {
                phoneCall(CommonConstants.CUSTOMER_CARE_PHONE_NO)

            }
            toolbarMain.helpButton.setOnClickListener {
                phoneCall(CommonConstants.CUSTOMER_CARE_PHONE_NO)
            }
            payNow.setOnClickListener {
                showLoader()
                viewModel.getAllPaymentMethods()
                onLinePayLayout.visibility = View.VISIBLE
                onLinePayLayout.requestFocus()
                payNow.visibility = View.GONE

            }

            binding.onlinePaymentItems.setOnCheckedChangeListener { radioGroup, radioButton, isChecked, checkedId ->
                var payRadioButton: PayRadioButton =
                    radioGroup.rootView.findViewById<PayRadioButton>(checkedId)
                mCheckedId = payRadioButton.value.name

            }
        }

    }

    override fun subscribeObservers() {
        viewModel.successMultiOrderDetailsLiveData.observe(viewLifecycleOwner) {
            updateUI(it)
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

        viewModel.successPaymentRequestLiveData.observe(viewLifecycleOwner) {
            dismissLoader()
            try {
                val action =
                    MultiOrderPackFragmentDirections.actionMultiOrderPackFragmentToPaymentFragment()
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


    }

    private fun updateUI(it: BaseOrderDetailsResponse?) {
        val orders = it?.order

        if (!orders.isNullOrEmpty()) {
            binding.apply {
                onLinePayLayout.visibility = View.GONE
                toolbarMain.subTitle.text = orders[0].shop?.name.toString()
                address.text = orders[0].shippingAddress.toString()
                orderID.text = orders[0].baseOrderId.toString()

                paymentType.text = orders[0].paymentMethod

                deliverManName.text = orders[0].deliveryMan?.name
                when {
                    orders[0].totalPaid == orders[0].total -> {
                        paymentStatus.text = "PAID"
                        paymentStatus.setTextColor(Color.parseColor("#28a745"))
                        payNow.visibility = View.GONE
                    }
                    (orders[0].total!! - orders[0].totalPaid) <= 0.0 -> {
                        paymentStatus.text = "PARTIAL PAID"
                        paymentStatus.setTextColor(Color.parseColor("#3A494E"))
                        payNow.visibility = View.VISIBLE
                    }
                    else -> {
                        paymentStatus.text = "UNPAID"
                        paymentStatus.setTextColor(Color.parseColor("#FF0000"))
                        payNow.visibility = View.VISIBLE
                    }
                }
                if (orders[0].orderNote.isNullOrEmpty() || orders[0].orderNote.isNullOrBlank()) {
                    comment.visibility = View.GONE
                    additionalItem.visibility = View.GONE
                } else {
                    comment.text = orders[0].orderNote.toString()

                }

                when {
                    orders[0].status?.equals(ApiConstants.ORDER_DELIVERED) -> {
                        removePayLayout()
                    }
                    orders[0].status?.equals(ApiConstants.ORDER_REVIEWED) -> {
                        removePayLayout()
                    }
                    orders[0].status?.equals(ApiConstants.ORDER_CANCELLED) -> {
                        removePayLayout()
                    }
                    orders[0].status?.equals(ApiConstants.ORDER_COMPLETED) -> {
                        removePayLayout()
                    }
                }

            }
            updateTotals(orders)

            subOrderDetailsAdapter.setList(orders)
            subOrderDetailsAdapter.notifyDataSetChanged()


        }


    }

    private fun removePayLayout() {
        binding.apply {
            payNow.visibility = View.GONE
            onLinePayLayout.visibility = View.GONE
            paymentStatusView.visibility = View.GONE
        }
    }

    private fun updateTotals(orders: List<SubOrder>) {
        var tSubTotal = 0.0
        var tDeliveryCharge = 0.0
        var tVat = 0.0
        var tDiscount = 0.0
        var tGrandTotal = 0.0

        for (item in orders) {
            if (item.status.equals(ApiConstants.ORDER_CANCELLED)) {
                continue
            }
            tSubTotal += item.subTotal
            tDeliveryCharge += item.deliveryCharge
            tVat += item.vat
            tDiscount += item.discount
            tGrandTotal += item.total

        }

        binding.apply {
            itemCost.text = tSubTotal.toString()
            itemGrandTotal.text = tGrandTotal.toString()
            deliveryCharge.text = tDeliveryCharge.toString()
            totalDiscount.text = "-${tDiscount}"
            vat.text = tVat.toString()


            goForPayment.setOnClickListener {
                showLoader()
                val jacjaiUrl = "https://www.jachai.com"

                val paymentRequest = PaymentRequest(
                    tGrandTotal,
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


}