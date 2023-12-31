package com.jachai.jachaimart.ui.groceries

import android.app.AlertDialog
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.addCallback
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricPrompt
import androidx.core.view.GravityCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.navigation.NavigationView
import com.google.firebase.messaging.FirebaseMessaging
import com.jachai.jachai_driver.utils.JachaiLog
import com.jachai.jachai_driver.utils.ToastUtils
import com.jachai.jachai_driver.utils.showLongToast
import com.jachai.jachai_driver.utils.showShortToast
import com.jachai.jachaimart.BuildConfig
import com.jachai.jachaimart.JachaiApplication
import com.jachai.jachaimart.R
import com.jachai.jachaimart.database.AppDatabase
import com.jachai.jachaimart.databinding.GroceriesShopFragmentBinding
import com.jachai.jachaimart.decorator.BiometricPromptUtils
import com.jachai.jachaimart.decorator.CryptographyManager
import com.jachai.jachaimart.model.request.JCService
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
import com.jachai.jachaimart.ui.home.adapters.ServiceAdapter
import com.jachai.jachaimart.ui.userlocation.adapters.SavedUserLocationAdapter
import com.jachai.jachaimart.utils.SharedPreferenceUtil
import com.jachai.jachaimart.utils.constant.CommonConstants
import com.jachai.jachaimart.utils.constant.SharedPreferenceConstants.CIPHERTEXT_MOBILE
import com.jachai.jachaimart.utils.constant.SharedPreferenceConstants.CIPHERTEXT_WRAPPER
import com.jachai.jachaimart.utils.constant.SharedPreferenceConstants.TAG_JACHAI_TOKEN
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem
import java.text.SimpleDateFormat
import java.util.*
import javax.crypto.Cipher


class GroceriesShopFragment :
    BaseFragment<GroceriesShopFragmentBinding>(R.layout.groceries_shop_fragment),
    CategoryAdapter.Interaction, CategoryWithProductAdapter.Interaction,
    SavedUserLocationAdapter.Interaction, CategoryWithProductPaginAdapter.Interaction,
    ServiceAdapter.Interaction {
    lateinit var savedUserLocationAdapter: SavedUserLocationAdapter
    private lateinit var bottomSheetDialog: BottomSheetDialog

    companion object {
        fun newInstance() = GroceriesShopFragment()
    }

    private val viewModel: GroceriesShopViewModel by viewModels()
    private var address: Address? = null

    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var serviceAdapter: ServiceAdapter

    //    private lateinit var categoryWithProductAdapter: CategoryWithProductAdapter
    private lateinit var navController: NavController

    private lateinit var categoryResponse: CategoryResponse


    private lateinit var mDrawerToggle: ActionBarDrawerToggle

    //    private var shopID: String = ""
//    private var hubID: String = ""
    private lateinit var categoryWithProductPaginAdapter: CategoryWithProductPaginAdapter

    private lateinit var chipper: Cipher

    private val cryptographyManager = CryptographyManager()

    private val cipherMobile
        get() = cryptographyManager.getMobileFromSharedPrefs(
            requireContext(),
            TAG_JACHAI_TOKEN,
            Context.MODE_PRIVATE,
            CIPHERTEXT_MOBILE
        )

    private val ciphertextWrapper
        get() = cryptographyManager.getCiphertextWrapperFromSharedPrefs(
            requireContext(),
            TAG_JACHAI_TOKEN,
            Context.MODE_PRIVATE,
            CIPHERTEXT_WRAPPER
        )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@OnCompleteListener
            }
            val token = task.result
            viewModel.requestRegister(requireActivity(), token)
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bottomSheetDialog = BottomSheetDialog(requireContext())
        val orderQty =
            AppDatabase.getAppDatabase(requireContext()).daoAccess().getProductOrdersSize()
        subscribeToFCM(BuildConfig.TOPIC_NOT)

        navController = Navigation.findNavController(view)

        viewModel.requestForBanners(CommonConstants.JC_MART_TYPE)


        if (!SharedPreferenceUtil.isFreshLogin()) {
            if (orderQty == 0 || isViewNull) {

                lifecycleScope.launch {
                    initRecyclerViews()
//                    if (SharedPreferenceUtil.getJCHubId() == null ) {
//                        showNoShopFoundAlert()
//                    }
                    if (SharedPreferenceUtil.getJCHubId() != null) {
//                    shopID = SharedPreferenceUtil.getJCShopId().toString()
                        val hubID = SharedPreferenceUtil.getJCHubId().toString()
//                    viewModel.requestForShopCategories(shopID)
                        viewModel.requestForShopCategories(hubID)
                        loadCatWithProducts(hubID)
                    }
                    viewModel.requestAllFavouriteProduct()
                    viewModel.requestAllAddress()
                }

            } else {
                try {
                    categoryWithProductPaginAdapter.notifyDataSetChanged()
                } catch (ex: Exception) {
                    JachaiLog.e(TAG, ex.localizedMessage)
                }

            }
        } else {
            initRecyclerViews()
            if (SharedPreferenceUtil.getJCHubId() != null) {
//                    shopID = SharedPreferenceUtil.getJCShopId().toString()
                val hubID = SharedPreferenceUtil.getJCHubId().toString()
//                    viewModel.requestForShopCategories(shopID)
                viewModel.requestForShopCategories(hubID)
                loadCatWithProducts(hubID)
            }
            viewModel.requestAllFavouriteProduct()
            viewModel.requestAllAddress()
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

        binding.layoutView.appVersion.text =
            "JaChai-" + BuildConfig.BUILD_TYPE + "-v" + BuildConfig.VERSION_NAME

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

        binding.layoutView.address.setOnClickListener {
            val action =
                GroceriesShopFragmentDirections.actionGroceriesShopFragmentToAddressFragment()
            navController.navigate(action)
        }

        binding.layoutView.touchId.setOnClickListener {
            if (ciphertextWrapper != null) {
                cryptographyManager.clearSharedPrefs(
                    requireContext(),
                    TAG_JACHAI_TOKEN,
                    Context.MODE_PRIVATE
                )
                binding.layoutView.switch1.isChecked = false
            } else {
                showBiometricPromptForEncryption()
            }
        }

        binding.layoutView.logout.setOnClickListener {
            SharedPreferenceUtil.clearAllPreferences();
            JachaiApplication.mDatabase.clearAllTables()
            val action =
                GroceriesShopFragmentDirections.actionGroceriesShopFragmentToLoginFragment2()
            navController.navigate(action)
        }

        binding.refresh.setOnRefreshListener {
            val hubID = SharedPreferenceUtil.getJCHubId()
            if (hubID == null) {
                showNoShopFoundAlert()
            } else {
                viewModel.requestForShopCategories(hubID)
                loadCatWithProducts(hubId = hubID)
            }
        }


    }

    private fun loadCatWithProducts(hubId: String) {
        lifecycleScope.launch {
            viewModel.requestForShopCategoryWithRelatedProduct(hubId).collectLatest {
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

            rvServices.apply {
                layoutManager = GridLayoutManager(context, 5)
                serviceAdapter =
                    ServiceAdapter(requireContext(), emptyList(), this@GroceriesShopFragment)
                adapter = serviceAdapter
            }
            viewModel.getAllServices()

            rvCategories1.apply {
                layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
                categoryAdapter =
                    CategoryAdapter(requireContext(), emptyList(), this@GroceriesShopFragment)
                adapter = categoryAdapter
            }
/*

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
*/
            categoryWithProductPaginAdapter =
                CategoryWithProductPaginAdapter(requireContext(), this@GroceriesShopFragment)

            rvCategories.apply {
                layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
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
        builder.setMessage("Sorry !! JaChai Mart is not available at your location right now. We are coming soon. Thanks")


        builder.setNegativeButton("Close") { dialog, _ ->
            dialog.dismiss()
        }
        builder.show()

    }

    override fun initView() {


        binding.apply {

            if (SharedPreferenceUtil.getMobileNo() != cipherMobile) {
                cryptographyManager.clearSharedPrefs(
                    requireContext(),
                    TAG_JACHAI_TOKEN,
                    Context.MODE_PRIVATE
                )
                layoutView.switch1.isChecked = false
            }


            layoutView.switch1.isChecked = ciphertextWrapper != null

            layoutView.mobileNo.text = SharedPreferenceUtil.getMobileNo()
            layoutView.name.text = SharedPreferenceUtil.getName()

            fetchCurrentLocation {
                it?.let { it1 -> viewModel.getAddressFromLatLan(requireContext(), it1) }

            }




            etSearchShops.setOnClickListener {

                if (SharedPreferenceUtil.getJCHubId().isNullOrEmpty()) {
                    showNoShopFoundAlert()
                } else {
                    val action =
                        GroceriesShopFragmentDirections.actionGroceriesShopFragmentToGroceriesSearchFragment(
                            SharedPreferenceUtil.getJCHubId()!!
                        )
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
        viewModel.successServiceListData.observe(viewLifecycleOwner) {
            serviceAdapter.setList(it)
            serviceAdapter.notifyDataSetChanged()
        }
        viewModel.successBannerResponseLiveData.observe(viewLifecycleOwner) { it ->
            try {
                val imageList = ArrayList<CarouselItem>()
                imageList.clear()
                it?.banners?.forEach {
                    imageList.add(CarouselItem(it?.bannerImage))
                }
                binding.imageSlider.addData(imageList)
            } catch (exp: Exception) {
                exp.printStackTrace()
            }


        }

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

        viewModel.successBiometricRegLiveData.observe(viewLifecycleOwner) {
            val encryptedServerTokenWrapper = cryptographyManager.encryptData(it!!.secret, chipper)
            cryptographyManager.persistCiphertextWrapperToSharedPrefs(
                encryptedServerTokenWrapper,
                requireContext(),
                TAG_JACHAI_TOKEN,
                Context.MODE_PRIVATE,
                CIPHERTEXT_WRAPPER
            )
            cryptographyManager.setMobile(
                SharedPreferenceUtil.getMobileNo()!!,
                requireContext(),
                TAG_JACHAI_TOKEN,
                Context.MODE_PRIVATE,
                CIPHERTEXT_MOBILE
            )

            binding.layoutView.switch1.isChecked = true
        }

//        viewModel.successCategoryWithProductResponseLiveData.observe(viewLifecycleOwner) {
//            dismissLoader()
//            categoryWithProductAdapter.setList(it?.catWithRelatedProducts)
//            categoryWithProductAdapter.notifyDataSetChanged()
//        }

        viewModel.successAddressResponseLiveData.observe(viewLifecycleOwner) {
            if (!SharedPreferenceUtil.isConfirmDeliveryAddress()) {
                SharedPreferenceUtil.getCurrentAddress().let { it1 ->
                    it1?.let { it2 ->
                        it?.addresses?.add(it2)
                    }
                    if (it != null) {
                        showBottomSheetDialog(it.addresses)
                    }
                }
            }

        }
        viewModel.successNearestJCShopUpdate.observe(viewLifecycleOwner) {
            if (it == true) {
                viewModel.successCategoryResponseLiveData.value = null
                val hubID = SharedPreferenceUtil.getJCHubId()
                if (hubID == null) {
                    showNoShopFoundAlert()
                    initRecyclerViews()
                } else {

                    viewModel.requestForShopCategories(hubID)
                    loadCatWithProducts(hubID)
                }
                initView()
            } else {
                val hubID = SharedPreferenceUtil.getJCHubId()
                if (hubID == null) {

                    showNoShopFoundAlert()
                    initRecyclerViews()
                } else {
                    viewModel.requestForShopCategories(hubID)
                    loadCatWithProducts(hubID)
                    initView()
                }
            }


        }

        viewModel.successOnGoingOrderFound.observe(viewLifecycleOwner) {
            binding.apply {
                if (it > 0) {
                    orderBottom.root.visibility = View.VISIBLE

                    val paddingDp = 52;
                    var density = requireContext().resources.displayMetrics.density
                    val paddingPixel: Int = (paddingDp * density).toInt()
                    mainLay.setPadding(0, 0, 0, paddingPixel)


                    val order = JachaiApplication.mDatabase.daoAccess().getLastBaseOrder()
                    JachaiLog.e(TAG, order.toString())
                    orderBottom.inProgressText.text = "$it orders in progress"

                    orderBottom.orderTime.text =
                        order.createdAt?.let { it1 -> getDateFormatter(it1) }

                    orderBottom.shopName.text = order.hubName

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
        viewModel.successAddToCartData.observe(viewLifecycleOwner) {
            binding.apply {
                if (it == true) {
                    toolbarMain.cartBadge.text =
                        JachaiApplication.mDatabase.daoAccess().getProductOrdersSize()
                            .toString()
                }
            }

        }

        viewModel.successUserAddressData.observe(viewLifecycleOwner) {
            val mAddress = it?.address ?: "n/a"
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

            if (!SharedPreferenceUtil.isJCHubAvailable()) {
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

            if (SharedPreferenceUtil.isFreshLogin()) {
                viewModel.requestAllAddress()
            }

            initTopView()


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

    override fun onProductAddToCartX(product: Product?, quantity: Int, position: Int) {
        product?.let { it1 ->

            if (quantity <= 0) {
                product.id?.let { it1 ->
                    product.variations?.get(0)?.variationId?.let { it2 ->
                        JachaiApplication.mDatabase.daoAccess()
                            .deleteCartProducts(it1, it2)
                    }
                }
                viewModel.successAddToCartData.postValue(true)
            } else {

                if (product.hub?.id?.let {
                        JachaiApplication.mDatabase.daoAccess()
                            .isInsertionApplicableByHubId(hubId = it)
                    } == true) {
//


                    if (JachaiApplication.mDatabase.daoAccess()
                            .getProductByProductIDByHub(product.id!!, product.hub.id) > 0
                    ) {

                        showShortToast("Product already added")

                    } else {

                        showShortToast("Product added")
                    }
                    viewModel.saveProductByHub(it1, quantity, product.hub, true)

                } else {
                    alertDialog(product, quantity)
                }
            }


        }


        viewModel.successAddToCartData.observe(viewLifecycleOwner, {
            binding.apply {
                if (it == true) {


                }
            }

        })
    }

/*

    fun onProductAddToCart(product: Product?) {
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

*/

    private fun showBottomSheetDialog(
        item: MutableList<Address>
    ) {

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
        if (!bottomSheetDialog.isShowing) {
            bottomSheetDialog.show()

        } else {
            bottomSheetDialog.dismiss()
            bottomSheetDialog.show()
        }


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
        SharedPreferenceUtil.setFreshLogin(false)
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
//        JachaiLog.e(TAG, data[position].fullAddress)

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
        for (mTopic in topicList) {
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

            viewModel.saveProductByHub(product, quantity, product.hub, false)
        }

        builder.setNegativeButton("Close") { dialog, which ->
            dialog.dismiss()
        }
        builder.show()
    }

    private fun showBiometricPromptForEncryption() {
        val canAuthenticate =
            BiometricManager.from(requireContext()).canAuthenticate(BIOMETRIC_STRONG)
        if (canAuthenticate == BiometricManager.BIOMETRIC_SUCCESS) {
            val secretKeyName = getString(R.string.secret_key_name)
            val cipher = cryptographyManager.getInitializedCipherForEncryption(secretKeyName)
            val biometricPrompt =
                BiometricPromptUtils.createBiometricPrompt(
                    requireActivity() as AppCompatActivity,
                    ::encryptAndStoreServerToken
                )
            val promptInfo =
                BiometricPromptUtils.createPromptInfo(requireActivity() as AppCompatActivity)
            biometricPrompt.authenticate(promptInfo, BiometricPrompt.CryptoObject(cipher))
        }
    }

    private fun encryptAndStoreServerToken(authResult: BiometricPrompt.AuthenticationResult) {
        authResult.cryptoObject?.cipher?.apply {
            chipper = this
            viewModel.requestBiometricRegister(requireActivity())
        }
    }

    override fun onServiceSelected(position: Int, item: JCService?) {
        if (item != null) {
            if (item.serviceId == "JC_FOOD") {
                val action =
                    GroceriesShopFragmentDirections.actionGroceriesShopFragmentToNavHome()
                navController.navigate(action)
            } else {
                val action =
                    GroceriesShopFragmentDirections.actionGroceriesShopFragmentToSericeNotFoundFragment()
                action.type = item.serviceId
                navController.navigate(action)
            }


        } else {
            val action =
                GroceriesShopFragmentDirections.actionGroceriesShopFragmentToSericeNotFoundFragment()
            navController.navigate(action)
        }
    }


}