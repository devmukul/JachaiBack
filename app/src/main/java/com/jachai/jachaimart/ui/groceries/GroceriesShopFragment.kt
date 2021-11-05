package com.jachai.jachaimart.ui.groceries

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.jachai.jachai_driver.utils.JachaiLog
import com.jachai.jachai_driver.utils.showLongToast
import com.jachai.jachaimart.JachaiFoodApplication
import com.jachai.jachaimart.R
import com.jachai.jachaimart.databinding.GroceriesShopFragmentBinding
import com.jachai.jachaimart.model.response.address.Address
import com.jachai.jachaimart.model.response.address.Location
import com.jachai.jachaimart.model.response.category.CatWithRelatedProduct
import com.jachai.jachaimart.model.response.category.Product
import com.jachai.jachaimart.model.response.home.CategoriesItem
import com.jachai.jachaimart.model.response.home.CategoryResponse
import com.jachai.jachaimart.ui.base.BaseFragment
import com.jachai.jachaimart.ui.groceries.adapters.CategoryWithProductAdapter
import com.jachai.jachaimart.ui.home.adapters.CategoryAdapter
import com.jachai.jachaimart.ui.userlocation.adapters.SavedUserLocationAdapter
import com.jachai.jachaimart.utils.SharedPreferenceUtil

class GroceriesShopFragment :
    BaseFragment<GroceriesShopFragmentBinding>(R.layout.groceries_shop_fragment),
    CategoryAdapter.Interaction, CategoryWithProductAdapter.Interaction,
    SavedUserLocationAdapter.Interaction {
    lateinit var savedUserLocationAdapter: SavedUserLocationAdapter

    companion object {
        fun newInstance() = GroceriesShopFragment()
    }

    private val viewModel: GroceriesShopViewModel by viewModels()

    private var address: Address? = null

    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var categoryWithProductAdapter: CategoryWithProductAdapter
    private lateinit var navController: NavController
    private val args: GroceriesShopFragmentArgs by navArgs()

    private lateinit var categoryResponse: CategoryResponse

    private lateinit var shopID: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        initView()
        subscribeObservers()


    }

    private fun showNoShopFoundAlert() {
        val builder = AlertDialog.Builder(context)
        builder.setCancelable(false)
        builder.setTitle(getString(R.string.app_name_short) + " alert")
        builder.setMessage("Sorry !! Shop is not available at your location right now. We are coming soon. Thanks")


        builder.setNegativeButton("Close") { dialog, which ->
            dialog.dismiss()
        }
        builder.show()

    }




    override fun initView() {
        if (SharedPreferenceUtil.getJCShopId() == null) {
            showNoShopFoundAlert()
        } else {
            shopID = SharedPreferenceUtil.getJCShopId().toString()
            viewModel.requestForShopCategories(shopID)
        }
        viewModel.requestAllFavouriteProduct()
        viewModel.requestAllAddress()

        binding.apply {
            fetchCurrentLocation {
//                toolbarMain.title.text = "Current Location"
                val mAddress = it?.address ?: "n/a"
//                toolbarMain.locationAddress.text = mAddress
                var location = Location(it?.latitude, it?.longitude)

                address = Address(
                    mAddress,
                    "0",
                    location = location,
                    "Current Location",
                    "0",
                    mAddress,
                    mAddress,
                    true
                )

                SharedPreferenceUtil.saveCurrentAddress(address!!)

            }

            initTopView()



            searchBar.root.setOnClickListener {
                val action =
                    GroceriesShopFragmentDirections.actionGroceriesShopFragmentToGroceriesSearchFragment()
                navController.navigate(action)
            }

            toolbarMain.back.setOnClickListener {
                navController.popBackStack()
            }

            toolbarMain.frameLay.setOnClickListener {
                val action =
                    if (JachaiFoodApplication.mDatabase.daoAccess().getProductOrdersSize() == 0) {
                        GroceriesShopFragmentDirections.actionGroceriesShopFragmentToEmptyCartFragment()
                    } else {
                        GroceriesShopFragmentDirections.actionGroceriesShopFragmentToCartFragment()
                    }
                navController.navigate(action)
            }




            rvCategories1.apply {
                layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
                categoryAdapter =
                    CategoryAdapter(requireContext(), emptyList(), this@GroceriesShopFragment)
                adapter = categoryAdapter
            }
            rvCategories.apply {
                layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                categoryWithProductAdapter =
                    CategoryWithProductAdapter(
                        requireContext(),
                        emptyList(),
                        this@GroceriesShopFragment
                    )
                adapter = categoryWithProductAdapter
            }


        }

    }

    override fun subscribeObservers() {
        viewModel.successCategoryResponseLiveData.observe(viewLifecycleOwner) {
            if (it != null) {


                if (it.categories?.isEmpty() == false) {
                    categoryResponse = it
                    categoryAdapter.setList(it.categories)
                    categoryAdapter.notifyDataSetChanged()
                    viewModel.requestForShopCategoryWithRelatedProduct(it.categories, shopID)


                } else {
                    showLongToast("No Product found. Empty Shop.")
                }
            } else {
//                showLongToast("No Product found. Empty Shop.")
            }


        }

        viewModel.successCategoryWithProductResponseLiveData.observe(viewLifecycleOwner) {
            categoryWithProductAdapter.setList(it?.catWithRelatedProducts)
            categoryWithProductAdapter.notifyDataSetChanged()
        }

        viewModel.successAddressResponseLiveData.observe(viewLifecycleOwner) {
            if (!SharedPreferenceUtil.isConfirmDeliveryAddress()) {
                SharedPreferenceUtil.getCurrentAddress().let { it1 ->
                    it1?.let { it2 -> it?.addresses?.add(it2) }
                    if (it != null) {
                        showBottomSheetDialog(it.addresses)
                    }
                }
            }

        }
        viewModel.successNearestJCShopUpdate.observe(viewLifecycleOwner) {
            if (it == true) {
                viewModel.successCategoryResponseLiveData.value = null
            }
            initView()
        }


    }


    override fun onCategoryItemSelected(position: Int, item: CategoriesItem?) {
        val action =
            GroceriesShopFragmentDirections.actionGroceriesShopFragmentToGroceryCategoryDetailsFragment()
        action.categoryId = item?.id
        action.categoryList = categoryResponse
        navController.navigate(action)
    }

    override fun onCategoryViewAllSelected(catWithRelatedProduct: CatWithRelatedProduct?) {
        val action =
            GroceriesShopFragmentDirections.actionGroceriesShopFragmentToGroceryCategoryDetailsFragment()
        action.categoryId = catWithRelatedProduct?.categoryId
        action.categoryList = categoryResponse
        navController.navigate(action)


    }

    override fun onCategoryProductSelected(product: Product?) {
        val action =
            GroceriesShopFragmentDirections.actionGroceriesShopFragmentToProductDetailsFragment()
        action.productId = product?.slug
        navController.navigate(action)
    }


    fun showBottomSheetDialog(
        item: MutableList<Address>
    ) {
        var bottomSheetDialog = BottomSheetDialog(requireContext())
        bottomSheetDialog?.setContentView(R.layout.bottom_sheet_location_selecter)

        val rvSavedAddress = bottomSheetDialog?.findViewById<RecyclerView>(R.id.rvSavedAddress)
        val addAddress = bottomSheetDialog?.findViewById<TextView>(R.id.addNewAddress)
        val confirm = bottomSheetDialog?.findViewById<Button>(R.id.confirm_button)

        if (SharedPreferenceUtil.getDeliveryAddress() != null) {
            for (i in item.indices) {
                item[i].isSelected =
                    SharedPreferenceUtil.getDeliveryAddress()?.id ?: 0 == item[i].id
            }
        }



        rvSavedAddress?.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            savedUserLocationAdapter =
                SavedUserLocationAdapter(
                    requireContext(),
                    this@GroceriesShopFragment,
                    item.asReversed()
                )
            adapter = savedUserLocationAdapter
        }
        bottomSheetDialog.show()
        addAddress?.setOnClickListener {
            bottomSheetDialog.dismiss()
            bottomSheetDialog.cancel()

            viewModel.successAddressResponseLiveData.value = null
            val action = GroceriesShopFragmentDirections
                .actionGroceriesShopFragmentToUserMapsFragment(null)
            navController.navigate(action)

        }
        confirm?.setOnClickListener {
            bottomSheetDialog.dismiss()
            bottomSheetDialog.cancel()

            viewModel.successAddressResponseLiveData.value = null
            SharedPreferenceUtil.setConfirmDeliveryAddress(true)
            initTopView()
            SharedPreferenceUtil.getDeliveryAddress()
                ?.let { it1 -> viewModel.getNearestJCShop(it1.location) }
        }


    }

    override fun onPause() {
        super.onPause()
        viewModel.successAddressResponseLiveData.value = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.successAddressResponseLiveData.value = null
    }

    private fun initTopView() {
        binding.apply {

            address = if (SharedPreferenceUtil.getDeliveryAddress() == null) {
                SharedPreferenceUtil.getCurrentAddress()
            } else {
                SharedPreferenceUtil.getDeliveryAddress()
            }


            toolbarMain.title.text = address?.name
            toolbarMain.locationAddress.text = address?.fullAddress
            toolbarMain.cartBadge.text =
                JachaiFoodApplication.mDatabase.daoAccess().getProductOrdersSize().toString()

            toolbarMain.clHome.setOnClickListener {
                SharedPreferenceUtil.setConfirmDeliveryAddress(false)
                viewModel.requestAllAddress()
            }
        }
    }

    override fun onAddressSelectedListener(data: List<Address>, position: Int) {
        JachaiLog.e(TAG, data[position].fullAddress)

        for (i in data.indices) {
            data[i].isSelected = i == position
        }
        SharedPreferenceUtil.saveAddressPosition(position)
        SharedPreferenceUtil.saveDeliveryAddress(data[position])
        savedUserLocationAdapter.setList(data)
        savedUserLocationAdapter.notifyDataSetChanged()


    }


}


