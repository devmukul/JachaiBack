package com.jachai.jachaimart.ui.home

import androidx.fragment.app.viewModels
import com.jachai.jachaimart.R
import com.jachai.jachaimart.databinding.FragmentHomeBinding
import com.jachai.jachaimart.ui.base.BaseFragment

class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    private val viewModel: HomeViewModel by viewModels()

    private var _binding: FragmentHomeBinding? = null
    override fun initView() {
    }

    override fun subscribeObservers() {
    }


}