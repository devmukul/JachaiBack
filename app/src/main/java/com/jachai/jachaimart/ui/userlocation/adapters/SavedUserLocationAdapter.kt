package com.jachai.jachaimart.ui.userlocation.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jachai.jachaimart.R
import com.jachai.jachaimart.model.response.address.Address


class SavedUserLocationAdapter(
    private val context: Context,
    private val interaction: Interaction,
    private var list: List<Address>
) : RecyclerView.Adapter<SavedUserLocationAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val mAddress: TextView = view.findViewById(R.id.mAddress)
        val title: TextView = view.findViewById(R.id.title)
        val radioButton: RadioButton = view.findViewById(R.id.radioButton)
        val imageDelete: ImageView = view.findViewById(R.id.ivDelete)

        fun bind(context: Context, data: Address) {

            title.text = data.name.toString()
            mAddress.text = data.fullAddress.toString()
            radioButton.isChecked = data.isSelected


        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.address_row_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = list[position]
        holder.bind(context, data)

        holder.itemView.setOnClickListener {
            interaction.onAddressSelectedListener(list, position)
        }
//        holder.imageDelete.setOnClickListener {
//            interaction.onAddressDeletedListener(list,position)
//        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setList(list: List<Address>) {
        this.list = list
    }

    interface Interaction {
        fun onAddressSelectedListener(data: List<Address>, position: Int)
        fun onAddressDeletedListener(data: List<Address>, position: Int)
    }

}