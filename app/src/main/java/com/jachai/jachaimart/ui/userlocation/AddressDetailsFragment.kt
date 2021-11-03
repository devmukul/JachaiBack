package com.jachai.jachaimart.ui.userlocation

import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.jachai.jachai_driver.utils.showLongToast
import com.jachai.jachaimart.R
import com.jachai.jachaimart.databinding.AddressDetailsFragmentBinding
import com.jachai.jachaimart.model.response.address.Location
import com.jachai.jachaimart.model.response.location.LocationDetails
import com.jachai.jachaimart.ui.base.BaseFragment
import com.jachai.jachaimart.utils.HttpStatusCode

class AddressDetailsFragment :
    BaseFragment<AddressDetailsFragmentBinding>(R.layout.address_details_fragment) {
    private lateinit var navController: NavController


    companion object {
        fun newInstance() = AddressDetailsFragment()
    }

    private val viewModel: AddressDetailsViewModel by viewModels()
    private val args: AddressDetailsFragmentArgs by navArgs()
    private lateinit var locationDetails: LocationDetails

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        if (args.locationDetails != null) {
            locationDetails = args.locationDetails!!
            initView()
            subscribeObservers()
        }


    }

    override fun initView() {
        binding.apply {
            toolbarMain.title.text = "Save Address"
            toolbarMain.back.setOnClickListener {
                navController.popBackStack()
            }
            tvAddress.text = locationDetails.fullAddress


            confrimButton.setOnClickListener {
                val id = radioGroup.checkedRadioButtonId
                val rb = view?.findViewById<RadioButton>(id)

                locationDetails.name = rb?.text.toString()
                val favLocation = Location(locationDetails.latitude, locationDetails.longitude)
                locationDetails.favLocation = favLocation

                locationDetails.primaryAddress = apartmentNo.text.toString()
                locationDetails.secondaryAddress = streetNo.text.toString()
                viewModel.saveLocationAddress(locationDetails)
                showLoader()


            }

        }


    }

    override fun subscribeObservers() {
        viewModel.successResponseLiveData.observe(viewLifecycleOwner){
            dismissLoader()

        }
        viewModel.errorResponseLiveData.observe(viewLifecycleOwner){
            dismissLoader()
            if (it != null) {
                showLongToast(it)
            }
        }

    }


}