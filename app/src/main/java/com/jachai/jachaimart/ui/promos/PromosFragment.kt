package com.jachai.jachaimart.ui.promos

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jachai.jachaimart.R
import com.jachai.jachaimart.databinding.PromosFragmentBinding
import com.jachai.jachaimart.model.response.promo.Promo
import com.jachai.jachaimart.ui.base.BaseFragment

class PromosFragment : BaseFragment<PromosFragmentBinding>(R.layout.promos_fragment),
    PromoCodeAdapter.Interaction {

    companion object {
        fun newInstance() = PromosFragment()
    }

    private val viewModel: PromosViewModel by viewModels()
    private lateinit var promoCodeAdapter: PromoCodeAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        subscribeObservers()


    }

    override fun initView() {
        viewModel.getAllPromos()
        binding.apply {
            rvPromos.apply {
                layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                promoCodeAdapter =
                    PromoCodeAdapter(requireContext(), emptyList(), this@PromosFragment)
                adapter = promoCodeAdapter
            }
        }
    }

    override fun subscribeObservers() {
        viewModel.successPromoCodesResponseLiveData.observe(viewLifecycleOwner) {

            if (it != null && it.statusCode == 200) {
                if (!it.promos.isNullOrEmpty()) {
                    promoCodeAdapter.setList(it.promos)
                    promoCodeAdapter.notifyDataSetChanged()
                }

            }

        }

    }

    override fun onPromoCodeSelected(promo: Promo?) {

    }


}