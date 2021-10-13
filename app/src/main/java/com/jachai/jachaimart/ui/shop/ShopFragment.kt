package com.jachai.jachaimart.ui.shop

import android.graphics.Color
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ahmadhamwi.tabsync.TabbedListMediator
import com.google.android.material.appbar.AppBarLayout
import com.jachai.jachai_driver.utils.Utils
import com.jachai.jachaimart.R
import com.jachai.jachaimart.databinding.ShopFragmentBinding
import com.jachai.jachaimart.model.shop.Product
import com.jachai.jachaimart.ui.base.BaseFragment
import com.jachai.jachaimart.ui.shop.adapter.CategoryAdapter

class ShopFragment : BaseFragment<ShopFragmentBinding>(R.layout.shop_fragment){

    companion object {
        fun newInstance() = ShopFragment()
    }

    private val viewModel: ShopViewModel by viewModels()

    private var isExpanded = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        viewModel.getDriverDocStatus("615fc648cd54a0170cc0c226")

        viewModel.successResponseLiveData.observe(viewLifecycleOwner,{
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
                    binding.toolbar.setTitle("Test")
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
                        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) getBaseActivity()!!.window.statusBarColor =
                            Color.WHITE
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
        }
    }

    override fun onResume() {
        super.onResume()

    }

    override fun subscribeObservers() {
    }

    private fun initTabLayout(categories: List<Product>) {
        for (category in categories) {
            binding.apply {
                tabLayout.addTab(tabLayout.newTab().setText(category.category))
            }

        }
    }

    private fun initRecycler(categories: List<Product>) {
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = CategoryAdapter(requireContext(), categories)
    }

    private fun initMediator(categories: List<Product>) {
        TabbedListMediator(
            binding.recyclerView,
            binding.tabLayout,
            categories.indices.toList()
        ).attach()
    }
}