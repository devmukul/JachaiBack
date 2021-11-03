package com.jachai.jachaimart.ui.userlocation

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.jachai.jachai_driver.utils.JachaiLog
import com.jachai.jachaimart.R
import com.jachai.jachaimart.databinding.SelectUserLocationFragmentBinding
import com.jachai.jachaimart.ui.base.BaseFragment
import com.jachai.jachaimart.ui.userlocation.adapters.LocationListAdapter
import com.jachai.jachaimart.utils.SharedPreferenceUtil
import kotlinx.coroutines.launch

class SelectUserLocationFragment :
    BaseFragment<SelectUserLocationFragmentBinding>(R.layout.select_user_location_fragment),
    LocationListAdapter.LocationSelectedListener {

    companion object {
        fun newInstance() = SelectUserLocationFragment()
    }


    private lateinit var navController: NavController
    private lateinit var locationListAdapter: LocationListAdapter

    private val viewModel: SelectUserLocationViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(requireView())
        initView()
        subscribeObservers()
    }

    override fun initView() {
        viewModel.findAddress(null)

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

                    viewModel.findAddress(p0.toString())

                } else {
                    viewModel.findAddress(null)
                }

            }

        })

        binding.rvLocation.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            locationListAdapter =
                LocationListAdapter(requireContext(), this@SelectUserLocationFragment)
            adapter = locationListAdapter
        }
    }

    override fun subscribeObservers() {
        lifecycleScope.launch {
            viewModel.successResponseLiveData.observe(viewLifecycleOwner) {
                it?.let { it1 ->
                    locationListAdapter.setList(it1)


                }
                locationListAdapter.notifyDataSetChanged()


            }

            viewModel.successLocationDetailsResponseLiveData.observe(viewLifecycleOwner) {
                it?.let { it1 ->

                    binding.etPickUpLocation.setText(it1.primaryAddress)
                    SharedPreferenceUtil.setUserLocation(it1)
                    //navigate
                    val action =
                        SelectUserLocationFragmentDirections.actionSelectUserLocationFragmentToUserMapsFragment(
                            it1
                        )
                    navController.navigate(action)

                }
            }
        }
    }


    override fun onLocationSelectedListener(data: AutocompletePrediction) {
        JachaiLog.d(TAG, data.toString())
        viewModel.findPlaceDetailsByID(data.placeId, data)
    }


}