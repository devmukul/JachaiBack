package com.jachai.jachaimart.ui.groceries.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jachai.jachaimart.databinding.CategoryDetailsProductRowBinding
import com.jachai.jachaimart.model.response.category.CatWithRelatedProduct
import com.jachai.jachaimart.model.response.product.Product
import com.jachai.jachaimart.ui.groceries.adapters.CategoryDetailsProductAdapter.ViewHolder

class CategoryDetailsProductAdapter(
    private val context: Context,
    private var list: List<CatWithRelatedProduct?>,
    private val interaction: Interaction?
) : RecyclerView.Adapter<ViewHolder>() {


    class ViewHolder(
        private var binding: CategoryDetailsProductRowBinding, private var interaction: Interaction?
    ) :
        RecyclerView.ViewHolder(binding.root),
        CategotyProductAdapter.Interaction {


        fun bind(context: Context, data: CatWithRelatedProduct?) {
            binding.apply {
                categoryName.text = data?.category ?: "Category"
                header.setOnClickListener {
                    interaction?.onCategoryViewAllSelected(data)
                }
                rvItems.apply {
                    layoutManager = GridLayoutManager(context, 2)

                    adapter = data?.let {
                        CategotyProductAdapter(
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


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            CategoryDetailsProductRowBinding.inflate(
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
        fun onProductAddToCart(product: Product?)

    }

}