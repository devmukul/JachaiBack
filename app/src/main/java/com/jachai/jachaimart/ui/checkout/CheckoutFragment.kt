package com.jachai.jachaimart.ui.checkout

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jachai.jachai_driver.utils.ToastUtils
import com.jachai.jachaimart.JachaiFoodApplication
import com.jachai.jachaimart.R
import com.jachai.jachaimart.databinding.CheckoutFragmentBinding
import com.jachai.jachaimart.model.request.PaymentRequest
import com.jachai.jachaimart.model.response.address.Address
import com.jachai.jachaimart.ui.base.BaseFragment
import com.jachai.jachaimart.ui.checkout.adapter.CheckoutAdapter
import com.jachai.jachaimart.utils.SharedPreferenceUtil

class CheckoutFragment : BaseFragment<CheckoutFragmentBinding>(R.layout.checkout_fragment) {

    companion object {
        fun newInstance() = CheckoutFragment()
    }

    private lateinit var navController: NavController
    private lateinit var checkoutAdapter: CheckoutAdapter
    private val viewModel: CheckoutViewModel by viewModels()
    private val args: CheckoutFragmentArgs by navArgs()
    var mCheckedId = ""
    var mOrderId = ""

    private lateinit var additionalComment: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        additionalComment = args.addNote.toString()
        initView()
        subscribeObservers()
    }



    override fun initView() {
        viewModel.geOrderList()
        updateBottomCart(0.0)
        binding.apply {


            toolbarMain.title.text = "Checkout"
            toolbarMain.back.setOnClickListener {
                val action = CheckoutFragmentDirections.actionCheckoutFragmentToCartFragment()
                navController.navigate(action)
            }

            editAddress.setOnClickListener {
                val action =
                    CheckoutFragmentDirections.actionCheckoutFragmentToMyAddressListFragment()
                action.shop =  SharedPreferenceUtil.getNearestShop()
                navController.navigate(action)
            }

            recyclerView.apply {
                layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                checkoutAdapter = CheckoutAdapter(requireContext(), emptyList())
                adapter = checkoutAdapter
            }

            updateCostCalculation()

            comment.text = SharedPreferenceUtil.getNotes()


            checkout.text = getString(R.string.place_order)
            clCheckout.setOnClickListener {


                val address: Address = (SharedPreferenceUtil.getDeliveryAddress()
                    ?: SharedPreferenceUtil.getCurrentAddress()) as Address



                viewModel.placeOrder(
                    SharedPreferenceUtil.getNotes().toString(),
                    address
                )


            }
            option1.value = "Cash on Delivery"
            option2.value = "Online Payment"
            option1.isChecked = true

            paymentRadio.setOnCheckedChangeListener { radiogroupn, radioButton, _, checkedId ->
                when (checkedId) {
                    R.id.option1 -> mCheckedId = "COD"
                    R.id.option2 -> mCheckedId = "SSL"
                }
                System.out.println("test $mCheckedId")

            }
        }


    }

    override fun onResume() {
        super.onResume()
        updateDeliveryAddress()
        updateCostCalculation()

    }

    private fun updateDeliveryAddress() {
        binding.deliveryAddress.text = SharedPreferenceUtil.getDeliveryAddress()?.fullAddress
            ?: SharedPreferenceUtil.getCurrentAddress()?.fullAddress

    }

    private fun updateCostCalculation() {
        binding.apply {
            val dao = JachaiFoodApplication.mDatabase.daoAccess()

            val subtotal = dao.getProductOrderSubtotal()
            val deliveryCost =
                SharedPreferenceUtil.getNearestShop()?.deliveryCharge?.toFloat() ?: 0.toFloat()
            val vatSdPercent = SharedPreferenceUtil.getNearestShop()?.vat?.toFloat() ?: 0.toFloat()
            val vatSd = (subtotal * vatSdPercent) / 100
            val discount = viewModel.getDiscountPrice()
            val total = subtotal + deliveryCost + vatSd
            val grandTotal = total + discount


            itemCost.text = String.format("%.2f", subtotal)
            itemGrandTotal.text = String.format("%.2f", grandTotal)
            totalDiscount.text = String.format("%.2f", discount)
            vat.text = String.format("%.2f", vatSd)


            deliveryCharge.text = String.format("%.2f", deliveryCost)

            updateBottomCart(grandTotal)


        }
    }

    override fun subscribeObservers() {
        viewModel.successProductOrderListLiveData.observe(viewLifecycleOwner) {
            checkoutAdapter.setList(it)
            checkoutAdapter.notifyDataSetChanged()
        }

        viewModel.successOrderLiveData.observe(viewLifecycleOwner) {
            val jacjaiUrl = "https://www.jachai.com"
            viewModel.clearCreatedOrder()
            if (mCheckedId == "SSL") {
                mOrderId = it.orderId!!
                val paymentRequest = PaymentRequest(
                    it.total!!,
                    it.orderId!!,
                    mCheckedId,
                    "$jacjaiUrl/payment/success",
                    "$jacjaiUrl/payment/fail",
                    "$jacjaiUrl/payment/cancel"
                )
                viewModel.requestPayment(paymentRequest)
            } else {
                dismissLoader()
                val action =
                    CheckoutFragmentDirections.actionCheckoutFragmentToOnGoingOrderFragment()
                action.orderID = it.orderId.toString()
                navController.navigate(action)
            }
        }

        viewModel.successPaymentRequestLiveData.observe(viewLifecycleOwner) {
            dismissLoader()
            val action = CheckoutFragmentDirections.actionCheckoutFragmentToPaymentFragment()
            action.orderID = mOrderId
            action.url = it.url
            navController.navigate(action)
        }

        viewModel.errorResponseLiveData.observe(viewLifecycleOwner, { message ->
            dismissLoader()
            ToastUtils.error(message ?: getString(R.string.text_something_went_wrong))
        })


    }

    private fun updateBottomCart(grandTotal: Double) {
        binding.apply {

            itemCount.text =
                JachaiFoodApplication.mDatabase.daoAccess().getProductOrdersSize()
                    .toString()
            totalCount.text = if (grandTotal == 0.0) {
                String.format("%.2f", JachaiFoodApplication.mDatabase.daoAccess().totalCost())

            } else {
                String.format("%.2f", grandTotal)

            }


        }

    }


}