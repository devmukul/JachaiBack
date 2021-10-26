package com.jachai.jachaimart.ui.groceries

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.jachai.jachaimart.R
import com.jachai.jachaimart.databinding.GroceriesShopFragmentBinding
import com.jachai.jachaimart.ui.base.BaseFragment

class GroceriesShopFragment :
    BaseFragment<GroceriesShopFragmentBinding>(R.layout.groceries_shop_fragment) {

    companion object {
        fun newInstance() = GroceriesShopFragment()
    }

    private val viewModel: GroceriesShopViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun initView() {

    }

    override fun subscribeObservers() {

    }


}