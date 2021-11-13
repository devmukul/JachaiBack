package com.jachai.jachaimart.ui.order.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jachai.jachaimart.R
import com.jachai.jachaimart.databinding.OrderRowBinding
import com.jachai.jachaimart.model.order.history.Order
import com.jachai.jachaimart.utils.constant.ApiConstants

class OrderViewRowAdapter(
    private val context: Context,
    private var list: List<Order?>,
    private var isOnGoingOrder: Boolean,
    private var interaction: Interaction
) : RecyclerView.Adapter<OrderViewRowAdapter.ViewHolder>() {
    class ViewHolder(
        private var binding: OrderRowBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(view: Context, order: Order?, isOnGoingOrder: Boolean) {

            binding.apply {
                if (order != null) {
                    orderId.text = order.orderId
                    shopName.text = order.shop?.name ?: "Shop "
                    orderTime.text = order.createdAt
                    totalCostOfOrder.text = order.total.toString()
                    if (!isOnGoingOrder) {
                        tracOrder.visibility = View.GONE
                    }
                    orderStatus.text = order.status.toString()
                    if (order.status.equals(ApiConstants.ORDER_CANCELLED)){
//                        orderStatus.setBackgroundResource(R.color.failed)

                        orderStatus.setTextColor(view.resources.getColor(R.color.failed))
                    }else if (order.status.equals(ApiConstants.ORDER_COMPLETED)
                        ||
                        order.status.equals(ApiConstants.ORDER_DELIVERED)
                        ||
                        order.status.equals(ApiConstants.ORDER_REVIEWED)){



                        orderStatus.setTextColor(view.resources.getColor(R.color.success))
                    }else{
                        orderStatus.setTextColor(view.resources.getColor(R.color.processing))

                    }

                }


            }


        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            OrderRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = list[position]
        holder.bind(context, data, isOnGoingOrder)
        holder.itemView.setOnClickListener {
            interaction.onOrderSelected(data,isOnGoingOrder)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setList(it: List<Order?>?) {
        if (it != null) {
            this.list = it
        }
    }

    interface Interaction {
        fun onOrderSelected(order: Order?, isOnGoingOrder: Boolean)
    }

}