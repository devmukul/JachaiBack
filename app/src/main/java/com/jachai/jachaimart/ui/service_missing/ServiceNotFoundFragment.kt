package com.jachai.jachaimart.ui.service_missing

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.jachai.jachaimart.R
import com.jachai.jachaimart.databinding.SericeNotFoundFragmentBinding
import com.jachai.jachaimart.ui.base.BaseFragment

class ServiceNotFoundFragment :
    BaseFragment<SericeNotFoundFragmentBinding>(R.layout.serice_not_found_fragment) {

    companion object {
        fun newInstance() = ServiceNotFoundFragment()
    }

    private val viewModel: ServiceNotFoundViewModel by viewModels()
    private lateinit var navController: NavController
    private val args: ServiceNotFoundFragmentArgs by navArgs()
    private lateinit var type: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        type = args.type
        initView()
        subscribeObservers()
    }

    override fun initView() {
        binding.apply {
            back.setOnClickListener {
                goToShop()
            }

            findProduct.setOnClickListener {
                goToShop()
            }
            var image = R.drawable.food_service_not_available
            var atitle = "Opps !! Sorry !!!"
            var subTitle = "Opps !! Sorry !!!"

            when {
                type.equals("JC_FOOD") -> {
                    image = R.drawable.food_service_not_available
                    title.text = "Food"
                    atitle = "Coming to your area soon !!"
                    subTitle = "We will deliver your favorite foods directly to your doorstep."
                }
                type == "JC_LEARNING" -> {
                    title.text = "E-Learning"
                    image = R.drawable.e_learning_service_not_available
                    atitle = "Ready to learn? We are coming soon!"
                    subTitle =
                        "It is a well-funded and well-managed e-learning platform \nwith the goal of making learning enjoyable for students."

                }
                type == "JC_COURIER" -> {
                    title.text = "Courier"
                    image = R.drawable.courier_service_not_available
                    atitle = "Coming soon in your town!"
                    subTitle =
                        "Search, compare, and book your ideal courier service at the best rate. Get an easy and fast delivery service with a courier at your doorstep."
                }
                type == "JC_MEDICINE" -> {
                    title.text = "Medicine"
                    image = R.drawable.pharmacy_service_not_available
                    atitle = "Get your medicine at home!"
                    subTitle =
                        "We are expanding our network rapidly and will be coming to your area soon! "
                }
                else -> {
                    title.text = "Fashion"
                    image = R.drawable.fasion_service_not_available
                    atitle = "You shop, we ship!"
                    subTitle = "When the going gets tough, shop with us."
                }
            }

            Glide.with(requireContext())
                .load(image)
                .placeholder(R.drawable.ic_place_holder)
                .error(R.drawable.ic_place_holder)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(serviceNotFoundImage)

            emptyMessage.text = atitle
            emptySubMessage.text = subTitle
        }

    }

    override fun subscribeObservers() {

    }

    private fun goToShop() {
        val action =
            ServiceNotFoundFragmentDirections.actionSericeNotFoundFragmentToGroceriesShopFragment()
        navController.navigate(action)
    }

}