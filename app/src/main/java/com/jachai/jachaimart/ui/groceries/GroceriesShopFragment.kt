package com.jachai.jachaimart.ui.groceries

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.jachai.jachaimart.R
import com.jachai.jachaimart.databinding.GroceriesShopFragmentBinding
import com.jachai.jachaimart.ui.base.BaseFragment

class GroceriesShopFragment :
    BaseFragment<GroceriesShopFragmentBinding>(R.layout.groceries_shop_fragment) {

    companion object {
        fun newInstance() = GroceriesShopFragment()
    }

    private val viewModel: GroceriesShopViewModel by viewModels()

    private lateinit var navController: NavController
    private val args: GroceriesShopFragmentArgs by navArgs()

    private lateinit var shopID: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
//        shopID = args.shopID.toString()
        shopID = "6178d9a7f49bc5270f4ba5f0"

        initView()
        subscribeObservers()

    }

    override fun initView() {

    }

    override fun subscribeObservers() {

    }


}