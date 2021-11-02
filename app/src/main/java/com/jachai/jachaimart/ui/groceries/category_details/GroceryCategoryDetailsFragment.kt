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




class GroceryCategoryDetailsFragment : BaseFragment<GroceryCategoryDetailsFragmentBinding>(R.layout.grocery_category_details_fragment),
    CategoryWithProductAdapter.Interaction {

    companion object {
        private const val CATEGORY_TITLE_KEY = "FRAGMENT_TITLE"
        private const val CATEGORY_ID_KEY = "CATEGORY_ID_KEY"

        fun newInstance(categoryTitle: String, categoryId: String): GroceryCategoryDetailsFragment {
            val categorizedNewsFragment = GroceryCategoryDetailsFragment()
            val bundle = Bundle()
            bundle.putString(CATEGORY_TITLE_KEY, categoryTitle)
            bundle.putString(CATEGORY_ID_KEY, categoryId)
            categorizedNewsFragment.arguments = bundle
            return categorizedNewsFragment
        }
    }

    private val viewModel: GroceryCategoryDetailsViewModel by viewModels()
    private lateinit var productAdapter: CategoryWithProductAdapter
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

            viewModel.getCategoryDetailsDetails("617e4b8f5097d45c3f896d0b", categoryId!!)
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
                tabLayout.addTab(tabLayout.newTab().setText(category.category))
            }

        }
    }

    private fun initRecycler(categories: List<CatWithRelatedProduct>) {
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        productAdapter = CategoryWithProductAdapter(requireContext(), categories, this)
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

    }
}