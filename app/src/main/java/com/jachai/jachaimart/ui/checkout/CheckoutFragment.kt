package com.jachai.jachaimart.ui.checkout

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import bd.com.evaly.ehealth.models.common.CurrentLocation
import com.jachai.jachaimart.R
import com.jachai.jachaimart.databinding.CheckoutFragmentBinding
import com.jachai.jachaimart.ui.base.BaseFragment

class CheckoutFragment : BaseFragment<CheckoutFragmentBinding>(R.layout.checkout_fragment) {

    companion object {
        fun newInstance() = CheckoutFragment()
    }

    private val viewModel: CheckoutViewModel by viewModels()
    private val args: CheckoutFragmentArgs by navArgs()

    private lateinit var additionalComment: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        additionalComment = args.addNote.toString()
        initView()
        subscribeObservers()
    }

    override fun initView() {
        binding.apply {
            bottomSheet.checkout.text = getString(R.string.place_order)

            bottomSheet.checkout.setOnClickListener {

                fetchCurrentLocation { location: CurrentLocation? ->
                    location?.let { it ->
                        viewModel.placeOrder(additionalComment, it)
                    }
                }

            }
        }


    }

    override fun subscribeObservers() {

    }


}