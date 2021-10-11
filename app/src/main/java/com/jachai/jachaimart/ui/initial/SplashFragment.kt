package com.jachai.jachaimart.ui.initial

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.jachai.jachaimart.R
import com.jachai.jachaimart.databinding.SplashFragmentBinding
import com.jachai.jachaimart.ui.base.BaseFragment
import com.jachai.jachaimart.utils.SharedPreferenceUtil

class SplashFragment : BaseFragment<SplashFragmentBinding>(R.layout.splash_fragment){

    companion object {
        fun newInstance() = SplashFragment()
    }

    private val viewModel: SplashViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()


        viewModel.liveData.observe( viewLifecycleOwner, {

            when(it) {
                "seccess" -> view.findNavController().navigate(R.id.splash_to_nav_home)
                "login" -> view.findNavController().navigate(R.id.splash_to_login)
            }
        })


    }

    override fun initView() {
        binding.apply {
        }
    }

    override fun subscribeObservers() {
    }

    override fun onResume() {
        super.onResume()
        if(SharedPreferenceUtil.isTokenAvailable()){
            viewModel.getUserInfo()
        }else{
            viewModel.initSplashScreen()
        }
    }
}