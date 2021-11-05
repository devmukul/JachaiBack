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
                val action =
                    CheckoutFragmentDirections.actionCheckoutFragmentToCartFragment()

                navController.navigate(action)
            }

            editAddress.setOnClickListener {
                val action =
                    CheckoutFragmentDirections.actionCheckoutFragmentToMyAddressListFragment()

                navController.navigate(action)
            }

            recyclerView.apply {
                layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                checkoutAdapter = CheckoutAdapter(requireContext(), emptyList())
                adapter = checkoutAdapter
            }

            updateCostCalculation()

            checkout.text = getString(R.string.place_order)


            clCheckout.setOnClickListener {


                SharedPreferenceUtil.getDeliveryAddress()?.let { it1 ->
                    viewModel.placeOrder(
                        SharedPreferenceUtil.getNotes().toString(),
                        it1
                    )
                }

//                val location = CurrentLocation(
//                    23.737209579805366,
//                    90.43048604373678, "NA"
//                )


                showLoader()
            }
        }


    }

    override fun onResume() {
        super.onResume()
        updateDeliveryAddress()

    }

    private fun updateDeliveryAddress() {
        binding.deliveryAddress.text = SharedPreferenceUtil.getDeliveryAddress()?.fullAddress
            ?: SharedPreferenceUtil.getCurrentAddress()?.fullAddress

    }

    private fun updateCostCalculation() {
        binding.apply {
            val dao = JachaiFoodApplication.mDatabase.daoAccess()

            val subtotal = dao.getProductOrderSubtotal()
            val deliveryCost = 20.0
            val vatSdPercent = 10.0
            val vatSd = (subtotal * vatSdPercent) / 100
            val discount = 20.0
            val total = subtotal + deliveryCost + vatSd
            val grandTotal = total - discount


            itemCost.text = subtotal.toString()
            itemGrandTotal.text = grandTotal.toString()
            totalDiscount.text = "-$discount"
            vat.text = vatSd.toString()


            deliveryCharge.text = "$deliveryCost"

            updateBottomCart(grandTotal)


        }
    }

    override fun subscribeObservers() {
        viewModel.successProductOrderListLiveData.observe(viewLifecycleOwner) {
            checkoutAdapter.setList(it)
            checkoutAdapter.notifyDataSetChanged()
        }

        viewModel.successOrderLiveData.observe(viewLifecycleOwner) {
            dismissLoader()
            val action = CheckoutFragmentDirections.actionCheckoutFragmentToOnGoingOrderFragment()
            action.orderID = it.orderId.toString()
            navController.navigate(action)
            viewModel.clearCreatedOrder()

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
                JachaiFoodApplication.mDatabase.daoAccess().totalCost().toString()
            } else {
                grandTotal.toString()
            }


        }

    }


}