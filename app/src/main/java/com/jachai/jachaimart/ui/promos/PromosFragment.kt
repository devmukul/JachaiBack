package com.jachai.jachaimart.ui.promos

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jachai.jachai_driver.utils.ToastUtils
import com.jachai.jachaimart.R
import com.jachai.jachaimart.databinding.PromosFragmentBinding
import com.jachai.jachaimart.model.response.promo.Promo
import com.jachai.jachaimart.ui.base.BaseFragment
import com.jachai.jachaimart.utils.SharedPreferenceUtil

class PromosFragment : BaseFragment<PromosFragmentBinding>(R.layout.promos_fragment),
    PromoCodeAdapter.Interaction {

    companion object {
        fun newInstance() = PromosFragment()
    }
    private lateinit var navController: NavController
    private val viewModel: PromosViewModel by viewModels()
    private lateinit var promoCodeAdapter: PromoCodeAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        initView()
        subscribeObservers()


    }

    override fun initView() {

        viewModel.getAllPromos()
        showLoader()
        binding.apply {
            toolbarMain.title.text = "Promos"
            toolbarMain.back.setOnClickListener {
                navController.popBackStack()
            }
            rvPromos.apply {
                layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                promoCodeAdapter =
                    PromoCodeAdapter(requireContext(), emptyList(), this@PromosFragment)
                adapter = promoCodeAdapter
            }

            btApply.setOnClickListener {
                val myPromoCode: String = promoCode.text.toString()
                if (myPromoCode.isBlank()) {
                    ToastUtils.error("Please write a valid code.")
                } else {
                    viewModel.requestForValidatedPromo(myPromoCode)
                    showLoader()
                }
            }
        }
    }

    override fun subscribeObservers() {
        viewModel.successPromoCodesResponseLiveData.observe(viewLifecycleOwner) {
            dismissLoader()
            if (it != null && it.statusCode == 200) {
                if (!it.promos.isNullOrEmpty()) {
                    promoCodeAdapter.setList(it.promos)
                    promoCodeAdapter.notifyDataSetChanged()
                }

            }

        }

        viewModel.successPromoCodesValidResponseLiveData.observe(viewLifecycleOwner) {
            dismissLoader()
            it?.let { it1 ->

                binding.errorMessage.visibility = View.GONE
                SharedPreferenceUtil.savePromoApplied(it1)
                ToastUtils.normal("PROMO APPLIED")
                navController.popBackStack()

            }

        }
        viewModel.errorPromoCodesInvalidResponseLiveData.observe(viewLifecycleOwner) {
            dismissLoader()
            binding.apply {
                errorMessage.visibility = View.VISIBLE
                errorMessage.text = it
            }
        }

        viewModel.errorPromoCodesResponseLiveData.observe(viewLifecycleOwner) {
            dismissLoader()
            it?.let { it1 -> ToastUtils.error(it1) }
        }

    }

    override fun onPromoCodeSelected(promo: Promo?) {

        promo?.let {
            binding.promoCode.setText(promo.promoCode)
            viewModel.requestForValidatedPromo(promoCode = it.promoCode)
            showLoader()
        }

    }


}