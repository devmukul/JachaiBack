package com.jachai.jachaimart.ui.groceries.category_details

import android.R.attr
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.ahmadhamwi.tabsync.TabbedListMediator
import com.jachai.jachaimart.R
import com.jachai.jachaimart.databinding.GroceryCategoryDetailsFragmentBinding
import com.jachai.jachaimart.model.response.category.CatWithRelatedProduct
import com.jachai.jachaimart.ui.base.BaseFragment
import com.jachai.jachaimart.ui.groceries.adapters.CategoryWithProductAdapter
import android.R.attr.defaultValue
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.android.gms.common.util.SharedPreferencesUtils
import com.jachai.jachaimart.ui.groceries.GroceriesShopFragmentDirections
import com.jachai.jachaimart.ui.groceries.adapters.CategoryDetailsProductAdapter
import com.jachai.jachaimart.utils.SharedPreferenceUtil


class GroceryCategoryDetailsFragment : BaseFragment<GroceryCategoryDetailsFragmentBinding>(R.layout.grocery_category_details_fragment),
    CategoryDetailsProductAdapter.Interaction {

    companion object {
        private const val CATEGORY_TITLE_KEY = "FRAGMENT_TITLE"
        private const val CATEGORY_ID_KEY = "CATEGORY_ID_KEY"
        private lateinit var navController1: NavController

        fun newInstance(categoryTitle: String, categoryId: String, navController: NavController): GroceryCategoryDetailsFragment {
            navController1 = navController
            val categorizedNewsFragment = GroceryCategoryDetailsFragment()
            val bundle = Bundle()
            bundle.putString(CATEGORY_TITLE_KEY, categoryTitle)
            bundle.putString(CATEGORY_ID_KEY, categoryId)
            categorizedNewsFragment.arguments = bundle
            return categorizedNewsFragment
        }
    }

    private val viewModel: GroceryCategoryDetailsViewModel by viewModels()
    private lateinit var productAdapter: CategoryDetailsProductAdapter
    private var categoryId:String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        categoryId = this.arguments?.getString(CATEGORY_ID_KEY,"")

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        subscribeObservers()

        //viewModel.checkCartStatus()

        viewModel.successResponseLiveData.observe(viewLifecycleOwner, {
            initTabLayout(it!!.productCategories)
            initRecycler(it.productCategories)
            initMediator(it.productCategories)
        })

    }

    override fun initView() {
        binding.apply {
            initRecycler(emptyList())

            viewModel.getCategoryDetailsDetails(SharedPreferenceUtil.getJCShopId()!!, categoryId!!)
        }
    }

    override fun onResume() {
        super.onResume()
    }

    override fun subscribeObservers() {
//        viewModel.successAddToCartData.observe(viewLifecycleOwner) {
//            if (it == true) {
//                binding.apply {
//                    productAdapter.notifyDataSetChanged()
//
//                    cartBottom.checkout.text = "ViEW CART"
//                    cartBottom.conLayout.visibility = View.VISIBLE
//                    cartBottom.itemCount.text =
//                        JachaiFoodApplication.mDatabase.daoAccess().getProductOrdersSize()
//                            .toString()
//                    cartBottom.totalCount.text =
//                        JachaiFoodApplication.mDatabase.daoAccess().totalCost().toString()
//
//                }
//            } else {
//                binding.cartBottom.conLayout.visibility = View.GONE
//            }
//        }
    }

    private fun initTabLayout(categories: List<CatWithRelatedProduct>) {

        binding.tabLayout.removeAllTabs()

        for (category in categories) {
            binding.apply {
                val tab = tabLayout.newTab()
                tab.setCustomView(R.layout.sub_category_row)
                val view = tab.customView
                val title = view!!.findViewById<TextView>(R.id.name)
                val image = view.findViewById<ImageView>(R.id.image)


                title.text = category.category
                tabLayout.addTab(tab)
            }

        }
    }

    private fun initRecycler(categories: List<CatWithRelatedProduct>) {
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        productAdapter = CategoryDetailsProductAdapter(requireContext(), categories, this)
        binding.recyclerView.adapter = productAdapter

    }

    private fun initMediator(categories: List<CatWithRelatedProduct>) {

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

    override fun onCategoryViewAllSelected(catWithRelatedProduct: CatWithRelatedProduct?) {

    }

    override fun onCategoryProductSelected(product: com.jachai.jachaimart.model.response.category.Product?) {

        val action =
            CategoryFragmentDirections.categoryDetailsToProductDetails()
        action.productId = product?.slug
        navController1.navigate(action)
    }
}