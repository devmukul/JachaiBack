package com.jachai.jachaimart.ui.userlocation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jachai.jachaimart.R
import com.jachai.jachaimart.databinding.MyAddressListFragmentBinding
import com.jachai.jachaimart.model.response.address.Address
import com.jachai.jachaimart.ui.base.BaseFragment
import com.jachai.jachaimart.ui.userlocation.adapters.SavedUserLocationAdapter
import com.jachai.jachaimart.utils.SharedPreferenceUtil

class MyAddressListFragment :
    BaseFragment<MyAddressListFragmentBinding>(R.layout.my_address_list_fragment),
    SavedUserLocationAdapter.Interaction {
    private lateinit var navController: NavController

    companion object {
        fun newInstance() = MyAddressListFragment()
    }

    lateinit var savedUserLocationAdapter: SavedUserLocationAdapter
    private val viewModel: MyAddressListViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        initView()
        subscribeObservers()
    }

    override fun initView() {
        viewModel.requestAllAddress()
        binding.apply {
            toolbarMain.title.text = "Saved address"
            toolbarMain.back.setOnClickListener {
                goToCheckout()
            }

            rvAddress.apply {
                layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                savedUserLocationAdapter =
                    SavedUserLocationAdapter(
                        requireContext(),
                        this@MyAddressListFragment,
                        emptyList()
                    )
                adapter = savedUserLocationAdapter
            }

            confirmButton.setOnClickListener {
                val action =  MyAddressListFragmentDirections.actionMyAddressListFragmentToUserMapsFragment(null)
                action.isFromFragment = true
                navController.navigate(action)
            }
        }

    }

    override fun subscribeObservers() {
        viewModel.successAddressResponseLiveData.observe(viewLifecycleOwner) {

                SharedPreferenceUtil.getCurrentAddress().let { it1 ->
                    it?.addresses?.add(it1)
                    if (it != null) {
                        showDataIntoList(it.addresses)
                    }
                }


        }

    }

    private fun showDataIntoList(addresses: MutableList<Address>) {
        if (SharedPreferenceUtil.isConfirmDeliveryAddress()) {
            for (i in addresses.indices) {
                addresses[i].isSelected =
                    addresses[i].equals(SharedPreferenceUtil.getDeliveryAddress()?.id)

            }
        }
        savedUserLocationAdapter.setList(addresses)
        savedUserLocationAdapter.notifyDataSetChanged()
    }

    override fun onAddressSelectedListener(data: List<Address>, position: Int) {
        for (i in data.indices) {
            data[i].isSelected = i == position
        }
        SharedPreferenceUtil.saveAddressPosition(position)
        SharedPreferenceUtil.saveDeliveryAddress(data[position])
        savedUserLocationAdapter.setList(data)
        savedUserLocationAdapter.notifyDataSetChanged()
        goToCheckout()
    }

    private fun goToCheckout() {
        val action = MyAddressListFragmentDirections.actionMyAddressListFragmentToCheckoutFragment(null)
        navController.navigate(action)

    }


}