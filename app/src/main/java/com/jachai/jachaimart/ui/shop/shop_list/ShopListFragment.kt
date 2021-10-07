package com.jachai.jachaimart.ui.shop.shop_list

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jachai.jachaimart.R

class ShopListFragment : Fragment() {

    companion object {
        fun newInstance() = ShopListFragment()
    }

    private lateinit var viewModel: ShopListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.shop_list_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ShopListViewModel::class.java)
        // TODO: Use the ViewModel
    }

}