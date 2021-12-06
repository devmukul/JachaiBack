package com.jachai.jachaimart.ui.groceries.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jachai.jachaimart.databinding.GroceriesShopCategoryRowBinding
import com.jachai.jachaimart.model.response.category.CatWithRelatedProduct
import com.jachai.jachaimart.model.response.product.Product

class CategoryWithProductPaginAdapter(
    private val context: Context,
    private val interaction: Interaction?
) : PagingDataAdapter<CatWithRelatedProduct, RecyclerView.ViewHolder>(DataDifferentiate) {


    class ViewHolder(
        private var binding: GroceriesShopCategoryRowBinding, private var interaction: Interaction?
    ) :
        RecyclerView.ViewHolder(binding.root),
        RelatedProductAdapter.Interaction {


        fun bind(context: Context, data: CatWithRelatedProduct?) {
            binding.apply {
                header.text = data?.category ?: "Category"
                header.setOnClickListener {
                    interaction?.onCategoryViewAllSelected(data)
                }
                rvItems.apply {
                    layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)

                    adapter = data?.let {
                        RelatedProductAdapter(
                            context,
                            it.products,
                            interaction = this@ViewHolder
                        )
                    }
                }


            }


        }

        override fun onProductSelected(product: Product?) {
            interaction?.onCategoryProductSelected(product)

        }

        override fun onProductAddToCart(product: Product?) {
            interaction?.onProductAddToCart(product)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            val data = getItem(position) as CatWithRelatedProduct
            holder.bind(context, data)
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            GroceriesShopCategoryRowBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), interaction
        )
    }


    object DataDifferentiate : DiffUtil.ItemCallback<CatWithRelatedProduct>() {
        override fun areItemsTheSame(oldItem: CatWithRelatedProduct, newItem: CatWithRelatedProduct): Boolean {
            return oldItem.categoryId == newItem.categoryId
        }

        override fun areContentsTheSame(oldItem: CatWithRelatedProduct, newItem: CatWithRelatedProduct): Boolean {
            return oldItem.categoryId == newItem.categoryId
        }

    }



    interface Interaction {
        fun onCategoryViewAllSelected(catWithRelatedProduct: CatWithRelatedProduct?)
        fun onCategoryProductSelected(product: Product?)
        fun onProductAddToCart(product: Product?)
    }



}