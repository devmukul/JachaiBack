package com.jachai.jachaimart.ui.shop


import android.app.AlertDialog
import android.graphics.Color
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.ahmadhamwi.tabsync.TabbedListMediator
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.jachai.jachai_driver.utils.JachaiLog
import com.jachai.jachai_driver.utils.Utils
import com.jachai.jachaimart.JachaiApplication
import com.jachai.jachaimart.R
import com.jachai.jachaimart.databinding.ShopFragmentBinding
import com.jachai.jachaimart.model.response.home.ShopsItem
import com.jachai.jachaimart.model.shop.Product
import com.jachai.jachaimart.model.shop.ProductX
import com.jachai.jachaimart.ui.base.BaseFragment
import com.jachai.jachaimart.ui.shop.adapter.CategoryAdapter
import com.jachai.jachaimart.utils.constant.CommonConstants
import kotlin.math.ceil


class ShopFragment : BaseFragment<ShopFragmentBinding>(R.layout.shop_fragment),
    CategoryAdapter.Interaction {

    companion object {
        fun newInstance() = ShopFragment()
    }

    private val viewModel: ShopViewModel by viewModels()

    private var isExpanded = true
    private val args: ShopFragmentArgs by navArgs()
    private lateinit var shopItem: ShopsItem
    private lateinit var categoryAdapter: CategoryAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        shopItem = args.shopItem
        initView()
        subscribeObservers()
        viewModel.getShopDetails(shopItem.id!!)
        viewModel.checkCartStatus()

        viewModel.successResponseLiveData.observe(viewLifecycleOwner, {
            initTabLayout(it!!.products)
            initRecycler(it.products)
            initMediator(it.products)
        })

        isExpanded = true
        val rectangle = Rect()
        val window = requireActivity().window
        window.decorView.getWindowVisibleDisplayFrame(rectangle)
        val statusBarHeight = rectangle.top
        val coverHeight =
            (binding.coverHolder.height + statusBarHeight + statusBarHeight + Utils.convertDpToPixel(
                56f,
                requireContext()
            ).toInt()) * -1

        binding.appBar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            appBarLayout.post {
                if (verticalOffset <= coverHeight) {
                    if (isExpanded) return@post
                    isExpanded = true
                    if (binding.toolbar.menu.size() > 0) binding.toolbar.menu.getItem(0).isVisible =
                        true
                    binding.infoIcon.visibility = View.GONE
                    binding.toolbar.setBackgroundColor(Color.WHITE)
                    // binding.toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
                    binding.toolbar.setTitle(shopItem.name)
                    if (getBaseActivity() != null && getBaseActivity()!!.window != null) requireActivity().window.statusBarColor =
                        Color.WHITE
                } else {
                    if (!isExpanded) return@post
                    isExpanded = false
                    if (binding.toolbar.menu.size() > 0) binding.toolbar.menu.getItem(0).isVisible =
                        false
                    binding.infoIcon.visibility = View.VISIBLE
                    binding.toolbar.setBackgroundColor(Color.TRANSPARENT)
                    //binding.toolbar.setNavigationIcon(R.drawable.ic_back_white_background)
                    binding.toolbar.title = ""
                    binding.collapsingToolbar.setScrimsShown(false, true)
                    if (getBaseActivity() != null && getBaseActivity()!!.window != null) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            getBaseActivity()!!.window.decorView.systemUiVisibility = 0
                            getBaseActivity()!!.window.statusBarColor = Color.TRANSPARENT
                        } else getBaseActivity()!!.window.statusBarColor = Color.WHITE
                    }
                }
            }
        })
//        setToolbar()
    }

    private fun setToolbar() {
        getBaseActivity()!!.setSupportActionBar(binding.toolbar)
        if (getBaseActivity()!!.supportActionBar != null) {
            getBaseActivity()!!.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            getBaseActivity()!!.supportActionBar!!.setDisplayShowHomeEnabled(true)
        }
    }


    override fun initView() {
        binding.apply {
            Glide.with(requireContext())
                .load(shopItem.banner)
                .placeholder(R.drawable.ic_place_holder)
                .error(R.drawable.ic_place_holder)
                .placeholder(R.drawable.ic_place_holder)
                .error(R.drawable.ic_place_holder)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(coverImage)

            Glide.with(requireContext())
                .load(shopItem.logo)
                .placeholder(R.drawable.ic_place_holder)
                .error(R.drawable.ic_place_holder)
                .placeholder(R.drawable.ic_place_holder)
                .error(R.drawable.ic_place_holder)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(logo)

            resName.text = shopItem.name
            subtitle.text = shopItem.description

            cartBottom.conLayout.setOnClickListener {
                val action = ShopFragmentDirections.actionShopFragmentToCartFragment()
                findNavController().navigate(action)

            }
            val charge = shopItem.deliveryCharge?.toDouble()?.let { ceil(it) }
            deliveryCharge.text = charge.toString()
            val time = shopItem.timeRemaining?.toInt() ?: 0
            val requiredTime = time + 5
            timeNeeded.text = "$time - $requiredTime mins"
            rate.text = shopItem.rating?.toDouble().toString()





            initRecycler(emptyList())


        }
    }

    override fun onResume() {
        super.onResume()

    }

    override fun subscribeObservers() {
        viewModel.successAddToCartData.observe(viewLifecycleOwner) {
            if (it == true) {
                binding.apply {
                    categoryAdapter.notifyDataSetChanged()

                    cartBottom.checkout.text = "ViEW CART"
                    cartBottom.conLayout.visibility = View.VISIBLE
                    cartBottom.itemCount.text =
                        JachaiApplication.mDatabase.daoAccess().getProductOrdersSize()
                            .toString()
                    cartBottom.totalCount.text =
                        JachaiApplication.mDatabase.daoAccess().totalCost().toString()

                }
            } else {
                binding.cartBottom.conLayout.visibility = View.GONE
            }
        }
    }

    private fun initTabLayout(categories: List<Product>) {

        binding.tabLayout.removeAllTabs()

        for (category in categories) {
            binding.apply {
                tabLayout.addTab(tabLayout.newTab().setText(category.category))
            }

        }
    }

    private fun initRecycler(categories: List<Product>) {
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        categoryAdapter = CategoryAdapter(requireContext(), categories, this@ShopFragment)
        binding.recyclerView.adapter = categoryAdapter

    }

    private fun initMediator(categories: List<Product>) {

        categories.indices.let {
            if (binding.tabLayout.tabCount > 0) {
                TabbedListMediator(
                    binding.recyclerView,
                    binding.tabLayout,
                    it.toList()
                ).attach()
            }
        }
    }

    override fun onProductItemSelected(position: Int, item: ProductX) {
        if (shopItem.id?.let {
                JachaiApplication.mDatabase.daoAccess().isInsertionApplicable(shopID = it)
            } == true) {
//            viewModel.saveProduct(item, 1, shopItem, true)
            showBottomSheetDialog(item, shopItem, true)

        } else {
            alertDialog(item)
        }


    }

    private fun alertDialog(product: ProductX) {
        val builder = AlertDialog.Builder(context)
        builder.setCancelable(false)
        builder.setTitle(getString(R.string.app_name_short) + " alert")
        builder.setMessage("Do have already selected products from a different restaurant. if you continue, your cart and selection will be removed")

        builder.setPositiveButton("Continue") { dialog, which ->
//            viewModel.saveProduct(product, 1, shopItem, false)

            showBottomSheetDialog(product, shopItem, false)
        }

        builder.setNegativeButton("Close") { dialog, which ->
            dialog.dismiss()
        }
        builder.show()
    }

    private fun showBottomSheetDialog(
        item: ProductX,
        shopItem: ShopsItem,
        isFromSameShop: Boolean
    ) {
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        bottomSheetDialog.setContentView(R.layout.bottom_add_to_cart)

        val image = bottomSheetDialog.findViewById<ImageView>(R.id.product_image)
        val productName = bottomSheetDialog.findViewById<TextView>(R.id.productName)
        val productPrice = bottomSheetDialog.findViewById<TextView>(R.id.item_cost)
        val addToCart = bottomSheetDialog.findViewById<Button>(R.id.addToCart)

        image?.let {
            Glide.with(requireContext())
                .load(item.productImage)
                .placeholder(R.drawable.ic_place_holder)
                .error(R.drawable.ic_place_holder)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(it)
        }
        productName?.text = item.name

        productPrice?.text = item.variations?.get(0)?.price?.mrp.toString()
        var quantity = 1
        val add = bottomSheetDialog.findViewById<ImageView>(R.id.ivAdd)
        val sub = bottomSheetDialog.findViewById<ImageView>(R.id.ic_sub)
        val productCount = bottomSheetDialog.findViewById<TextView>(R.id.ic_count)
        productCount?.text = 1.toString()

        bottomSheetDialog.show()

        add?.setOnClickListener {
            val count = productCount?.text.toString().toInt()
            val addCount = count + 1
            val price = item.variations?.get(0)?.price?.mrp?.times(addCount)
            quantity = addCount
            productCount?.text = addCount.toString()
            productPrice?.text = price.toString()

        }


        sub?.setOnClickListener {
            val count = productCount?.text.toString().toInt()
            val addCount = count - 1
            val finalCount = if (addCount <= 0) {
                1
            } else {
                addCount
            }
            val price = item.variations?.get(0)?.price?.mrp?.times(finalCount)
            quantity = finalCount
            productCount?.text = finalCount.toString()
            productPrice?.text = price.toString()


        }



        addToCart?.setOnClickListener {
            JachaiLog.d("SHOP", quantity.toString())
            viewModel.saveProduct(CommonConstants.JC_FOOD_TYPE,item, quantity, shopItem, isFromSameShop)
            bottomSheetDialog.dismiss()
        }


    }
}