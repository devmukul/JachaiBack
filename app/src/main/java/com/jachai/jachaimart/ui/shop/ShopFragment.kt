package com.jachai.jachaimart.ui.shop

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.ahmadhamwi.tabsync.TabbedListMediator
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.appbar.AppBarLayout
import com.jachai.jachai_driver.utils.Utils
import com.jachai.jachaimart.JachaiFoodApplication
import com.jachai.jachaimart.R
import com.jachai.jachaimart.databinding.ShopFragmentBinding
import com.jachai.jachaimart.model.response.home.ShopsItem
import com.jachai.jachaimart.model.shop.Product
import com.jachai.jachaimart.model.shop.ProductX
import com.jachai.jachaimart.ui.base.BaseFragment
import com.jachai.jachaimart.ui.home.HomeFragmentDirections
import com.jachai.jachaimart.ui.shop.adapter.CategoryAdapter

class ShopFragment : BaseFragment<ShopFragmentBinding>(R.layout.shop_fragment),
    CategoryAdapter.Interaction {

    companion object {
        fun newInstance() = ShopFragment()
    }

    private val viewModel: ShopViewModel by viewModels()

    private var isExpanded = true

    private val args: ShopFragmentArgs by navArgs()

    private lateinit var shopItem: ShopsItem

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        shopItem = args.shopItem
        initView()
        subscribeObservers()
        viewModel.getDriverDocStatus(shopItem.id!!)
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
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(coverImage)

            Glide.with(requireContext())
                .load(shopItem.logo)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(logo)

            resName.text = shopItem.name

            cartBottom.checkout.setOnClickListener {
                val action = ShopFragmentDirections.actionShopFragmentToCartFragment()
                findNavController().navigate(action)

            }


        }
    }

    override fun onResume() {
        super.onResume()

    }

    override fun subscribeObservers() {
        viewModel.successAddToCartData.observe(viewLifecycleOwner) {
            if (it == true) {
                binding.apply {
                    cartBottom.conLayout.visibility = View.VISIBLE
                    cartBottom.itemCount.text =
                        JachaiFoodApplication.mDatabase.daoAccess().getProductOrdersSize()
                            .toString()
                    cartBottom.totalCount.text =
                        JachaiFoodApplication.mDatabase.daoAccess().totalCost().toString()

                }
            }else{
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
        binding.recyclerView.adapter =
            CategoryAdapter(requireContext(), categories, this@ShopFragment)
    }

    private fun initMediator(categories: List<Product>) {
        TabbedListMediator(
            binding.recyclerView,
            binding.tabLayout,
            categories.indices.toList()
        ).attach()
    }

    override fun onProductItemSelected(position: Int, item: ProductX) {
        if (shopItem.id?.let {
                JachaiFoodApplication.mDatabase.daoAccess().isInsertionApplicable(shopID = it)
            } == true) {
            viewModel.saveProduct(item, 1, shopItem, true)

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
            viewModel.saveProduct(product, 1, shopItem, false)
        }

        builder.setNegativeButton("Close") { dialog, which ->
            dialog.dismiss()
        }
        builder.show()


    }
}