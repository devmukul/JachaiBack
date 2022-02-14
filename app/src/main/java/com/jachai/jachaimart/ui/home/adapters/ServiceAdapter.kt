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
import com.jachai.jachaimart.model.request.JCService

class ServiceAdapter(
    private val context: Context,
    private var list: List<JCService>,
    private val interaction: Interaction?

) : RecyclerView.Adapter<ServiceAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = view.findViewById(R.id.serviceImg)
        val name: TextView = view.findViewById(R.id.name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(context)
                .inflate(R.layout.service_row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = list[position]

        if (data != null) {
            Glide.with(context)
                .load(data.serviceLogo)
                .placeholder(R.drawable.ic_place_holder)
                .error(R.drawable.ic_place_holder)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(holder.image)

            holder.name.text = data.serviceName
        }

        holder.itemView.setOnClickListener {
            data.let { it1 -> interaction?.onServiceSelected(position, it1) }
        }
    }

    override fun getItemCount(): Int {
        return list.count()
    }

    fun setList(serviceList: List<JCService>) {
        if (serviceList != null) {
            this.list = serviceList
        }
    }

    interface Interaction {
        fun onServiceSelected(position: Int, item: JCService?)
    }

}