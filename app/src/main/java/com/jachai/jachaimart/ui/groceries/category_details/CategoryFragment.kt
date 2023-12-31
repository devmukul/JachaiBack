package com.jachai.jachaimart.ui.groceries.category_details

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.jachai.jachaimart.JachaiApplication
import com.jachai.jachaimart.R
import com.jachai.jachaimart.databinding.CategoryFragmentBinding
import com.jachai.jachaimart.model.response.home.CategoriesItem
import com.jachai.jachaimart.ui.base.BaseFragment
import com.jachai.jachaimart.utils.SharedPreferenceUtil


class CategoryFragment :
    BaseFragment<CategoryFragmentBinding>(R.layout.category_fragment),
    OnCartItemAddListener{

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

            childFragmentManager?.fragments?.forEach {
                if (it is GroceryCategoryDetailsFragment) {
                    childFragmentManager?.beginTransaction()?.remove(it)?.commit()
                }
            }

            val categoryViewPagerAdapter = SectionsPagerAdapter(
                categoryData.categoryList?.categories!!,
                navController,
                childFragmentManager
            )
            viewPager.adapter = categoryViewPagerAdapter
            viewPager.offscreenPageLimit = 1
            viewPager.setCurrentItem(selectedTabPos, true)
            tabLayout.setupWithViewPager(viewPager)

//            TabLayoutMediator(tabLayout, viewPager, true, true) { tab, position ->
//                tab.text = categoryData.categoryList?.categories!![position].title
//
//            }.attach()

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

                if (SharedPreferenceUtil.getJCHubId().isNullOrEmpty()) {
                    navController.popBackStack()
                } else {
                    val action =
                        CategoryFragmentDirections.actionCategoryFragmentToGroceriesSearchFragment(
                            SharedPreferenceUtil.getJCHubId()!!
                        )
                    navController.navigate(action)
                }
            }


        }
    }

    override fun subscribeObservers() {
//        viewModel.successAddToCartData.observe(viewLifecycleOwner, {
//            binding.apply {
//                if (it == true) {
//
//                    cartBadge.text =
//                        JachaiApplication.mDatabase.daoAccess().getProductOrdersSize()
//                            .toString()
//
//
//                }
//            }
//
//        })
    }

    class CategoryViewPagerAdapter(
        private val tabTitleList: List<CategoriesItem>,
        private val navController: NavController,
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

    class SectionsPagerAdapter(private val tabTitleList: List<CategoriesItem>,
                               private val navController: NavController
                               , fm: FragmentManager
    ) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return GroceryCategoryDetailsFragment.newInstance(
                tabTitleList[position].title!!,
                tabTitleList[position].id!!,
                navController
            )
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return tabTitleList[position].title!!
        }

        override fun getCount(): Int {
            // Show 2 total pages.
            return tabTitleList.size
        }
    }

    override fun onCartItemAdded() {
        binding.cartBadge.text = JachaiApplication.mDatabase.daoAccess().getProductOrdersSize().toString()
    }

}

