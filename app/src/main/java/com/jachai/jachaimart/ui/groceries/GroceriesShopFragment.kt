package com.jachai.jachaimart.ui.groceries

import android.app.AlertDialog
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.addCallback
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.navigation.NavigationView
import com.jachai.jachai_driver.utils.JachaiLog
import com.jachai.jachai_driver.utils.showLongToast
import com.jachai.jachai_driver.utils.showShortToast
import com.jachai.jachaimart.JachaiApplication
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
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import java.text.SimpleDateFormat
import java.util.*


class GroceriesShopFragment :
    BaseFragment<GroceriesShopFragmentBinding>(R.layout.groceries_shop_fragment),
    CategoryAdapter.Interaction, CategoryWithProductAdapter.Interaction,
    SavedUserLocationAdapter.Interaction {
    lateinit var savedUserLocationAdapter: SavedUserLocationAdapter

    companion object {
        fun newInstance() = GroceriesShopFragment()
    }

    private val viewModel: GroceriesShopViewModel by viewModels()
    private var mView: View? = null
    private var address: Address? = null

    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var categoryWithProductAdapter: CategoryWithProductAdapter
    private lateinit var navController: NavController
    private val args: GroceriesShopFragmentArgs by navArgs()

    private lateinit var categoryResponse: CategoryResponse


    lateinit var mDrawerToggle: ActionBarDrawerToggle

    private lateinit var shopID: String


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        if (isViewNull) {
            GlobalScope.async {
                if (SharedPreferenceUtil.getJCShopId() == null) {
                    showNoShopFoundAlert()
                } else {
                    shopID = SharedPreferenceUtil.getJCShopId().toString()
                    viewModel.requestForShopCategories(shopID)

                }
                viewModel.requestAllFavouriteProduct()
                viewModel.requestAllAddress()


                initRecyclerViews()
            }

        }

        initView()
        subscribeObservers()

        val callback = requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            AlertDialog.Builder(requireContext())
                .setTitle("Exit?")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Exit") { arg0, arg1 -> requireActivity().finish() }.create()
                .show()

        }

        mDrawerToggle = ActionBarDrawerToggle(
            activity,
            binding.drawerLayout,
            binding.toolbarMain.toolbar,
            R.string.drawer_open,
            R.string.drawer_close
        )
        mDrawerToggle.syncState()
        binding.toolbarMain.toolbar.setNavigationIcon(R.drawable.ic_hamburger_icon)

        val navView: NavigationView = binding.navView
        navView.setupWithNavController(navController)

        binding.layoutView.close.setOnClickListener {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        }

        binding.layoutView.favourite.setOnClickListener {
            val action =
                GroceriesShopFragmentDirections.actionGroceriesShopFragmentToFavouriteFragment()
            navController.navigate(action)
        }

        binding.layoutView.order.setOnClickListener {
            val action =
                GroceriesShopFragmentDirections.actionGroceriesShopFragmentToOrderFragment()
            navController.navigate(action)
        }

        binding.layoutView.logout.setOnClickListener {
            SharedPreferenceUtil.clearAllPreferences();
            JachaiApplication.mDatabase.clearAllTables()
            val action =
                GroceriesShopFragmentDirections.actionGroceriesShopFragmentToLoginFragment2()
            navController.navigate(action)
        }


    }

    private fun initRecyclerViews() {
        binding.apply {
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


    override fun onResume() {
        super.onResume()
        viewModel.getAllOrder()
//        viewModel.getCurrentOrderStatus()


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


        binding.apply {

            layoutView.mobileNo.text = SharedPreferenceUtil.getMobileNo()
            layoutView.name.text = SharedPreferenceUtil.getName()

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
            etSearchShops.setOnClickListener {
                val action =
                    GroceriesShopFragmentDirections.actionGroceriesShopFragmentToGroceriesSearchFragment()
                navController.navigate(action)
            }

            toolbarMain.back.setOnClickListener {

            }

            toolbarMain.frameLay.setOnClickListener {
                val action =
                    if (JachaiApplication.mDatabase.daoAccess().getProductOrdersSize() == 0) {
                        GroceriesShopFragmentDirections.actionGroceriesShopFragmentToEmptyCartFragment()
                    } else {
                        GroceriesShopFragmentDirections.actionGroceriesShopFragmentToCartFragment()
                    }
                navController.navigate(action)
            }





            orderBottom.root.setOnClickListener {
                if (JachaiApplication.mDatabase.daoAccess().getOnGoingOrderCount() >= 1) {
                    val action =
                        GroceriesShopFragmentDirections.actionGroceriesShopFragmentToOrderFragment()
                    navController.navigate(action)

                } else {
                    val action =
                        GroceriesShopFragmentDirections.actionGroceriesShopFragmentToOrderFragment()
                    navController.navigate(action)
                }

            }


        }


    }

    override fun subscribeObservers() {
        viewModel.successCategoryResponseLiveData.observe(viewLifecycleOwner) {

            if (it != null) {


                if (it.categories?.isEmpty() == false) {
                    initRecyclerViews()
                    categoryResponse = it
                    categoryAdapter.setList(it.categories)
                    categoryAdapter.notifyDataSetChanged()
                    showLoader()
                    viewModel.requestForShopCategoryWithRelatedProduct(it.categories, shopID)



                } else {
                    showLongToast("No Product found. Empty Shop.")
                }
            } else {
//                showLongToast("No Product found. Empty Shop.")
            }


        }

        viewModel.errorCategoryWithProductResponseLiveData.observe(viewLifecycleOwner) {
            dismissLoader()
            it?.let { it1 -> showShortToast(it1) }
        }

        viewModel.successCategoryWithProductResponseLiveData.observe(viewLifecycleOwner) {
            dismissLoader()
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
                if (SharedPreferenceUtil.getJCShopId() == null) {
                    showNoShopFoundAlert()
                    initRecyclerViews()
                } else {
                    shopID = SharedPreferenceUtil.getJCShopId().toString()
                    viewModel.requestForShopCategories(shopID)
                }
            } else {
                if (SharedPreferenceUtil.getJCShopId() == null) {

                    showNoShopFoundAlert()
                    initRecyclerViews()
                } else {
                    shopID = SharedPreferenceUtil.getJCShopId().toString()
                    viewModel.requestForShopCategories(shopID)
                }
            }
            initView()


        }

        viewModel.successOnGoingOrderFound.observe(viewLifecycleOwner) {
            binding.apply {
                if (it > 0) {
                    orderBottom.root.visibility = View.VISIBLE
                    val order = JachaiApplication.mDatabase.daoAccess().getLastOnGoingOrder()
                    JachaiLog.e(TAG, order.toString())
                    if (order != null) {
                        orderBottom.inProgressText.text = "$it orders in progress"

                        val format =
                            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.getDefault())
                        val date: Date = format.parse(order.createdAt)
                        val myFormat = SimpleDateFormat("dd-MM HH:mm a ", Locale.getDefault())


                        orderBottom.orderTime.text = myFormat.format(date).toString()

                        orderBottom.shopName.text = order.shop?.name
                    }
                } else {
                    orderBottom.root.visibility = View.GONE
                }
            }


        }


        viewModel.successOnGoingOrderListLiveData.observe(viewLifecycleOwner) {
            viewModel.getCurrentOrderStatus()
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
                ?.let { it1 -> viewModel.getNearestJCShop(it1.location, false, null) }
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
                JachaiApplication.mDatabase.daoAccess().getProductOrdersSize().toString()

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

    override fun onAddressDeletedListener(data: List<Address>, position: Int) {

    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        mDrawerToggle.onConfigurationChanged(newConfig)
    }


}