package com.jachai.jachaimart.ui.groceries

import android.app.AlertDialog
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.addCallback
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.navigation.NavigationView
import com.google.firebase.messaging.FirebaseMessaging
import com.jachai.jachai_driver.utils.JachaiLog
import com.jachai.jachai_driver.utils.showLongToast
import com.jachai.jachai_driver.utils.showShortToast
import com.jachai.jachaimart.BuildConfig
import com.jachai.jachaimart.JachaiApplication
import com.jachai.jachaimart.R
import com.jachai.jachaimart.databinding.GroceriesShopFragmentBinding
import com.jachai.jachaimart.model.response.address.Address
import com.jachai.jachaimart.model.response.address.Location
import com.jachai.jachaimart.model.response.category.CatWithRelatedProduct
import com.jachai.jachaimart.model.response.home.CategoriesItem
import com.jachai.jachaimart.model.response.home.CategoryResponse
import com.jachai.jachaimart.model.response.product.Product
import com.jachai.jachaimart.ui.base.BaseFragment
import com.jachai.jachaimart.ui.groceries.adapters.CategoryWithProductAdapter
import com.jachai.jachaimart.ui.groceries.adapters.CategoryWithProductPaginAdapter
import com.jachai.jachaimart.ui.home.adapters.CategoryAdapter
import com.jachai.jachaimart.ui.userlocation.adapters.SavedUserLocationAdapter
import com.jachai.jachaimart.utils.SharedPreferenceUtil
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*


class GroceriesShopFragment :
    BaseFragment<GroceriesShopFragmentBinding>(R.layout.groceries_shop_fragment),
    CategoryAdapter.Interaction, CategoryWithProductAdapter.Interaction,
    SavedUserLocationAdapter.Interaction, CategoryWithProductPaginAdapter.Interaction {
    lateinit var savedUserLocationAdapter: SavedUserLocationAdapter

    companion object {
        fun newInstance() = GroceriesShopFragment()
    }

    private val viewModel: GroceriesShopViewModel by viewModels()
    private var address: Address? = null

    private lateinit var categoryAdapter: CategoryAdapter

    //    private lateinit var categoryWithProductAdapter: CategoryWithProductAdapter
    private lateinit var navController: NavController

    private lateinit var categoryResponse: CategoryResponse


    private lateinit var mDrawerToggle: ActionBarDrawerToggle

    private var shopID: String = ""
    private lateinit var categoryWithProductPaginAdapter: CategoryWithProductPaginAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@OnCompleteListener
            }
            val token = task.result
           viewModel.requestRegister (requireActivity(), token)
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscribeToFCM(BuildConfig.TOPIC_NOT)

        navController = Navigation.findNavController(view)

        if (isViewNull) {
            GlobalScope.async {
                initRecyclerViews()
                if (SharedPreferenceUtil.getJCShopId() == null) {
                    showNoShopFoundAlert()
                } else {
                    shopID = SharedPreferenceUtil.getJCShopId().toString()
                    viewModel.requestForShopCategories(shopID)
                    loadCatWithProducts(shopID)
                }
                viewModel.requestAllFavouriteProduct()
                viewModel.requestAllAddress()
            }
        }


        initView()
        subscribeObservers()

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            AlertDialog.Builder(requireContext())
                .setTitle("Exit?")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Exit") { _, _ -> requireActivity().finish() }.create()
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

        binding.refresh.setOnRefreshListener {
            viewModel.requestForShopCategories(shopID)
            loadCatWithProducts(shopID)
        }


    }

    private fun loadCatWithProducts(shopID: String) {
        lifecycleScope.launch {
            viewModel.requestForShopCategoryWithRelatedProduct(shopID).collectLatest {
                categoryWithProductPaginAdapter.submitData(it)
            }
            categoryWithProductPaginAdapter.loadStateFlow.collectLatest { loadStates ->
                JachaiLog.d(TAG, "loadStates $loadStates")
                when (loadStates.refresh) {
                    is LoadState.Loading -> {
                        binding.refresh.isRefreshing = true
                    }
                    !is LoadState.Loading -> {
                        binding.refresh.isRefreshing = false
                    }
                    !is LoadState.Error -> {
                        binding.refresh.isRefreshing = false
                        JachaiLog.d(TAG, "Error message")
                    }
                }

            }
        }
        categoryWithProductPaginAdapter.addLoadStateListener { loadState ->
            try {
                binding.refresh.isRefreshing = loadState.refresh == LoadState.Loading

                val errorState = when {
                    loadState.append is LoadState.Error -> loadState.append as LoadState.Error
                    loadState.prepend is LoadState.Error -> loadState.prepend as LoadState.Error
                    loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error
                    else -> null
                }
                errorState?.let {
                    JachaiLog.d(TAG, it.error.toString())
                    showShortToast(it.error.toString())

                }
            } catch (exception: Exception) {
                exception.message?.let { it1 -> JachaiLog.d(TAG, it1) }
            }
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
//            rvCategories.apply {
//                layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
//                categoryWithProductAdapter =
//                    CategoryWithProductAdapter(
//                        requireContext(),
//                        emptyList(),
//                        this@GroceriesShopFragment
//                    )
//                adapter = categoryWithProductAdapter
//            }
            categoryWithProductPaginAdapter =
                CategoryWithProductPaginAdapter(requireContext(), this@GroceriesShopFragment)

            rvCategories.apply {
                layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                adapter = categoryWithProductPaginAdapter
            }


        }
    }


    override fun onResume() {
        super.onResume()
        viewModel.getAllOrder()
    }

    private fun showNoShopFoundAlert() {
        val builder = AlertDialog.Builder(context)
        builder.setCancelable(false)
        builder.setTitle(getString(R.string.app_name_short) + " alert")
        builder.setMessage("Sorry !! Shop is not available at your location right now. We are coming soon. Thanks")


        builder.setNegativeButton("Close") { dialog, _ ->
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
                val location = Location(it?.latitude, it?.longitude)

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

                if(!SharedPreferenceUtil.isJCShopAvailable()){
                    if (SharedPreferenceUtil.isConfirmDeliveryAddress()) {
                        SharedPreferenceUtil.getDeliveryAddress()?.let { it1 ->
                            viewModel.getNearestJCShop(
                                it1.location, false, null
                            )
                        }
                    } else {
                        viewModel.getNearestJCShop(address!!.location, false, null)
                    }
                }

            }

            initTopView()
            etSearchShops.setOnClickListener {

                if (SharedPreferenceUtil.getJCShopId().isNullOrEmpty()) {
                    showNoShopFoundAlert()
                } else {
                    val action =
                        GroceriesShopFragmentDirections.actionGroceriesShopFragmentToGroceriesSearchFragment(SharedPreferenceUtil.getJCShopId()!!)
                    navController.navigate(action)
                }
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
//                    initRecyclerViews()
                    categoryResponse = it
                    categoryAdapter.setList(it.categories)
                    categoryAdapter.notifyDataSetChanged()
//                    showLoader()
//                    viewModel.requestForShopCategoryWithRelatedProduct(it.categories, shopID)


                } else {
                    showLongToast("No Product found. Empty Shop.")
                }
            }
//            else {
//                showLongToast("No Product found. Empty Shop.")
//            }


        }

        viewModel.errorCategoryWithProductResponseLiveData.observe(viewLifecycleOwner) {
            dismissLoader()
            it?.let { it1 -> showShortToast(it1) }
        }

//        viewModel.successCategoryWithProductResponseLiveData.observe(viewLifecycleOwner) {
//            dismissLoader()
//            categoryWithProductAdapter.setList(it?.catWithRelatedProducts)
//            categoryWithProductAdapter.notifyDataSetChanged()
//        }

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
                    loadCatWithProducts(shopID)
                }
            } else {
                if (SharedPreferenceUtil.getJCShopId() == null) {

                    showNoShopFoundAlert()
                    initRecyclerViews()
                } else {
                    shopID = SharedPreferenceUtil.getJCShopId().toString()
                    viewModel.requestForShopCategories(shopID)
                    loadCatWithProducts(shopID)
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
                    orderBottom.inProgressText.text = "$it orders in progress"

                    orderBottom.orderTime.text =
                        order.createdAt?.let { it1 -> getDateFormatter(it1) }

                    orderBottom.shopName.text = order.shop?.name
                } else {
                    orderBottom.root.visibility = View.GONE
                }
            }


        }


        viewModel.successOnGoingOrderListLiveData.observe(viewLifecycleOwner) {
            viewModel.getCurrentOrderStatus()
        }

        viewModel.isRefresh.observe(viewLifecycleOwner) { isRefresh ->
            isRefresh?.let {
                binding.refresh.isRefreshing = it
            }
        }
        viewModel.successAddToCartData.observe(viewLifecycleOwner, {
            binding.apply {
                if (it == true) {

                    toolbarMain.cartBadge.text =
                        JachaiApplication.mDatabase.daoAccess().getProductOrdersSize()
                            .toString()


                }
            }

        })


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

    override fun onProductAddToCart(product: Product?) {
        product?.let { it1 ->

            if (product.shop?.id?.let {
                    JachaiApplication.mDatabase.daoAccess()
                        .isInsertionApplicable(shopID = it)
                } == true) {
//
                if (JachaiApplication.mDatabase.daoAccess()
                        .getProductByProductID(product.id!!, product.shop.id) > 0
                ) {

                    showShortToast("Product already added")

                } else {

                    showShortToast("Product added")
                }
                viewModel.saveProduct(it1, 1, product.shop, true)

            } else {
                alertDialog(product, 1)
            }


        }
//        updateBottomCart()
    }


    private fun showBottomSheetDialog(
        item: MutableList<Address>
    ) {
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_location_selecter)

        val rvSavedAddress = bottomSheetDialog.findViewById<RecyclerView>(R.id.rvSavedAddress)
        val addAddress = bottomSheetDialog.findViewById<TextView>(R.id.addNewAddress)
        val confirm = bottomSheetDialog.findViewById<Button>(R.id.confirm_button)

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
            initRecyclerViews()
            initTopView()

            if (SharedPreferenceUtil.getDeliveryAddress() != null) {
                SharedPreferenceUtil.getDeliveryAddress()
                    ?.let { it1 ->
                        viewModel.getNearestJCShop(it1.location, false, null)
                    }
            } else {
                SharedPreferenceUtil.getCurrentAddress()
                    ?.let { it1 ->
                        viewModel.getNearestJCShop(it1.location, false, null)
                    }
            }


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

    private fun getDateFormatter(date: String): String {
        val inputFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        inputFormatter.timeZone = TimeZone.getTimeZone("UTC")
        val date = inputFormatter.parse(date)
        val outFormatter = SimpleDateFormat("dd MMM yyyy h:mm a ")
        return outFormatter.format(date)
    }
    fun subscribeToFCM(topic: String? = null, topicList: MutableList<String> = mutableListOf()) {
        topic?.let { topicList.add(it) }
        for(mTopic in topicList){
            FirebaseMessaging.getInstance().subscribeToTopic(mTopic).addOnSuccessListener {
            }
        }
    }
    private fun alertDialog(product: Product, quantity: Int) {
        val builder = AlertDialog.Builder(context)
        builder.setCancelable(false)
        builder.setTitle(getString(R.string.app_name_short) + " alert")
        builder.setMessage("Do have already selected products from a different shop. if you continue, your cart and selection will be removed")

        builder.setPositiveButton("Continue") { _, _ ->

            viewModel.saveProduct(product, quantity, product.shop, false)
        }

        builder.setNegativeButton("Close") { dialog, which ->
            dialog.dismiss()
        }
        builder.show()
    }


}