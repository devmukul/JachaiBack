package com.jachai.jachaimart.ui.groceries.search.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jachai.jachaimart.databinding.ListItemTagBinding

class PopularTagAdapter(
    private val context: Context,
    private var list: List<String>,
    private val interaction: Interaction?

) : RecyclerView.Adapter<PopularTagAdapter.ViewHolder>() {


    class ViewHolder(private var binding: ListItemTagBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            context: Context,
            data: String?,
            interaction: Interaction?
        ) {
            binding.apply {
                if (data != null) {
                    name.text = data
                    binding.root.setOnClickListener {
                        interaction?.onItemSelected(data)
                    }

                }
            }
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ListItemTagBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = list[position]
        holder.bind(context, data, interaction)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setList(it: List<String>?) {
        if (it != null) {
            this.list = it
        }
    }

    interface Interaction {
        fun onItemSelected(product: String)
    }


}