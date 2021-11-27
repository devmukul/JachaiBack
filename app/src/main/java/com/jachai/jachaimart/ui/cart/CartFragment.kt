package com.jachai.jachaimart.ui.cart

import android.net.Uri
import android.os.Bundle
import android.view.View
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
                restraurantName.text = it.shopName
                restraurantSubtitle.text = it.shopSubtitle

                Glide.with(requireContext())
                    .load(it.shopImage)
                    .placeholder(R.drawable.ic_place_holder)
                    .error(R.drawable.ic_place_holder)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .into(image)
            }


        }

        viewModel.successProductOrderListLiveData.observe(viewLifecycleOwner) {


            if (it.isEmpty()) {
                navController.popBackStack()
            } else {
                cartAdapter.setList(it)
                cartAdapter.notifyDataSetChanged()
                updateCostCalculation()
            }
        }

    }

    override fun onQuantityItemAdded(item: ProductOrder?) {
        var qty = (item?.quantity?.toInt() ?: 0) + 1
        val finalCount = if (qty <= 6 ) {
            qty
        } else {
            showShortToast("Max limit reached")
            6
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
            var nearCostToFree = 0F
            val deliveryCost = if (SharedPreferenceUtil.getNearestShop()?.isFreeDelivery== true){
                0.toFloat()
            } else{
                if (SharedPreferenceUtil.getNearestShop()?.minimumAmountForFreeDelivery != null &&
                    SharedPreferenceUtil.getNearestShop()?.minimumAmountForFreeDelivery !=0F){
                    if (subtotal.toDouble()>= SharedPreferenceUtil.getNearestShop()?.minimumAmountForFreeDelivery!! ){
                        0.toFloat()
                    }else{
                        nearCostToFree = SharedPreferenceUtil.getNearestShop()?.minimumAmountForFreeDelivery!!.toFloat() - subtotal.toFloat()
                        SharedPreferenceUtil.getNearestShop()?.deliveryCharge?.toFloat() ?: 0.toFloat()
                    }
                }else {
                    SharedPreferenceUtil.getNearestShop()?.deliveryCharge?.toFloat() ?: 0.toFloat()
                }
            }
            val vatSdPercent = SharedPreferenceUtil.getNearestShop()?.vat?.toFloat() ?: 0.toFloat()
            val vatSd = (subtotal.toFloat() * vatSdPercent) / 100
            val discount = viewModel.getDiscountPrice()
            val total = subtotal.toFloat() + deliveryCost + vatSd
            val grandTotal = total + discount


            itemCost.text = String.format("%.2f", subtotal)
            totalDiscount.text = String.format("-%.2f", discount)
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

}