package com.jachai.jachaimart.ui.groceries.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jachai.jachaimart.databinding.CategoryProductRowBinding
import com.jachai.jachaimart.databinding.GroceriesShopCategoryProductRowBinding
import com.jachai.jachaimart.model.response.category.Product

class CategotyProductAdapter(
    private val context: Context,
    private var list: List<Product?>,
    private val interaction: Interaction?

) : RecyclerView.Adapter<CategotyProductAdapter.ViewHolder>() {


    class ViewHolder(private var binding: CategoryProductRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            context: Context,
            data: Product?,
            interaction: Interaction?
        ) {
            binding.apply {
                if (data != null) {
                    Glide.with(context)
                        .load(data.productImage)
                        .into(productImage)
                    productTitle.text = data.name
                    productPrice.text = data.variations[0].price.mrp.toString()
                    productPreviousPrice.text = data.variations[0].price.discountedPrice.toString()
                    binding.root.setOnClickListener {
                        interaction?.onProductSelected(data)
                    }

                }
            }
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            CategoryProductRowBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = list[position]
        holder.bind(context, data, interaction)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setList(it: List<Product>?) {
        if (it != null) {
            this.list = it
        }
    }

    interface Interaction {
        fun onProductSelected(product: Product?)
    }


}