package com.jachai.jachaimart.ui.home.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.jachai.jachaimart.R
import com.jachai.jachaimart.model.response.home.CategoriesItem

class CategoryAdapter(
    private val context: Context,
    private var list: List<CategoriesItem?>,
    private val interaction: Interaction?

) : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = view.findViewById(R.id.image)
        val name: TextView = view.findViewById(R.id.name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(context)
                .inflate(R.layout.custom_related_category_row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = list[position]

        if (data != null) {
            Glide.with(context)
                .load(data.image)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(holder.image)

            holder.name.text = data.title
        }

        holder.itemView.setOnClickListener {
            data?.let { it1 -> interaction?.onCategoryItemSelected(position, it1) }
        }
    }

    override fun getItemCount(): Int {
        return list.count()
    }

    fun setList(categoryList: List<CategoriesItem?>?) {
        if (categoryList != null) {
            this.list = categoryList
        }
    }

    interface Interaction {
        fun onCategoryItemSelected(position: Int, item: CategoriesItem?)
    }

}