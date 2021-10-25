package com.jachai.jachaimart.ui.cart

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.jachai.jachaimart.JachaiFoodApplication
import com.jachai.jachaimart.R
import com.jachai.jachaimart.databinding.CartFragmentBinding
import com.jachai.jachaimart.model.order.ProductOrder
import com.jachai.jachaimart.ui.base.BaseFragment
import com.jachai.jachaimart.ui.cart.adapter.CartAdapter

class CartFragment : BaseFragment<CartFragmentBinding>(R.layout.cart_fragment),
    CartAdapter.Interaction {

    companion object {
        fun newInstance() = CartFragment()
    }

    private lateinit var cartAdapter: CartAdapter
    private lateinit var navController: NavController

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
            recyclerView.apply {
                layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                cartAdapter = CartAdapter(requireContext(), emptyList(), this@CartFragment)
                adapter = cartAdapter
            }

            toolbarMain.clearCart.setOnClickListener {
                JachaiFoodApplication.mDatabase.daoAccess().clearOrderTable()
                navController.popBackStack()
            }

            bottomSheet.checkout.setOnClickListener {
//                navController.navigate(R.id.action_cartFragment_to_checkoutFragment)

                val action =
                    CartFragmentDirections.actionCartFragmentToCheckoutFragment(comment.text.toString())
                navController.navigate(action)
            }

        }


    }

    override fun subscribeObservers() {
        viewModel.successShopDetailsLiveData.observe(viewLifecycleOwner) {
            binding.apply {
                restraurantName.text = it.shopName
                restraurantSubtitle.text = it.shopSubtitle
                Glide.with(requireContext())
                    .load(it.shopImage)
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
                updateBottomCart()
            }
        }

    }

    override fun onQuantityItemAdded(item: ProductOrder?) {
        val qty = (item?.quantity?.toInt() ?: 0) + 1
        item?.quantity = qty
        viewModel.updateQuantity(item)


    }

    override fun onQuantityItemSubtraction(item: ProductOrder?) {
        val qty = (item?.quantity?.toInt() ?: 0) - 1
        item?.quantity = qty
        viewModel.updateQuantity(item)


    }

    private fun updateBottomCart() {
        binding.apply {

            bottomSheet.itemCount.text =
                JachaiFoodApplication.mDatabase.daoAccess().getProductOrdersSize()
                    .toString()
            bottomSheet.totalCount.text =
                JachaiFoodApplication.mDatabase.daoAccess().totalCost().toString()

        }

    }

}