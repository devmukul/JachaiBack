package com.jachai.jachaimart.ui.addresses

import android.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jachai.jachaimart.R
import com.jachai.jachaimart.databinding.AddressFragmentBinding
import com.jachai.jachaimart.model.response.address.Address
import com.jachai.jachaimart.model.response.product.Product
import com.jachai.jachaimart.ui.base.BaseFragment
import com.jachai.jachaimart.ui.userlocation.adapters.SavedUserLocationAdapter

class AddressFragment : BaseFragment<AddressFragmentBinding>(R.layout.address_fragment),
    UserLocationListAdapter.Interaction {

    companion object {
        fun newInstance() = AddressFragment()
    }

    private lateinit var navController: NavController


    private val viewModel: AddressViewModel by viewModels()
    private lateinit var addressListAdapter: UserLocationListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        initView()
        subscribeObservers()
    }

    override fun initView() {
        viewModel.requestAllAddress()
        showLoader()

        binding.apply {
            toolbarMain.title.text = "Addresses"
            toolbarMain.back.setOnClickListener {
                onBackPressed()
            }
            rvAddress.apply {
                layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                addressListAdapter =
                    UserLocationListAdapter(requireContext(), this@AddressFragment,  emptyList())
                adapter = addressListAdapter
            }
        }


    }

    override fun subscribeObservers() {

        viewModel.successAddressResponseLiveData.observe(viewLifecycleOwner) {
            dismissLoader()
            addressListAdapter.setList(it!!.addresses)
            addressListAdapter.notifyDataSetChanged()
        }
        viewModel.errorOrderDetailsLiveData.observe(viewLifecycleOwner) {
            dismissLoader()
        }

        viewModel.deleteAddressResponseLiveData.observe(viewLifecycleOwner) {
            dismissLoader()
            addressListAdapter.setList(it!!.addresses)
            addressListAdapter.notifyDataSetChanged()
        }


    }

    override fun onAddressDeletedListener(id: String) {
        alertDialog(id)
    }

    private fun alertDialog(id: String) {
        val builder = AlertDialog.Builder(context)
        builder.setCancelable(false)
        builder.setTitle(getString(R.string.app_name_short) + " alert")
        builder.setMessage("Are you sure you want to delete?")

        builder.setPositiveButton("Delete") { _, _ ->

            showLoader()
            viewModel.deleteAddress(id)
        }

        builder.setNegativeButton("Cancel") { dialog, which ->
            dialog.dismiss()
        }
        builder.show()
    }

}