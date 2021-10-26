package com.jachai.jachaimart.ui.groceries

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jachai.jachaimart.R

class GroceriesShopFragment : Fragment() {

    companion object {
        fun newInstance() = GroceriesShopFragment()
    }

    private lateinit var viewModel: GroceriesShopViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.groceries_shop_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(GroceriesShopViewModel::class.java)
        // TODO: Use the ViewModel
    }

}