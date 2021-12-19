package com.jachai.jachaimart.ui.groceries.adapters

import android.content.Context
import android.graphics.Paint
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import android.view.*
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.Slide
import androidx.transition.TransitionManager
import com.bumptech.glide.Glide
import com.jachai.jachai_driver.utils.ToastUtils
import com.jachai.jachaimart.R
import com.jachai.jachaimart.databinding.GroceriesShopCategoryProductRowBinding
import com.jachai.jachaimart.model.response.product.Product


class RelatedProductAdapter(
    private val context: Context,
    private var list: List<Product?>,
    private val interaction: Interaction?

) : RecyclerView.Adapter<RelatedProductAdapter.ViewHolder>() {


    class ViewHolder(private var binding: GroceriesShopCategoryProductRowBinding) :
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
                    if (product.variations?.get(0)?.stock ?: 0 > 0) {
                        conlay21.visibility = View.VISIBLE
                        conlay22.visibility = View.GONE

                    } else {
                        conlay21.visibility = View.INVISIBLE
                        conlay22.visibility = View.VISIBLE
                    }

                    val mPrice: Double = product.variations?.get(0)?.price?.mrp?.toDouble() ?: 0.0
                    val mDiscountedPrice =
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
                        productPreviousPrice.visibility = View.INVISIBLE
                    }

                    try {

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
                                        "Save ${product.variations[0]?.productDiscount?.percentage}%"
                                }
                            }

                        } else {
                            discountPrice.visibility = View.GONE
                        }

                    } catch (ex: Exception) {
                        discountPrice.visibility = View.GONE
                    }
                    //add to cart view starts
                    var quantity = icCount.text.toString().toInt()
                    var isClicked = false
                    ivAdd.setOnClickListener {

                        if (!isClicked) {
                            Handler(Looper.getMainLooper()).postDelayed({
                                conCount.slideAnimation(SlideDirection.RIGHT, SlideType.HIDE)
                                isClicked = false
                            }, 5000)
                            isClicked = true

                        }

                        if (conCount.visibility == View.GONE || conCount.visibility == View.VISIBLE) {

                            conCount.slideVisible(1000)
                            val count = icCount.text.toString().toInt()
                            val addCount = count + 1
                            val price = product.variations?.get(0)?.price?.mrp?.times(addCount)

//                productPrice?.text = price.toString()
                            val finalCount =
                                if (addCount < product.variations?.get(0)?.stock?.toInt() ?: 0) {
                                    addCount
                                } else {
                                    ToastUtils.warning("Max limit reached")

                                    product.variations?.get(0)?.stock?.toInt() ?: 0
                                }
                            quantity = finalCount
                            icCount.text = finalCount.toString()

                            interaction?.onProductAddToCartX(product, quantity)


                        }

                    }
                    icSub.setOnClickListener {
                        val count = icCount.text.toString().toInt()
                        val addCount = count - 1

                        conCount.slideVisible(1000)
                        val finalCount = if (addCount <= 0) {
                            conCount.slideAnimation(SlideDirection.RIGHT, SlideType.HIDE)
                            isClicked = false
                            0
                        } else {
                            addCount
                        }
                        val price = product.variations?.get(0)?.price?.mrp?.times(finalCount)
                        quantity = finalCount


                        icCount.text = finalCount.toString()
                        interaction?.onProductAddToCartX(product, quantity)


                    }



                    binding.root.setOnClickListener {
                        interaction?.onProductSelected(product)
                    }
//                    binding.addToCart.setOnClickListener {
//                        interaction?.onProductAddToCart(product)
//                    }

                }
            }
        }

        fun View.slideVisible(duration: Int = 5000) {
//            val transition: Transition = Slide(Gravity.LEFT)
//            transition.duration = duration.toLong()
//            TransitionManager.beginDelayedTransition(this as ViewGroup,transition)
            visibility = View.VISIBLE
            animate()
                .alpha(1f)
                .setDuration(duration.toLong())
                .setListener(null)


        }
/*        fun View.fadeVisibility(visibility: Int, duration: Long = 400) {
            val transition: Transition = Fade()
            transition.duration = duration
            transition.addTarget(this)
            TransitionManager.beginDelayedTransition(this.parent as ViewGroup, transition)
            this.visibility = visibility
        }*/


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

        fun View.slideAnimation(direction: SlideDirection, type: SlideType, duration: Long = 1200) {
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

        private fun View.slideGone(durationt: Int = 5000) {

            val transition = Slide(Gravity.LEFT)
            transition.apply {
                duration = durationt.toLong()
                addTarget(this@slideGone)
            }
            TransitionManager.beginDelayedTransition(this.parent as ViewGroup, transition)

            isVisible = false

//            animate()
//                .alpha(1f)
//                .setDuration(duration.toLong())
//                .setListener(object : AnimatorListenerAdapter() {
//                    override fun onAnimationEnd(animation: Animator?) {
//                        visibility = View.GONE
//                    }
//                })

//            visibility = View.VISIBLE
//            val animate = TranslateAnimation(0f, 0f,this.width.toFloat(),0f )
//            animate.duration = duration.toLong()
////            animate.fillAfter = true
//            this.startAnimation(animate)

        }


    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            GroceriesShopCategoryProductRowBinding.inflate(
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

        //        fun onProductAddToCart(product: Product?)
        fun onProductAddToCartX(product: Product?, quantity: Int)
    }


}