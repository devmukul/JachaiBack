package com.jachai.jachaimart.ui.checkout.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jachai.jachaimart.databinding.CheckoutOrderRowBinding
import com.jachai.jachaimart.model.order.ProductOrder

class CheckoutAdapter(
    private val context: Context,
    private var list: List<ProductOrder?>
) : RecyclerView.Adapter<CheckoutAdapter.ViewHolder>() {
    class ViewHolder(
        private var binding: CheckoutOrderRowBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(context: Context, data: ProductOrder?) {
            binding.itemQty.text = "${data?.quantity}x"
            val quantity = data?.quantity?.toInt()
            val price = data?.price?.toDouble()
            val finalPrice = quantity?.times(price!!)

            if (data?.variant?.equals("Discounted") == true){
                binding.discount.visibility = View.VISIBLE
            }else{
                binding.discount.visibility = View.GONE
            }

            binding.itemCost.text = finalPrice.toString()
            binding.itemName.text = data?.productName.toString()
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            CheckoutOrderRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = list[position]
        holder.bind(context, data)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setList(it: List<ProductOrder>?) {
        if (it != null) {
            this.list = it
        }
    }

}