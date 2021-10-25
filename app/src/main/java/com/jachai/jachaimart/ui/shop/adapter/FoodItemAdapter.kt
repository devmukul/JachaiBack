package com.jachai.jachaimart.ui.shop.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jachai.jachaimart.JachaiFoodApplication
import com.jachai.jachaimart.R
import com.jachai.jachaimart.model.shop.ProductX

class FoodItemAdapter(
    private val context: Context,
    private var items: List<ProductX>,
    private val interaction: CategoryAdapter.Interaction?
) :
    RecyclerView.Adapter<FoodItemAdapter.ItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            LayoutInflater.from(context).inflate(R.layout.shop_category_item_row, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(items[position])
        holder.itemView.setOnClickListener {
            interaction?.onProductItemSelected(position, item = items[position])
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ItemViewHolder(private val view: View) :
        RecyclerView.ViewHolder(view) {

        fun bind(item: ProductX) {
            view.findViewById<TextView>(R.id.head).text = item.name
            view.findViewById<TextView>(R.id.subhead).text = item.name
            view.findViewById<TextView>(R.id.cost).text =
                item.variations.get(0).price.mrp.toString()
            val countView = view.findViewById<TextView>(R.id.count)
            val count = JachaiFoodApplication.mDatabase.daoAccess().getOrderCount(item.id)

            if (count > 0) {
                countView.visibility = View.VISIBLE
                countView.text = count.toString()
            } else {
                countView.visibility = View.GONE
            }

            Glide.with(view.context).load(item.productImage).into(view.findViewById(R.id.foodImage))

        }
    }


}
