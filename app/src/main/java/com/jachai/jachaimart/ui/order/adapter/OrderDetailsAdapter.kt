package com.jachai.jachaimart.ui.order.adapter

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jachai.jachaimart.databinding.CheckoutOrderRowBinding
import com.jachai.jachaimart.model.order.details.Product

class OrderDetailsAdapter(
    private val context: Context,
    private var list: List<Product?>? = emptyList(),
    private var orderStatus: Boolean
) : RecyclerView.Adapter<OrderDetailsAdapter.ViewHolder>() {
    class ViewHolder(
        private var binding: CheckoutOrderRowBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(context: Context, data: Product?, orderStatus: Boolean) {
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
            if (!orderStatus){
                binding.apply {
                    itemQty.paintFlags = itemQty.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    itemCost.paintFlags = itemCost.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    itemName.paintFlags = itemName.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                }
            }


        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            CheckoutOrderRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = list?.get(position)
        holder.bind(context, data, orderStatus)
    }

    override fun getItemCount(): Int {
        return list?.size ?: 0
    }

    fun setList(it: List<Product?>?) {
        if (it != null) {
            this.list = it

        }
    }
    fun  setOrderStatus(orderStatus: Boolean){
        this.orderStatus = orderStatus
    }

}