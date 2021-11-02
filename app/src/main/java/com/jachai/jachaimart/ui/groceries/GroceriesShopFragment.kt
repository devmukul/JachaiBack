package com.jachai.jachaimart.ui.groceries

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jachai.jachaimart.JachaiFoodApplication
import com.jachai.jachaimart.R
import com.jachai.jachaimart.databinding.GroceriesShopFragmentBinding
import com.jachai.jachaimart.model.response.category.CatWithRelatedProduct
import com.jachai.jachaimart.model.response.category.Product
import com.jachai.jachaimart.model.response.home.CategoriesItem
import com.jachai.jachaimart.model.response.home.CategoryResponse
import com.jachai.jachaimart.ui.base.BaseFragment
import com.jachai.jachaimart.ui.groceries.adapters.CategoryWithProductAdapter
import com.jachai.jachaimart.ui.home.adapters.CategoryAdapter
import com.jachai.jachaimart.utils.SharedPreferenceUtil

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

    private lateinit var categoryResponse: CategoryResponse

    private lateinit var shopID: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
//        shopID = args.shopID.toString()
         if (SharedPreferenceUtil.getJCShopId() == null){
            showNoShopFoundAlert()
        }else{
             shopID = SharedPreferenceUtil.getJCShopId().toString()
             initView()
             subscribeObservers()
        }




    }

    private fun showNoShopFoundAlert() {
        val builder = AlertDialog.Builder(context)
        builder.setCancelable(false)
        builder.setTitle(getString(R.string.app_name_short) + " alert")
        builder.setMessage("Sorry !! Shop is not available at your location right now. We are coming soon. Thanks")

//        builder.setPositiveButton("Continue") { _, _ ->
//
//
//        }

        builder.setNegativeButton("Close") { dialog, which ->
            dialog.dismiss()
        }
        builder.show()

    }

    override fun initView() {
        viewModel.requestAllFavouriteProduct()
        viewModel.requestForShopCategories(shopID)




        binding.apply {

            toolbarMain.back.setOnClickListener {
                navController.popBackStack()
            }

            toolbarMain.frameLay.setOnClickListener {
                val action =
                    if (JachaiFoodApplication.mDatabase.daoAccess().getProductOrdersSize() == 0) {
                        GroceriesShopFragmentDirections.actionGroceriesShopFragmentToEmptyCartFragment()
                    } else {
                        GroceriesShopFragmentDirections.actionGroceriesShopFragmentToCartFragment()
                    }
                navController.navigate(action)
            }

            rvCategories1.apply {
                layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
                categoryAdapter =
                    CategoryAdapter(requireContext(), emptyList(), this@GroceriesShopFragment)
                adapter = categoryAdapter
            }
            rvCategories.apply {
                layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
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
            categoryResponse = it!!
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
        action.categoryList = categoryResponse
        navController.navigate(action)
    }

    override fun onCategoryViewAllSelected(catWithRelatedProduct: CatWithRelatedProduct?) {
        val action =
            GroceriesShopFragmentDirections.actionGroceriesShopFragmentToGroceryCategoryDetailsFragment()
        action.categoryId = catWithRelatedProduct?.categoryId
        action.categoryList = categoryResponse
        navController.navigate(action)


    }

    override fun onCategoryProductSelected(product: Product?) {
        val action =
            GroceriesShopFragmentDirections.actionGroceriesShopFragmentToProductDetailsFragment()
        action.productId = product?.slug
        navController.navigate(action)
    }


}