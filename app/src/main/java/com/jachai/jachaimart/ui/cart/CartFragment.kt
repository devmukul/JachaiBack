package com.jachai.jachaimart.ui.cart

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.jachai.jachai_driver.utils.showShortToast
import com.jachai.jachaimart.JachaiApplication
import com.jachai.jachaimart.R
import com.jachai.jachaimart.databinding.CartFragmentBinding
import com.jachai.jachaimart.model.order.ProductOrder
import com.jachai.jachaimart.ui.base.BaseFragment
import com.jachai.jachaimart.ui.cart.adapter.CartAdapter
import com.jachai.jachaimart.utils.SharedPreferenceUtil
import com.jachai.jachaimart.utils.constant.CommonConstants

class CartFragment : BaseFragment<CartFragmentBinding>(R.layout.cart_fragment),
    CartAdapter.Interaction {

    companion object {
        fun newInstance() = CartFragment()
    }

    private lateinit var cartAdapter: CartAdapter
    private lateinit var navController: NavController
    private lateinit var customTabsIntent:CustomTabsIntent


    private val viewModel: CartViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        viewModel.getOrderDetails()
        viewModel.geOrderList()
        initView()
        subscribeObservers()
    }

    override fun initView() {

        binding.apply {
            toolbarMain.back.setOnClickListener {
                goToShop()
//                navController.popBackStack()
            }


            checkout.text = getString(R.string.checkout_order)
            recyclerView.apply {
                layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                cartAdapter = CartAdapter(requireContext(), emptyList(), this@CartFragment)
                adapter = cartAdapter
            }

            toolbarMain.clearCart.setOnClickListener {
                JachaiApplication.mDatabase.daoAccess().clearOrderTable()
                goToShop()
            }

            clCheckout.setOnClickListener {
//                navController.navigate(R.id.action_cartFragment_to_checkoutFragment)

                SharedPreferenceUtil.saveNotes(comment.text.toString())

                val action =
                    CartFragmentDirections.actionCartFragmentToCheckoutFragment(null)
                action.addNote = comment.text.toString()
                navController.navigate(action)
            }
            textView4.setOnClickListener {
                val builder = CustomTabsIntent.Builder()
                builder.setToolbarColor(ContextCompat.getColor(requireContext(), R.color.design_default_color_on_primary))
                customTabsIntent = builder.build()
                customTabsIntent.launchUrl(requireContext(), Uri.parse(CommonConstants.POLICY_URL))
            }

        }


    }

    private fun goToShop() {
        val action =
            CartFragmentDirections.actionCartFragmentToGroceriesShopFragment()
        navController.navigate(action)
    }

    override fun subscribeObservers() {
        viewModel.successShopDetailsLiveData.observe(viewLifecycleOwner) {
            binding.apply {
                when {
                    it.orderModule.equals(CommonConstants.JC_FOOD_TYPE) -> {
                        restraurantName.text = it.shopName
                        restraurantSubtitle.text = it.shopSubtitle

                        Glide.with(requireContext())
                            .load(it.shopImage)
                            .placeholder(R.drawable.ic_place_holder)
                            .error(R.drawable.ic_place_holder)
                            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                            .into(image)
                    }
                    it.orderModule.equals(CommonConstants.JC_MART_TYPE) -> {
                        restraurantName.text = it.hubName
                        restraurantSubtitle.text = it.shopSubtitle

                        Glide.with(requireContext())
                            .load(it.hubImage)
                            .placeholder(R.drawable.ic_place_holder)
                            .error(R.drawable.ic_place_holder)
                            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                            .into(image)
                    }
                }
            }


        }

        viewModel.successProductOrderListLiveData.observe(viewLifecycleOwner) {


            if (it.isEmpty()) {
                goToShop()
            } else {
                cartAdapter.setList(it)
                cartAdapter.notifyDataSetChanged()
                updateCostCalculation()
            }
        }

    }

    override fun onQuantityItemAdded(item: ProductOrder?) {
        var qty = (item?.quantity?.toInt() ?: 0) + 1
        val finalCount = if (qty <= item?.stock?.toInt() ?: 0) {
            qty
        } else {
            showShortToast("Max limit reached")
            item?.stock?.toInt() ?: 0
        }
        qty = finalCount



        item?.quantity = finalCount
        viewModel.updateQuantity(item)


    }

    override fun onQuantityItemSubtraction(item: ProductOrder?) {
        val qty = (item?.quantity?.toInt() ?: 0) - 1
        item?.quantity = qty
        viewModel.updateQuantity(item)


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

    private fun updateCostCalculation() {
        binding.apply {
            val dao = JachaiApplication.mDatabase.daoAccess()

            val subtotal = dao.getProductOrderSubtotal()
            val vatSdPercent = SharedPreferenceUtil.getNearestHub()?.vat?.toFloat() ?: 0.toFloat()
            val vatSd : Double = (subtotal * vatSdPercent) / 100
            val discount = viewModel.getDiscountPrice()

            val total = subtotal  + vatSd + discount


            var nearCostToFree = 0F
            val deliveryCost = if (SharedPreferenceUtil.getNearestHub()?.isFreeDelivery== true){
                0.toFloat()
            } else{
                if (SharedPreferenceUtil.getNearestHub()?.minimumAmountForFreeDelivery !=0F){
                    if (total.toDouble()>= SharedPreferenceUtil.getNearestHub()?.minimumAmountForFreeDelivery!! ){
                        0.toFloat()
                    }else{
                        nearCostToFree = SharedPreferenceUtil.getNearestHub()?.minimumAmountForFreeDelivery!!.toFloat() - total.toFloat()
                        SharedPreferenceUtil.getNearestHub()?.deliveryCharge?.toFloat() ?: 0.toFloat()
                    }
                }else {
                    SharedPreferenceUtil.getNearestHub()?.deliveryCharge?.toFloat() ?: 0.toFloat()
                }
            }


            val grandTotal = total + deliveryCost.toDouble()


            itemCost.text = String.format("%.2f", subtotal)
            totalDiscount.text = String.format("%.2f", discount)
            vat.text = String.format("%.2f", vatSd)

            deliveryCharge.text = String.format("%.2f", deliveryCost)
            if (nearCostToFree>0){
                tvDeliveryFreeMessage.visibility = View.VISIBLE
                val deliveryFreeMessage = String.format("Add %.2f tk more to get free delivery", nearCostToFree)
                tvDeliveryFreeMessage.text = deliveryFreeMessage
                showShortToast(deliveryFreeMessage)
            }else{
                tvDeliveryFreeMessage.visibility = View.GONE
            }
            updateBottomCart(grandTotal)


        }
    }

}