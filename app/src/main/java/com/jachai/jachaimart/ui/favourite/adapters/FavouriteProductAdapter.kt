package com.jachai.jachaimart.ui.favourite.adapters

import android.content.Context
import android.graphics.Paint
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jachai.jachaimart.JachaiApplication
import com.jachai.jachaimart.R
import com.jachai.jachaimart.databinding.FavouriteProductRowBinding
import com.jachai.jachaimart.model.response.product.Product

class FavouriteProductAdapter(
    private val context: Context,
    private var list: List<Product?>,
    private val interaction: Interaction?

) : RecyclerView.Adapter<FavouriteProductAdapter.ViewHolder>() {


    class ViewHolder(private var binding: FavouriteProductRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            context: Context,
            product: Product?,
            interaction: Interaction?
        ) {
            binding.apply {
                if (product != null) {
                    Glide.with(context)
                        .load(product.productImage)
                        .placeholder(R.drawable.ic_place_holder)
                        .error(R.drawable.ic_place_holder)
                        .into(productImage)

                    productTitle.text = product.name
                    if (product?.variations?.get(0)?.stock ?: 0 > 0) {
                        conlay21.visibility = View.VISIBLE
                        conlay22.visibility = View.GONE

                    } else {
                        conlay21.visibility = View.INVISIBLE
                        conlay22.visibility = View.VISIBLE
                    }

                    val mPrice: Double = product.variations?.get(0)?.price?.mrp?.toDouble() ?: 0.0
                    val mDiscountedPrice: Double =
                        product.variations?.get(0)?.price?.discountedPrice?.toDouble() ?: 0.0


//                    productPrice.text = product.variations[0].price.mrp.toString()
//
//                    productPreviousPrice.text = product.variations[0].price.discountedPrice.toString()

                    if (mDiscountedPrice != 0.0 && mDiscountedPrice < mPrice) {
                        productPrice.text = "${mDiscountedPrice.toFloat()}"
                        productPreviousPrice.text = "৳${mPrice.toFloat()}"
                        productPreviousPrice.paintFlags =
                            productPreviousPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    } else {
                        productPrice.text = mPrice.toFloat().toString()
                        productPreviousPrice.text = "${mDiscountedPrice.toFloat()}"
                        productPreviousPrice.visibility = View.GONE
                    }

                    if (product.variations?.get(0)?.productDiscount?.flat ?: 0 > 0 || product.variations?.get(
                            0
                        )?.productDiscount?.percentage ?: 0 > 0
                    ) {
                        if (product.variations?.get(0)?.productDiscount?.flat ?: 0 > 0) {
                            discountPrice.text =
                                "Save ৳${product.variations?.get(0)?.productDiscount?.flat}"
                        } else {
                            if (product.variations?.get(0)?.productDiscount?.percentage!! > 0) {
                                discountPrice.text =
                                    "Save ${product.variations?.get(0)?.productDiscount?.percentage}%"
                            }
                        }

                    } else {
                        discountPrice.visibility = View.GONE
                    }
                    var quantity = icCount.text.toString().toInt()
                    ivAdd.setOnClickListener {
                        Handler(Looper.getMainLooper()).postDelayed({
                            conCount.visibility = View.GONE
                        }, 5000)

                        if (conCount.visibility == View.GONE || conCount.visibility == View.VISIBLE) {

                            conCount.visibility = View.VISIBLE
                            val count = icCount.text.toString().toInt()
                            val addCount = count + 1
                            val price = product?.variations?.get(0)?.price?.mrp?.times(addCount)

//                productPrice?.text = price.toString()
                            val finalCount =
                                if (addCount < product?.variations?.get(0)?.stock?.toInt() ?: 0) {
                                    addCount
                                } else {
//                                showShortToast("Max limit reached")
                                    product?.variations?.get(0)?.stock?.toInt() ?: 0
                                }
                            quantity = finalCount
                            icCount.text = finalCount.toString()

                            interaction?.onProductAddToCartX(product, quantity)


                        }

                    }
                    icSub.setOnClickListener {
                        val count = icCount.text.toString().toInt()
                        val addCount = count - 1
                        if (addCount <= 0) {
                            product.id?.let { it1 ->
                                product.variations?.get(0)?.variationId?.let { it2 ->
                                    JachaiApplication.mDatabase.daoAccess()
                                        .deleteCartProducts(it1, it2)
                                }
                                conCount.visibility = View.GONE
                            }


                        } else {
                            conCount.visibility = View.VISIBLE
                            val finalCount = if (addCount <= 0) {
                                1
                            } else {
                                addCount
                            }
                            val price = product?.variations?.get(0)?.price?.mrp?.times(finalCount)
                            quantity = finalCount


                            icCount.text = finalCount.toString()
                            interaction?.onProductAddToCartX(product, quantity)

                        }

                    }

                    binding.root.setOnClickListener {
                        interaction?.onProductSelected(product)
                    }


                }
            }
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            FavouriteProductRowBinding.inflate(
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

    fun setList(it: List<Product>?) {
        if (it != null) {
            this.list = it
        }
    }

    interface Interaction {
        fun onProductSelected(product: Product?)
        fun onProductAddToCartX(product: Product?, quantity: Int)
    }


}