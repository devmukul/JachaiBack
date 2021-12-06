package com.jachai.jachaimart.ui.checkout

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jachai.jachai_driver.utils.ToastUtils
import com.jachai.jachaimart.JachaiApplication
import com.jachai.jachaimart.R
import com.jachai.jachaimart.databinding.CheckoutFragmentBinding
import com.jachai.jachaimart.model.request.PaymentRequest
import com.jachai.jachaimart.model.response.address.Address
import com.jachai.jachaimart.ui.base.BaseFragment
import com.jachai.jachaimart.ui.checkout.adapter.CheckoutAdapter
import com.jachai.jachaimart.utils.SharedPreferenceUtil
import com.jachai.jachaimart.utils.constant.CommonConstants
import es.dmoral.toasty.Toasty

class CheckoutFragment : BaseFragment<CheckoutFragmentBinding>(R.layout.checkout_fragment) {

    companion object {
        fun newInstance() = CheckoutFragment()
    }
    private lateinit var customTabsIntent:CustomTabsIntent
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

                showLoader()
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
            }
            textView4.setOnClickListener {
                val builder = CustomTabsIntent.Builder()
                builder.setToolbarColor(ContextCompat.getColor(requireContext(), R.color.design_default_color_on_primary))
                customTabsIntent = builder.build()
                customTabsIntent.launchUrl(requireContext(), Uri.parse(CommonConstants.POLICY_URL))
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
            val dao = JachaiApplication.mDatabase.daoAccess()

            val subtotal = dao.getProductOrderSubtotal()

            val vatSdPercent = SharedPreferenceUtil.getNearestShop()?.vat?.toFloat() ?: 0.toFloat()
            val vatSd : Double = (subtotal * vatSdPercent) / 100
            val discount = viewModel.getDiscountPrice()

            val total = subtotal  + vatSd + discount


            var nearCostToFree = 0F
            val deliveryCost = if (SharedPreferenceUtil.getNearestShop()?.isFreeDelivery== true){
                0.toFloat()
            } else{
                if (SharedPreferenceUtil.getNearestShop()?.minimumAmountForFreeDelivery !=0F){
                    if (total.toDouble()>= SharedPreferenceUtil.getNearestShop()?.minimumAmountForFreeDelivery!! ){
                        0.toFloat()
                    }else{
                        nearCostToFree = SharedPreferenceUtil.getNearestShop()?.minimumAmountForFreeDelivery!!.toFloat() - total.toFloat()
                        SharedPreferenceUtil.getNearestShop()?.deliveryCharge?.toFloat() ?: 0.toFloat()
                    }
                }else {
                    SharedPreferenceUtil.getNearestShop()?.deliveryCharge?.toFloat() ?: 0.toFloat()
                }
            }


            val grandTotal = total + deliveryCost.toDouble()


            itemCost.text = String.format("%.2f", subtotal)
            itemGrandTotal.text = String.format("%.2f", grandTotal)
            totalDiscount.text = String.format("%.2f", discount)
            vat.text = String.format("%.2f", vatSd)


            deliveryCharge.text = String.format("%.2f", deliveryCost)
            if (nearCostToFree>0){
                tvDeliveryFreeMessage.visibility = View.VISIBLE
                tvDeliveryFreeMessage.text = String.format("Add %.2f tk more to get free delivery", nearCostToFree)
            }else{
                tvDeliveryFreeMessage.visibility = View.GONE
            }

            updateBottomCart(grandTotal)


        }
    }

    override fun subscribeObservers() {
        viewModel.successProductOrderListLiveData.observe(viewLifecycleOwner) {
            checkoutAdapter.setList(it)
            checkoutAdapter.notifyDataSetChanged()
        }

        viewModel.successOrderLiveData.observe(viewLifecycleOwner) {
            val jachaiUrl = "https://www.jachai.com"
            viewModel.clearCreatedOrder()
            if (mCheckedId == "SSL") {
                mOrderId = it.orderId!!
                val paymentRequest = PaymentRequest(
                    it.total!!,
                    it.orderId,
                    mCheckedId,
                    "$jachaiUrl/payment/success",
                    "$jachaiUrl/payment/fail",
                    "$jachaiUrl/payment/cancel"
                )
                viewModel.requestPayment(paymentRequest)
            } else {
                dismissLoader()
                val action =
                    CheckoutFragmentDirections.actionCheckoutFragmentToOnGoingOrderFragment(it.orderId.toString())
                navController.navigate(action)
            }
        }

        viewModel.failedResponseLiveData.observe(viewLifecycleOwner) {
            Toasty.error(requireContext(), it?.message!!).show()
            dismissLoader()
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
                JachaiApplication.mDatabase.daoAccess().getProductOrdersSize()
                    .toString()
            totalCount.text = if (grandTotal == 0.0) {
                String.format("%.2f", JachaiApplication.mDatabase.daoAccess().totalCost())

            } else {
                String.format("%.2f", grandTotal)

            }


        }

    }


}