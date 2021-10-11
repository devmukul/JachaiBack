package com.jachai.jachaimart.ui.auth

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.jachai.jachai_driver.utils.ToastUtils
import com.jachai.jachaimart.R
import com.jachai.jachaimart.databinding.NameUpdateFragmentBinding
import com.jachai.jachaimart.ui.base.BaseFragment

class NameUpdateFragment  : BaseFragment<NameUpdateFragmentBinding>(R.layout.name_update_fragment) {

    companion object {
        fun newInstance() = NameUpdateFragment()
    }

    private val viewModel: LoginViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        subscribeObservers()

        viewModel.successResponseLiveData.observe(viewLifecycleOwner, { _ ->
            dismissLoader()
            view.findNavController()
                .navigate(R.id.nameUpdate_to_nav_home)
        })
        viewModel.errorResponseLiveData.observe(viewLifecycleOwner, { message ->
            dismissLoader()
            ToastUtils.error(message ?: getString(R.string.text_something_went_wrong))
        })

    }
    override fun initView() {
        binding.apply {

            submitButton.setOnClickListener {
                if (isFromValidationSuccess()) {
                    showLoader()
                    viewModel.registerMobileNumber(binding.fullName.text.toString())
                }
            }
        }
    }

    private fun isFromValidationSuccess(): Boolean {
        var errorMessage: String? = null

        errorMessage = when {
            TextUtils.isEmpty(binding.fullName.text.toString()) -> getString(R.string.provide_name)

            else -> null
        }
        if (errorMessage == null) return true

        ToastUtils.warning(errorMessage)
        return false
    }

    override fun subscribeObservers() {
    }

}