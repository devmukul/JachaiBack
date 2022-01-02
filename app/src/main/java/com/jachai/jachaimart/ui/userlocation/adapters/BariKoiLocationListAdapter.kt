package com.jachai.jachaimart.ui.userlocation.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.jachai.jachaimart.R
import com.jachai.jachaimart.model.map.BariKoiPlace


class BariKoiLocationListAdapter(
    private val context: Context,
    private val cellClickListener: LocationSelectedListener
) : RecyclerView.Adapter<BariKoiLocationListAdapter.ViewHolder>() {

    private var list: List<BariKoiPlace> = emptyList()

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val primaryName: TextView = view.findViewById(R.id.primary_name)
        val secondaryName: TextView = view.findViewById(R.id.secondary_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.custom_search_result_row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = list[position]

        holder.primaryName.text = data.address.toString()
        holder.secondaryName.text = data.area.toString()



        holder.itemView.setOnClickListener {
            cellClickListener.onLocationSelectedListener(data)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setList(list: List<BariKoiPlace>) {
        this.list = list
    }

    interface LocationSelectedListener {
        fun onLocationSelectedListener(data: BariKoiPlace)
    }

}