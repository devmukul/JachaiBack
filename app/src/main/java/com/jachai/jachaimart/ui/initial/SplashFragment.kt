package com.jachai.jachaimart.ui.initial

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.jachai.jachaimart.R
import com.jachai.jachaimart.databinding.SplashFragmentBinding
import com.jachai.jachaimart.model.response.address.Address
import com.jachai.jachaimart.model.response.address.Location
import com.jachai.jachaimart.ui.base.BaseFragment
import com.jachai.jachaimart.utils.SharedPreferenceUtil

class SplashFragment : BaseFragment<SplashFragmentBinding>(R.layout.splash_fragment) {

    companion object {
        fun newInstance() = SplashFragment()
    }

    private val viewModel: SplashViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        initView()
        subscribeObservers()

        if (SharedPreferenceUtil.isTokenAvailable()) {
            if(SharedPreferenceUtil.isNameAvailable() && SharedPreferenceUtil.isMobileAvailable())
                viewModel.getUserInfo()
            else
                viewModel.initSplashScreen()
        } else {
            viewModel.initSplashScreen()
        }


        viewModel.liveData.observe(viewLifecycleOwner, {

            when (it) {
                "seccess" -> view.findNavController().navigate(R.id.action_splashFragment2_to_groceriesShopFragment)
                "login" -> view.findNavController().navigate(R.id.splash_to_login)
            }
        })


    }

    override fun initView() {
        binding.apply {
            fetchCurrentLocation {

                val mAddress = it?.address ?: "n/a"
                val location = Location(it?.latitude, it?.longitude)
                val cAddress = Address(
                    mAddress,
                    "0",
                    location = location,
                    "Current Location",
                    "0",
                    mAddress,
                    mAddress,
                    true
                )
                SharedPreferenceUtil.saveCurrentAddress(cAddress)
            }
        }
    }

    override fun subscribeObservers() {
        viewModel.successUserAddressData.observe(viewLifecycleOwner){
            val mAddress = it?.address ?: "n/a"
            val location = Location(it?.latitude, it?.longitude)

            val address = Address(
                mAddress,
                "0",
                location = location,
                "Current Location",
                "0",
                mAddress,
                mAddress,
                true
            )

            SharedPreferenceUtil.saveCurrentAddress(address)


            if (SharedPreferenceUtil.isConfirmDeliveryAddress()) {
                SharedPreferenceUtil.getDeliveryAddress()?.let { it1 ->
                    viewModel.getNearestJCShop(
                        it1.location, false, null
                    )
                }
            } else {
                viewModel.getNearestJCShop(address.location, false, null)
            }
        }
    }

    override fun onResume() {
        super.onResume()

        fetchCurrentLocation {
            it?.let { it1 -> viewModel.getAddressFromLatLan(requireContext(), it1) }

        }


    }
}