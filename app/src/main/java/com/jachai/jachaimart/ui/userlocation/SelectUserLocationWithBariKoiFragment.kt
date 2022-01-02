package com.jachai.jachaimart.ui.userlocation


import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jachai.jachaimart.R
import com.jachai.jachaimart.databinding.SelectUserLocationFragmentBinding
import com.jachai.jachaimart.model.map.BariKoiPlace
import com.jachai.jachaimart.model.response.location.LocationDetails
import com.jachai.jachaimart.ui.base.BaseFragment
import com.jachai.jachaimart.ui.userlocation.adapters.BariKoiLocationListAdapter
import com.jachai.jachaimart.utils.SharedPreferenceUtil
import kotlinx.coroutines.launch

class SelectUserLocationWithBariKoiFragment :
    BaseFragment<SelectUserLocationFragmentBinding>(R.layout.select_user_location_fragment),
    BariKoiLocationListAdapter.LocationSelectedListener {

    companion object {
        fun newInstance() = SelectUserLocationWithBariKoiFragment()
    }

    private val args: SelectUserLocationWithBariKoiFragmentArgs by navArgs()

    private lateinit var navController: NavController
    private val viewModel: SelectUserLocationWithBariKoiViewModel by viewModels()
    private lateinit var bariKoiLocationListAdapter: BariKoiLocationListAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(requireView())
        initView()
        subscribeObservers()
    }

    override fun initView() {
        viewModel.findLocationAddress(null)
        binding.include.title.text = "Save Address"
        binding.include.back.setOnClickListener {
            navController.popBackStack()
        }
        binding.etPickUpLocation.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                if (p0.toString().trim().count() >= 4) {
                    viewModel.findLocationAddress(p0.toString())
                } else {
                    viewModel.findLocationAddress(null)
                }

            }

        })

        binding.rvLocation.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            bariKoiLocationListAdapter =
                BariKoiLocationListAdapter(
                    requireContext(),
                    this@SelectUserLocationWithBariKoiFragment
                )
            adapter = bariKoiLocationListAdapter
        }

    }

    override fun subscribeObservers() {
        lifecycleScope.launch {
            viewModel.successAuthCompleteResponseLiveData.observe(viewLifecycleOwner) {
                it?.let {
                    bariKoiLocationListAdapter.setList(it)
                }
                bariKoiLocationListAdapter.notifyDataSetChanged()

            }
        }

    }

    override fun onLocationSelectedListener(data: BariKoiPlace) {
        binding.etPickUpLocation.setText(data.address)

        val locationDetails = LocationDetails()


        locationDetails.primaryAddress = data.address.toString()
        locationDetails.fullAddress = data.address.toString()
        locationDetails.secondaryAddress = data.address.toString()

        locationDetails.latitude = data.location?.latitude
        locationDetails.longitude = data.location?.longitude
        locationDetails.placeId = null

        SharedPreferenceUtil.setUserLocation(locationDetails)
        val action =
            SelectUserLocationWithBariKoiFragmentDirections.actionSelectUserLocationWithBariKoiFragmentToUserMapsFragment(
                locationDetails
            )
        action.isFromFragment =  args.isFromFragment
        navController.navigate(action)
    }


}