package com.jachai.jachaimart.ui.order.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jachai.jachaimart.R
import com.jachai.jachaimart.databinding.SubOrderItemBinding
import com.jachai.jachaimart.model.order.multi_order.SubOrder
import com.jachai.jachaimart.utils.constant.ApiConstants

class SubOrderDetailsAdapter(
    private val context: Context,
    private var list: List<SubOrder?>
) : RecyclerView.Adapter<SubOrderDetailsAdapter.ViewHolder>() {

    class ViewHolder(
        private var binding: SubOrderItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        private lateinit var orderDetailsAdapter: OrderDetailsAdapter
        fun bind(context: Context, order: SubOrder?, position: Int) {
            binding.apply {


                orderID.text = order?.orderId.toString()
                orderFrom.text = order?.shop?.name.toString()
                itemCost.text = order?.subTotal.toString()
                itemGrandTotal.text = order?.total.toString()
                deliveryCharge.text = order?.deliveryCharge.toString()
                totalDiscount.text = "-${order?.discount ?: 0.0}"
                vat.text = order?.vat.toString()
                paymentType.text = order?.paymentMethod

                recyclerView.apply {
                    layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                    orderDetailsAdapter =
                        OrderDetailsAdapter(context, order?.products, true)

                    adapter = orderDetailsAdapter
                }

                if (position == 0) {
                    constraintLayout8.visibility = View.VISIBLE

                } else {
                    constraintLayout8.visibility = View.GONE
                }


                when {
                    order?.status?.equals(ApiConstants.ORDER_INITIATED) == true -> {
                        state1.isIndeterminate = true
                        status.text = "Initiating order"
                        statusMessage.text = "Shop will pick your order soon."
                        timeDuration.text = "55 mins - 24 hrs"

                    }
                    order?.statusOfDeliveryMan?.equals(ApiConstants.ORDER_ACCEPTED_BY_DELIVERY_MAN) == true -> {
                        status.text = "Packaging products"
                        statusMessage.text = "Shop is arranging your product."
                        timeDuration.text = "1 hr - 3 hrs"

                    }
                    order?.statusOfDeliveryMan?.equals(ApiConstants.ORDER_PICKED) == true -> {
                        status.text = "Rider on the way"
                        statusMessage.text = "Rider just picked your order from the shop"
                        timeDuration.text = "15 - 23 mins"
                    }
                    order?.status?.equals(ApiConstants.ORDER_PROCESSING) == true -> {

                        status.text = "Rider on the way"
                        statusMessage.text = "Your order is on the way for delivery"
                        timeDuration.text = "0 - 5 mins"
                    }
                    order?.status?.equals(ApiConstants.ORDER_DELIVERED) == true -> {
                        conlayOrders.setBackgroundResource(R.drawable.box_curve_light_green)
                        status.text = "Please collect order"
                        statusMessage.text = "Thanks for shopping with JaChai Mart"
                        timeDuration.text = "Delivered"
                        textView14.text = "STATUS"
                    }

                    order?.status?.equals(ApiConstants.ORDER_REVIEWED) == true -> {
                        conlayOrders.setBackgroundResource(R.drawable.box_curve_light_green)
                        status.text = "Order already delivered"
                        statusMessage.text = "Thanks for shopping with JaChai Mart"
                        timeDuration.text = "Delivered"
                        textView14.text = "STATUS"
                    }
                    order?.status?.equals(ApiConstants.ORDER_CANCELLED) == true -> {
                        conlayOrders.setBackgroundResource(R.drawable.box_curve_light_red)
                        orderDetailsAdapter.setOrderStatus(false)
                        orderDetailsAdapter.notifyDataSetChanged()
                        status.text = "Sorry. Order is canceled for some reasons"
                        statusMessage.text = "Thanks for shopping with JaChai Mart"
                        timeDuration.text = "Canceled"
                        textView14.text = "STATUS"

                    }
                    order?.status?.equals(ApiConstants.ORDER_COMPLETED) == true -> {
                        conlayOrders.setBackgroundResource(R.drawable.box_curve_light_green)
                        status.text = "Order already delivered"
                        statusMessage.text = "Thanks for shopping with JaChai Mart"
                        timeDuration.text = "Delivered"
                        textView14.text = "STATUS"
                    }


                }


            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            SubOrderItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = list[position]
        holder.bind(context, data, position)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setList(it: List<SubOrder?>?) {
        if (it != null) {
            this.list = it
        }
    }

}