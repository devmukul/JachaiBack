package com.jachai.jachaimart.ui.groceries.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jachai.jachaimart.databinding.GroceriesShopCategoryRowBinding
import com.jachai.jachaimart.model.response.category.CatWithRelatedProduct
import com.jachai.jachaimart.model.response.category.Product
import com.jachai.jachaimart.ui.groceries.adapters.CategoryWithProductAdapter.ViewHolder

class CategoryWithProductAdapter(
    private val context: Context,
    private var list: List<CatWithRelatedProduct?>,
    private val interaction: Interaction?

) : RecyclerView.Adapter<ViewHolder>() {


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

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = list[position]
        holder.bind(context, data)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setList(it: List<CatWithRelatedProduct>?) {
        if (it != null) {
            this.list = it
        }
    }

    interface Interaction {
        fun onCategoryViewAllSelected(catWithRelatedProduct: CatWithRelatedProduct?)
        fun onCategoryProductSelected(product: Product?)
    }

}