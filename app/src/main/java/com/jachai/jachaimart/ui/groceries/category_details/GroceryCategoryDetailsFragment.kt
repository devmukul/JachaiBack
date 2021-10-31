package com.jachai.jachaimart.ui.groceries.category_details

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jachai.jachaimart.R

class GroceryCategoryDetailsFragment : Fragment() {

    companion object {
        fun newInstance() = GroceryCategoryDetailsFragment()
    }

    private lateinit var viewModel: GroceryCategoryDetailsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.grocery_category_details_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(GroceryCategoryDetailsViewModel::class.java)
        // TODO: Use the ViewModel
    }

}