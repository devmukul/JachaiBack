package com.jachai.jachaimart.elearning.ui.elearning

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jachai.jachaimart.R
import com.jachai.jachaimart.elearning.model.Program

class SublectListAdapter (private val context: Context,
                          private val list: MutableList<Program>,
                          private val cellClickListener: CellClickListener

) : RecyclerView.Adapter<SublectListAdapter.ViewHolder>() {

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val titleTV: TextView = view.findViewById(R.id.title)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.list_item_program, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.count()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = list[position]
        holder.titleTV.text = data.name

        holder.itemView.setOnClickListener {
            cellClickListener.onCellClickListener(data)
        }

    }
    interface CellClickListener {
        fun onCellClickListener(data: Program)
    }

}

