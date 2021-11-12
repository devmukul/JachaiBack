package com.jachai.jachaimart.ui.groceries.category_details

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.jachai.jachaimart.R
import com.jachai.jachaimart.databinding.CategoryFragmentBinding
import com.jachai.jachaimart.model.response.home.CategoriesItem
import com.jachai.jachaimart.ui.base.BaseFragment
import android.view.WindowManager
import android.view.Window


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

            loop@ for ((index, value) in  categoryData.categoryList?.categories!!.withIndex()) {
                if(value.id.equals(categoryData.categoryId)){
                    selectedTabPos = index
                    break@loop
                }
            }

            back.setOnClickListener {
                onBackPressed()
            }

            val categoryViewPagerAdapter = CategoryViewPagerAdapter( categoryData.categoryList?.categories!!, navController, this@CategoryFragment)
            viewPager.adapter = categoryViewPagerAdapter
            viewPager.offscreenPageLimit = 1
            viewPager.clipToPadding = false
            viewPager.clipChildren = false
            viewPager.setCurrentItem(selectedTabPos, true)

            TabLayoutMediator(tabLayout, viewPager, true, true ) { tab, position ->
                tab.text = categoryData.categoryList?.categories!![position].title

            }.attach()

            tabLayout.setScrollPosition(selectedTabPos,0f,true, true)



        }
    }

    override fun subscribeObservers() {
    }

    class CategoryViewPagerAdapter(private val tabTitleList: List<CategoriesItem>, private val navController: NavController,
                               fragmentManager: Fragment) : FragmentStateAdapter(fragmentManager) {
        override fun getItemCount(): Int  = tabTitleList.size

        override fun createFragment(position: Int): Fragment = GroceryCategoryDetailsFragment.newInstance(tabTitleList[position].title!!, tabTitleList[position].id!!, navController)


    }

}

