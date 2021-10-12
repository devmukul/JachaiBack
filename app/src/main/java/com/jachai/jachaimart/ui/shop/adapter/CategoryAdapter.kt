package com.jachai.jachaimart.ui.shop.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jachai.jachaimart.R
import com.jachai.jachaimart.model.shop.Product

class CategoryAdapter (
    private val context: Context,
    private val listOfCategories: List<Product>
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(
            LayoutInflater.from(context).inflate(R.layout.shop_category_row, parent, false)
        )
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(listOfCategories[position])
    }

    override fun getItemCount(): Int {
        return listOfCategories.size
    }

    class CategoryViewHolder(
        private val view: View
    ) : RecyclerView.ViewHolder(view) {

        fun bind(category: Product) {
            view.findViewById<TextView>(R.id.textView3).text = category.category
            val rv =  view.findViewById<RecyclerView>(R.id.foodList)
            rv.layoutManager = LinearLayoutManager(view.context)
            rv.adapter = FoodItemAdapter(view.context, category.products)
        }
    }
}