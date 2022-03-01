package com.jachai.jachaimart.elearning.ui.initial

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.jachai.jachaimart.R
import com.jachai.jachaimart.databinding.FragmentElaerningTourBinding
import com.jachai.jachaimart.ui.base.BaseFragment

class TourFragment : BaseFragment<FragmentElaerningTourBinding>(R.layout.fragment_elaerning_tour) {

    companion object {
        fun newInstance() = TourFragment()
    }

    private lateinit var navController: NavController
    private val viewModel: TourViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        subscribeObservers()
        initView()
    }

    override fun initView() {
        binding.apply {
            val tourPageAdapter = TourPagerAdapter(parentFragmentManager)
            tourPager.adapter = tourPageAdapter
            pagerIndicator.setViewPager(tourPager)
            letsGoButton.setOnClickListener {
                navController.navigate(R.id.action_tourFragment_to_selectProgramFragment)
            }

        }
    }

    override fun subscribeObservers() {
    }

    override fun onResume() {
        super.onResume()

        fetchCurrentLocation {
            it?.let { it1 -> viewModel.getAddressFromLatLan(requireContext(), it1) }

        }


    }

    class TourPagerFragment : Fragment() {
        @Nullable
        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View {
            return try {
                if (arguments != null) {
                    val layoutResId: Int = requireArguments().getInt(RES_ID)
                    if (layoutResId != 0) inflater.inflate(
                        layoutResId,
                        container,
                        false
                    ) else View(context)
                } else View(context)
            } catch (e: Exception) {
                View(context)
            }
        }

        companion object {
            const val RES_ID = "RES_ID"
            fun newInstance(@LayoutRes layoutResId: Int): Fragment {
                val tourFragment = TourPagerFragment()
                val bundle = Bundle()
                bundle.putInt(RES_ID, layoutResId)
                tourFragment.arguments = bundle
                return tourFragment
            }
        }
    }


    private class TourPagerAdapter internal constructor(fm: FragmentManager?) :
        FragmentPagerAdapter(fm!!) {
        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> TourPagerFragment.newInstance(
                    R.layout.tour_first_page
                )
                1 -> TourPagerFragment.newInstance(
                    R.layout.tour_second_page
                )
                2 -> TourPagerFragment.newInstance(
                    R.layout.tour_third_page
                )
                else -> TourFragment()
            }
        }

        override fun getCount(): Int {
            return 3
        }
    }
}