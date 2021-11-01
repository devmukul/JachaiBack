package com.jachai.jachaimart.ui.groceries.category_details

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.jachai.jachaimart.R
import com.jachai.jachaimart.databinding.CategoryFragmentBinding
import com.jachai.jachaimart.model.response.home.CategoriesItem
import com.jachai.jachaimart.ui.base.BaseFragment

class CategoryFragment :
    BaseFragment<CategoryFragmentBinding>(R.layout.category_fragment) {

    companion object {
        fun newInstance() = CategoryFragment()
    }

    private val categoryData: CategoryFragmentArgs by navArgs()

    private val viewModel: CategoryViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    override fun initView() {
        binding.apply {

            val categoryViewPagerAdapter = CategoryViewPagerAdapter( categoryData.categoryList?.categories!!, this@CategoryFragment)
            viewPager.adapter = categoryViewPagerAdapter
            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.text = categoryData.categoryList?.categories!![position].title
            }.attach()
        }
    }

    override fun subscribeObservers() {
    }

    class CategoryViewPagerAdapter(private val tabTitleList: List<CategoriesItem>,
                               fragmentManager: Fragment) : FragmentStateAdapter(fragmentManager) {
        override fun getItemCount(): Int  = tabTitleList.size

        override fun createFragment(position: Int): Fragment = GroceryCategoryDetailsFragment.newInstance(tabTitleList[position].title!!, tabTitleList[position].id!!)

    }

}
