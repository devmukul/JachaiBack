package com.vikas.paging3.view.loader.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jachai.jachaimart.R
import com.jachai.jachaimart.model.response.category.Product
import com.jachai.jachaimart.ui.groceries.search.adapter.PopularTagAdapter

class LoaderDoggoImageAdapter(private val interaction: Interaction?) :
    PagingDataAdapter<Product, RecyclerView.ViewHolder>(REPO_COMPARATOR) {

    companion object {
        private val REPO_COMPARATOR = object : DiffUtil.ItemCallback<Product>() {
            override fun areItemsTheSame(oldItem: Product, newItem: Product) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Product, newItem: Product) =
                oldItem.id == newItem.id
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as? DoggoImageViewHolder)?.bind(data = getItem(position), interaction)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return DoggoImageViewHolder.getInstance(parent)
    }

    /**
     * view holder class for doggo item
     */
    class DoggoImageViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        companion object {
            //get instance of the DoggoImageViewHolder
            fun getInstance(parent: ViewGroup): DoggoImageViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val view = inflater.inflate(R.layout.category_product_row, parent, false)
                return DoggoImageViewHolder(view)
            }
        }

        var productImage: ImageView = view.findViewById(R.id.product_image)
        var productTitle: TextView = view.findViewById(R.id.product_title)
        var productPrice: TextView = view.findViewById(R.id.product_price)
        var productPreviousPrice: TextView = view.findViewById(R.id.product_previous_price)

        fun bind(data: Product?,
                 interaction: Interaction?) {

            if (data != null) {
                Glide.with(itemView.context)
                    .load(data.productImage)
                    .placeholder(R.drawable.ic_place_holder)
                    .error(R.drawable.ic_place_holder)
                    .into(productImage)
                productTitle.text = data.name
                productPrice.text = data.variations[0].price.mrp.toString()
                productPreviousPrice.text = data.variations[0].price.discountedPrice.toString()

            }

            itemView.setOnClickListener {
                interaction?.onItemSelected(data)
            }
        }

    }

    interface Interaction {
        fun onItemSelected(product: Product?)
    }

}