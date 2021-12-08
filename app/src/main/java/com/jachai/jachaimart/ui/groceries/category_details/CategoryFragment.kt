package com.jachai.jachaimart.ui.groceries.category_details

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.jachai.jachaimart.JachaiApplication
import com.jachai.jachaimart.R
import com.jachai.jachaimart.databinding.CategoryFragmentBinding
import com.jachai.jachaimart.model.response.home.CategoriesItem
import com.jachai.jachaimart.ui.base.BaseFragment
import com.jachai.jachaimart.utils.SharedPreferenceUtil


class CategoryFragment :
    BaseFragment<CategoryFragmentBinding>(R.layout.category_fragment) {

    companion object {
        fun newInstance() = CategoryFragment()
    }

    private val categoryData: CategoryFragmentArgs by navArgs()

    private val viewModel: CategoryViewModel by viewModels()
    private lateinit var navController: NavController
    var selectedTabPos = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        initView()
    }

    override fun initView() {
        binding.apply {
            back.setOnClickListener {
                navController.popBackStack()

            }

            loop@ for ((index, value) in categoryData.categoryList?.categories!!.withIndex()) {
                if (value.id.equals(categoryData.categoryId)) {
                    selectedTabPos = index
                    break@loop
                }
            }

            back.setOnClickListener {
                onBackPressed()
            }

            val categoryViewPagerAdapter = CategoryViewPagerAdapter(
                categoryData.categoryList?.categories!!,
                navController,
                this@CategoryFragment
            )
            viewPager.adapter = categoryViewPagerAdapter
            viewPager.offscreenPageLimit = 1
            viewPager.clipToPadding = false
            viewPager.clipChildren = false
            viewPager.isSaveEnabled = false
            viewPager.setCurrentItem(selectedTabPos, true)

            TabLayoutMediator(tabLayout, viewPager, true, true) { tab, position ->
                tab.text = categoryData.categoryList?.categories!![position].title

            }.attach()

            tabLayout.setScrollPosition(selectedTabPos, 0f, true, true)

            cartBadge.text =
                JachaiApplication.mDatabase.daoAccess().getProductOrdersSize().toString()

            frameLay.setOnClickListener {
                val action =
                    if (JachaiApplication.mDatabase.daoAccess().getProductOrdersSize() == 0) {
                        CategoryFragmentDirections.actionCategoryFragmentToEmptyCartFragment()
                    } else {
                        CategoryFragmentDirections.actionCategoryFragmentToCartFragment()
                    }
                navController.navigate(action)
            }

            search.setOnClickListener {

                if (SharedPreferenceUtil.getJCShopId().isNullOrEmpty()) {
                    navController.popBackStack()
                } else {
                    val action =
                        CategoryFragmentDirections.actionCategoryFragmentToGroceriesSearchFragment(
                            SharedPreferenceUtil.getJCShopId()!!
                        )
                    navController.navigate(action)
                }
            }


        }
    }

    override fun subscribeObservers() {
        viewModel.successAddToCartData.observe(viewLifecycleOwner, {
            binding.apply {
                if (it == true) {

                    cartBadge.text =
                        JachaiApplication.mDatabase.daoAccess().getProductOrdersSize()
                            .toString()


                }
            }

        })
    }

    class CategoryViewPagerAdapter(
        private val tabTitleList: List<CategoriesItem>, private val navController: NavController,
        fragmentManager: Fragment
    ) : FragmentStateAdapter(fragmentManager) {


        override fun getItemCount(): Int = tabTitleList.size

        override fun createFragment(position: Int): Fragment =
            GroceryCategoryDetailsFragment.newInstance(
                tabTitleList[position].title!!,
                tabTitleList[position].id!!,
                navController
            )


    }

}

