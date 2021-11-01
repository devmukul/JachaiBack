package com.jachai.jachaimart.ui.cart

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.jachai.jachaimart.R
import com.jachai.jachaimart.databinding.EmptyCartFragmentBinding
import com.jachai.jachaimart.ui.base.BaseFragment

class EmptyCartFragment : BaseFragment<EmptyCartFragmentBinding>(R.layout.empty_cart_fragment) {

    companion object {
        fun newInstance() = EmptyCartFragment()
    }

    private val viewModel: EmptyCartViewModel by viewModels()
    private lateinit var navController: NavController


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        initView()
        subscribeObservers()
    }

    override fun initView() {
        binding.apply {
            back.setOnClickListener {
                navController.popBackStack()

            }

            findProduct.setOnClickListener {
                navController.popBackStack()
            }
        }

    }

    override fun subscribeObservers() {

    }


}