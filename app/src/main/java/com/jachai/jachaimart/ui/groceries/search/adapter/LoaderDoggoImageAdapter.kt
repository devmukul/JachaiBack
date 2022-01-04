package com.jachai.jachaimart.ui.groceries.search.adapter

import android.content.Context
import android.graphics.Paint
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jachai.jachai_driver.utils.ToastUtils
import com.jachai.jachaimart.R
import com.jachai.jachaimart.model.response.product.Product
import com.jachai.jachaimart.ui.groceries.adapters.RelatedProductAdapter

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
        var discountPrice: TextView = view.findViewById(R.id.discount_price)
        var conlay22: TextView = view.findViewById(R.id.conlay22)
        var conlay21: ConstraintLayout = view.findViewById(R.id.conlay21)
        var conCount: ConstraintLayout = view.findViewById(R.id.con_count)

        //        var addToCart: CardView = view.findViewById(R.id.add_to_cart)
        var icCount: TextView = view.findViewById(R.id.ic_count)
        var ivAdd: ImageView = view.findViewById(R.id.ivAdd)
        var icSub: ImageView = view.findViewById(R.id.ic_sub)


        fun bind(
            data: Product?,
            interaction: Interaction?
        ) {

            if (data != null) {
                Glide.with(itemView.context)
                    .load(data.productImage)
                    .placeholder(R.drawable.ic_place_holder)
                    .error(R.drawable.ic_place_holder)
                    .into(productImage)

                productTitle.text = data.name

                val mPrice = data.variations?.get(0)?.price?.mrp?.toDouble() ?: 0.0
                val mDiscountedPrice = data.variations?.get(0)?.price?.discountedPrice?.toDouble()
                    ?: 0.0
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

                if (data.variations?.get(0)?.productDiscount?.flat ?: 0 > 0 || data.variations?.get(
                        0
                    )?.productDiscount?.percentage ?: 0 > 0
                ) {
                    if (data.variations?.get(0)?.productDiscount?.flat ?: 0 > 0) {
                        discountPrice.text =
                            "Save ৳${data.variations?.get(0)?.productDiscount?.flat}"
                    } else {
                        if (data.variations?.get(0)?.productDiscount?.percentage!! > 0) {
                            discountPrice.text =
                                "Save ${data.variations[0]?.productDiscount?.percentage}%"
                        }
                    }

                } else {
                    discountPrice.visibility = View.GONE
                }

            }

            if (data?.variations?.get(0)?.stock ?: 0 > 0) {
                conlay21.visibility = View.VISIBLE
                conlay22.visibility = View.GONE

            } else {
                conlay21.visibility = View.INVISIBLE
                conlay22.visibility = View.VISIBLE
            }


            //add to cart view starts
            var quantity = icCount.text.toString().toInt()
            var isClicked = false
            ivAdd.setOnClickListener {

                if (!isClicked) {
                    Handler(Looper.getMainLooper()).postDelayed({
                        conCount.slideAnimation(
                            SlideDirection.RIGHT,
                            SlideType.HIDE
                        )
                        isClicked = false
                    }, 5000)
                    isClicked = true

                }

                if (conCount.visibility == View.GONE || conCount.visibility == View.VISIBLE) {

                    conCount.slideVisible(1000)
                    val count = icCount.text.toString().toInt()
                    val addCount = count + 1
                    val price = data?.variations?.get(0)?.price?.mrp?.times(addCount)

//                productPrice?.text = price.toString()
                    val finalCount =
                        if (addCount < data?.variations?.get(0)?.stock?.toInt() ?: 0) {
                            addCount
                        } else {
                            ToastUtils.warning("Max limit reached")

                            data?.variations?.get(0)?.stock?.toInt() ?: 0
                        }
                    quantity = finalCount
                    icCount.text = finalCount.toString()

                    interaction?.onProductAddToCartX(data, quantity)


                }

            }
            icSub.setOnClickListener {
                val count = icCount.text.toString().toInt()
                val addCount = count - 1

                conCount.slideVisible(1000)
                val finalCount = if (addCount <= 0) {
                    conCount.slideAnimation(
                        SlideDirection.RIGHT,
                        SlideType.HIDE
                    )
                    isClicked = false
                    0
                } else {
                    addCount
                }
                val price = data?.variations?.get(0)?.price?.mrp?.times(finalCount)
                quantity = finalCount


                icCount.text = finalCount.toString()
                interaction?.onProductAddToCartX(data, quantity)


            }


//            addToCart.setOnClickListener {
//                interaction?.onProductAddToCart(data)
//            }
            itemView.setOnClickListener {
                interaction?.onItemSelected(data)
            }


        }

        private fun View.slideVisible(duration: Int = 5000) {
            visibility = View.VISIBLE
            animate()
                .alpha(1f)
                .setDuration(duration.toLong())
                .setListener(null)


        }

        enum class SlideDirection {
            UP,
            DOWN,
            LEFT,
            RIGHT
        }

        enum class SlideType {
            SHOW,
            HIDE
        }

        private fun View.slideAnimation(direction: SlideDirection, type: SlideType, duration: Long = 1200) {
            val fromX: Float
            val toX: Float
            val fromY: Float
            val toY: Float
            val array = IntArray(2)
            getLocationInWindow(array)
            if ((type == SlideType.HIDE && (direction == SlideDirection.RIGHT || direction == SlideDirection.DOWN)) ||
                (type == SlideType.SHOW && (direction == SlideDirection.LEFT || direction == SlideDirection.UP))
            ) {
                val displayMetrics = DisplayMetrics()
                val windowManager =
                    context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
                windowManager.defaultDisplay.getMetrics(displayMetrics)
                val deviceWidth = displayMetrics.widthPixels
                val deviceHeight = displayMetrics.heightPixels
                array[0] = deviceWidth
                array[1] = deviceHeight
            }
            when (direction) {
                SlideDirection.UP -> {
                    fromX = 0f
                    toX = 0f
                    fromY = if (type == SlideType.HIDE) 0f else (array[1] + height).toFloat()
                    toY = if (type == SlideType.HIDE) -1f * (array[1] + height) else 0f
                }
                SlideDirection.DOWN -> {
                    fromX = 0f
                    toX = 0f
                    fromY = if (type == SlideType.HIDE) 0f else -1f * (array[1] + height)
                    toY = if (type == SlideType.HIDE) 1f * (array[1] + height) else 0f
                }
                SlideDirection.LEFT -> {
                    fromX = if (type == SlideType.HIDE) 0f else 1f * (array[0] + width)
                    toX = if (type == SlideType.HIDE) -1f * (array[0] + width) else 0f
                    fromY = 0f
                    toY = 0f
                }
                SlideDirection.RIGHT -> {
                    fromX = if (type == SlideType.HIDE) 0f else -1f * (array[0] + width)
                    toX = if (type == SlideType.HIDE) 1f * (array[0] + width) else 0f
                    fromY = 0f
                    toY = 0f
                }
            }
            val animate = TranslateAnimation(
                fromX,
                toX,
                fromY,
                toY
            )
            animate.duration = duration
            animate.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationRepeat(animation: Animation?) {

                }

                override fun onAnimationEnd(animation: Animation?) {
                    if (type == SlideType.HIDE) {
                        visibility = View.GONE
                    }
                }

                override fun onAnimationStart(animation: Animation?) {
                    visibility = View.VISIBLE
                }

            })
            startAnimation(animate)
        }

    }

    interface Interaction {
        fun onItemSelected(product: Product?)

        //        fun onProductAddToCart(product: Product?)
        fun onProductAddToCartX(product: Product?, quantity: Int)
    }

}