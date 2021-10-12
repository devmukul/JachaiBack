package com.jachai.jachaimart.ui.home.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jachai.jachaimart.R
import com.jachai.jachaimart.model.response.home.BannersItem

class BannerAdapter(
    private val context: Context,
    private var list: List<BannersItem?>
) : RecyclerView.Adapter<BannerAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = view.findViewById(R.id.image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.home_campaign_image_row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = list[position]

        if (data != null) {
            Glide.with(context)
                .load(data.bannerImage)
                .into(
                    holder.image
                )
        }
    }

    override fun getItemCount(): Int {
        return list.count()
    }

    fun setList(banners: List<BannersItem?>?) {
        if (banners != null) {
            this.list = banners
        }
    }
}