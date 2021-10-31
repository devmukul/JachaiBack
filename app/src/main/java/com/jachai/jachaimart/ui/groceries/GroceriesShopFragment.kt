package com.jachai.jachaimart.ui.groceries

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jachai.jachaimart.R
import com.jachai.jachaimart.databinding.GroceriesShopFragmentBinding
import com.jachai.jachaimart.model.response.category.CatWithRelatedProduct
import com.jachai.jachaimart.model.response.category.Product
import com.jachai.jachaimart.model.response.home.CategoriesItem
import com.jachai.jachaimart.ui.base.BaseFragment
import com.jachai.jachaimart.ui.groceries.adapters.CategoryWithProductAdapter
import com.jachai.jachaimart.ui.home.adapters.CategoryAdapter
import com.jachai.jachaimart.ui.shop.shop_list.ShopListFragmentDirections

class GroceriesShopFragment :
    BaseFragment<GroceriesShopFragmentBinding>(R.layout.groceries_shop_fragment),
    CategoryAdapter.Interaction, CategoryWithProductAdapter.Interaction {

    companion object {
        fun newInstance() = GroceriesShopFragment()
    }

    private val viewModel: GroceriesShopViewModel by viewModels()

    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var categoryWithProductAdapter: CategoryWithProductAdapter
    private lateinit var navController: NavController
    private val args: GroceriesShopFragmentArgs by navArgs()

    private lateinit var shopID: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
//        shopID = args.shopID.toString()
        shopID = "617e4b8f5097d45c3f896d0b"

        initView()
        subscribeObservers()


    }

    override fun initView() {
        viewModel.requestAllFavouriteProduct()
        viewModel.requestForShopCategories(shopID)




        binding.apply {

            toolbarMain.back.setOnClickListener {
                navController.popBackStack()
            }
            rvCategories1.apply {
                layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
                categoryAdapter =
                    CategoryAdapter(requireContext(), emptyList(), this@GroceriesShopFragment)
                adapter = categoryAdapter
            }
            rvCategories.apply {
                layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
                categoryWithProductAdapter =
                    CategoryWithProductAdapter(
                        requireContext(),
                        emptyList(),
                        this@GroceriesShopFragment
                    )
                adapter = categoryWithProductAdapter
            }


        }

    }

    override fun subscribeObservers() {
        viewModel.successCategoryResponseLiveData.observe(viewLifecycleOwner) {
            categoryAdapter.setList(it?.categories)
            categoryAdapter.notifyDataSetChanged()
            viewModel.requestForShopCategoryWithRelatedProduct(it?.categories, shopID)
        }

        viewModel.successCategoryWithProductResponseLiveData.observe(viewLifecycleOwner) {
            categoryWithProductAdapter.setList(it?.catWithRelatedProducts)
            categoryWithProductAdapter.notifyDataSetChanged()
        }

    }

    override fun onCategoryItemSelected(position: Int, item: CategoriesItem?) {
        val action =
            GroceriesShopFragmentDirections.actionGroceriesShopFragmentToGroceryCategoryDetailsFragment()
        action.categoryId = item?.id
        navController.navigate(action)
    }

    override fun onCategoryViewAllSelected(catWithRelatedProduct: CatWithRelatedProduct?) {
        val action =
            GroceriesShopFragmentDirections.actionGroceriesShopFragmentToGroceryCategoryDetailsFragment()
        action.categoryId = catWithRelatedProduct?.category
        navController.navigate(action)


    }

    override fun onCategoryProductSelected(product: Product?) {
        val action =
            GroceriesShopFragmentDirections.actionGroceriesShopFragmentToProductDetailsFragment()
        action.productId = product?.id
        navController.navigate(action)
    }


}