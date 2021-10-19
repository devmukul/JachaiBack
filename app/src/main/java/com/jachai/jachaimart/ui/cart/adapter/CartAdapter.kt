package com.jachai.jachaimart.ui.cart.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.jachai.jachaimart.databinding.CartItemRowBinding
import com.jachai.jachaimart.model.order.ProductOrder

class CartAdapter(
    private val context: Context,
    private var list: List<ProductOrder?>,
    private var interaction: Interaction

) : RecyclerView.Adapter<CartAdapter.ViewHolder>() {
    class ViewHolder(
        private var binding: CartItemRowBinding,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(binding.root) {


        fun bind(context: Context, productOrder: ProductOrder?) {

            Glide.with(context)
                .load(productOrder?.image)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(binding.image)
            binding.price.text = productOrder?.price.toString()
            binding.itemName.text = productOrder?.productName
            binding.include.icCount.text = productOrder?.quantity.toString()
            binding.include.ivAdd.setOnClickListener {
                interaction?.onQuantityItemAdded(productOrder)
            }
            binding.include.icSub.setOnClickListener {
                interaction?.onQuantityItemSubtraction(productOrder)
            }


        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            CartItemRowBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            interaction
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = list[position]
        holder.bind(context, data)

    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setList(list: List<ProductOrder>?) {
        if (list != null) {
            this.list = list
        }
    }

    interface Interaction {
        fun onQuantityItemAdded(item: ProductOrder?)
        fun onQuantityItemSubtraction(item: ProductOrder?)
    }
}