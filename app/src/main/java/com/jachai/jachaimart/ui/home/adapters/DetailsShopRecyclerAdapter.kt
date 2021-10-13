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
import com.jachai.jachaimart.model.response.home.ShopsItem

class DetailsShopRecyclerAdapter(
    private val context: Context,
    private var list: List<ShopsItem?>,
    private val interaction: ShopRecyclerAdapter.Interaction?
) : RecyclerView.Adapter<DetailsShopRecyclerAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = view.findViewById(R.id.banner)
        val name: TextView = view.findViewById(R.id.resName)
        val subTitle: TextView = view.findViewById(R.id.resName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(context)
                .inflate(R.layout.custom_shop_details_row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = list[position]

        if (data != null) {
            Glide.with(context)
                .load(data.banner)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(holder.image)

            holder.name.text = data.name
        }
        holder.itemView.setOnClickListener {
            data?.let { it -> interaction?.onShopItemSelected(position, it) }

        }
    }

    override fun getItemCount(): Int {
        return list.count()
    }

    fun setList(shopList: List<ShopsItem?>?) {
        if (shopList != null) {
            this.list = shopList
        }
    }

}