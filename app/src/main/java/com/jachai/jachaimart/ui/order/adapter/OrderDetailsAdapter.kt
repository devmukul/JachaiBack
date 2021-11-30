package com.jachai.jachaimart.ui.order.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jachai.jachaimart.databinding.CheckoutOrderRowBinding
import com.jachai.jachaimart.model.order.details.Product

class OrderDetailsAdapter(
    private val context: Context,
    private var list: List<Product?>
) : RecyclerView.Adapter<OrderDetailsAdapter.ViewHolder>() {
    class ViewHolder(
        private var binding: CheckoutOrderRowBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(context: Context, data: Product?) {
            binding.itemQty.text = "${data?.quantity}x"
            val quantity = data?.quantity?.toInt()
            val price = data?.variation?.price?.mrp?.toDouble()
            val finalPrice = quantity?.times(price!!)?.toDouble()

            binding.itemCost.text = finalPrice.toString()
            binding.itemName.text = data?.name.toString()
            if (data?.variation?.variationName.equals("Discounted")){
                binding.discount.visibility = View.VISIBLE
            }else{
                binding.discount.visibility = View.GONE
            }
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

    fun setList(it: List<Product?>?) {
        if (it != null) {
            this.list = it
        }
    }

}