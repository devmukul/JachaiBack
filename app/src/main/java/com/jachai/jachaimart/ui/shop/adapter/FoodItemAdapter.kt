package com.jachai.jachaimart.ui.shop.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jachai.jachaimart.R
import com.jachai.jachaimart.model.shop.ProductX

class FoodItemAdapter (
    private val context: Context,
    private var items: List<ProductX>
) :
    RecyclerView.Adapter<FoodItemAdapter.ItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            LayoutInflater.from(context).inflate(R.layout.shop_category_item_row, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ItemViewHolder(private val view: View) :
        RecyclerView.ViewHolder(view) {

        fun bind(item: ProductX) {
            view.findViewById<TextView>(R.id.head).text = item.name
            view.findViewById<TextView>(R.id.subhead).text = item.name
            view.findViewById<TextView>(R.id.cost).text = item.variations.get(0).price.mrp.toString()

            Glide.with(view.context).load(item.productImage).into(view.findViewById(R.id.foodImage))
        }
    }
}
