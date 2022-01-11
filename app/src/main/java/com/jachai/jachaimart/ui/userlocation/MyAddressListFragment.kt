package com.jachai.jachaimart.ui.userlocation

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jachai.jachaimart.R
import com.jachai.jachaimart.databinding.MyAddressListFragmentBinding
import com.jachai.jachaimart.model.response.address.Address
import com.jachai.jachaimart.model.response.address.Location
import com.jachai.jachaimart.model.response.grocery.Shop
import com.jachai.jachaimart.ui.base.BaseFragment
import com.jachai.jachaimart.ui.userlocation.adapters.SavedUserLocationAdapter
import com.jachai.jachaimart.utils.SharedPreferenceUtil

class MyAddressListFragment :
    BaseFragment<MyAddressListFragmentBinding>(R.layout.my_address_list_fragment),
    SavedUserLocationAdapter.Interaction {
    private lateinit var navController: NavController

    private val args: MyAddressListFragmentArgs by navArgs()

    companion object {
        fun newInstance() = MyAddressListFragment()
    }

    lateinit var savedUserLocationAdapter: SavedUserLocationAdapter
    private val viewModel: MyAddressListViewModel by viewModels()
    lateinit var shopID: String
    lateinit var shop: Shop

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
                val action =
                    MyAddressListFragmentDirections.actionMyAddressListFragmentToUserMapsFragment(
                        null
                    )
                action.isFromFragment = true
                navController.navigate(action)
            }
        }

    }

    override fun subscribeObservers() {
        viewModel.successAddressResponseLiveData.observe(viewLifecycleOwner) {

            SharedPreferenceUtil.getCurrentAddress().let { it1 ->
                it1?.let { it2 -> it?.addresses?.add(it2) }
                if (it != null) {
                    showDataIntoList(it.addresses)
                }
            }


        }

        viewModel.successNearestJCShop.observe(viewLifecycleOwner) {
            if (!it?.shops.isNullOrEmpty()) {
//                JachaiLog.e(TAG, it?.shops?.get(0)?.id.toString())
                if (SharedPreferenceUtil.getJCHubId()?.equals(it?.shops?.get(0)?.id) == true) {
                    SharedPreferenceUtil.saveDeliveryAddress(address = it?.userDeliveryAddress!!)
                    goToCheckout()

                } else {
//                    JachaiLog.e(TAG, SharedPreferenceUtil.getJCShopId().toString())
                    showShopIsNotFoundAlert()
                }
            } else {
//                JachaiLog.e(TAG, SharedPreferenceUtil.getJCShopId().toString())
                showShopIsNotFoundAlert()
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

        val location = Location(data[position].location.latitude, data[position].location.longitude)
        viewModel.getNearestJCShop(location, true, data[position])

        SharedPreferenceUtil.saveAddressPosition(position)
        savedUserLocationAdapter.setList(data)
        savedUserLocationAdapter.notifyDataSetChanged()

    }

    override fun onAddressDeletedListener(data: List<Address>, position: Int) {

    }

    private fun goToCheckout() {
        val action =
            MyAddressListFragmentDirections.actionMyAddressListFragmentToCheckoutFragment(null)
        navController.navigate(action)

    }


    private fun showShopIsNotFoundAlert() {
        val builder = AlertDialog.Builder(context)
        builder.setCancelable(false)
        builder.setTitle(getString(R.string.app_name_short) + " alert")
        builder.setMessage("Sorry. Shop isn't delivered to this address.")


        builder.setNegativeButton("Close") { dialog, which ->
            dialog.dismiss()
        }
        builder.show()

    }


}