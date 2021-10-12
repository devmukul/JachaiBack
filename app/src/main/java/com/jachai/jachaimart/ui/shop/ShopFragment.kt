package com.jachai.jachaimart.ui.shop

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ahmadhamwi.tabsync.TabbedListMediator
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.successResponseLiveData.observe(viewLifecycleOwner,{
            initTabLayout(it!!.products)
            initRecycler(it.products)
            initMediator(it.products)

        })
    }

    override fun initView() {
        binding.apply {
        }
    }

    override fun onResume() {
        super.onResume()
        initView()
        viewModel.getDriverDocStatus("615fc648cd54a0170cc0c226")
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