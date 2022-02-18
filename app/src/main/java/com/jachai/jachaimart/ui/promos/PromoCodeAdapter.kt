package com.jachai.jachaimart.ui.promos

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jachai.jachaimart.databinding.PromoItemBinding
import com.jachai.jachaimart.model.response.product.Product
import com.jachai.jachaimart.model.response.promo.Promo
import java.text.SimpleDateFormat
import java.util.*

class PromoCodeAdapter(
    private val context: Context,
    private var list: List<Promo?>,
    private val interaction: Interaction?

) : RecyclerView.Adapter<PromoCodeAdapter.ViewHolder>() {


    class ViewHolder(private var binding: PromoItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            context: Context,
            promo: Promo?,
            interaction: Interaction?
        ) {
            binding.apply {
                if (promo != null) {
                    promoCode.text = promo.promoCode
                    promoValue.text =
                        if (promo.promoValue.flat != null && promo.promoValue.flat != 0) {
                            "৳${promo.promoValue.flat} OFF"
                        } else {
                            "${promo.promoValue.percentage}% OFF"
                        }
                    val minimumPurchase = promo.minimumAmountPurchase ?: 0
                    minimumOrderAmount.text = "৳$minimumPurchase minimum"
                    val valid = promo.endAt?.let { getDateFormatter(it) }
                    validUntil.text = "until $valid"
                    root.setOnClickListener {
                        interaction?.onPromoCodeSelected(promo)
                    }


                }


            }
        }
        private fun getDateFormatter(date: String): String {
            val inputFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            inputFormatter.timeZone = TimeZone.getTimeZone("UTC")
            val date = inputFormatter.parse(date)
            val outFormatter = SimpleDateFormat("dd MMM yyyy")
            return outFormatter.format(date)
        }

    }




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            PromoItemBinding.inflate(
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

    fun setList(it: List<Promo>?) {
        if (it != null) {
            this.list = it
        }
    }

    interface Interaction {
        fun onPromoCodeSelected(promo: Promo?)
    }


}