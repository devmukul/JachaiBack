package com.jachai.jachaimart.elearning.ui.elearning

import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import com.jachai.jachai_driver.ui.register.status.SelectProgramViewModel
import com.jachai.jachaimart.R
import com.jachai.jachaimart.databinding.ProgramListFragmentBinding
import com.jachai.jachaimart.elearning.model.Program
import com.jachai.jachaimart.ui.base.BaseFragment
import com.jachai.jachaimart.utils.SharedPreferenceUtil

class HomeFragment : BaseFragment<ProgramListFragmentBinding>(R.layout.program_list_fragment),
    SublectListAdapter.CellClickListener {

    private lateinit var programList: MutableList<Program>
    companion object {
        fun newInstance() = HomeFragment()
    }
    private lateinit var navController: NavController

    private val viewModel: SelectProgramViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)


        binding.head.text = getString(R.string.wellcome)+ SharedPreferenceUtil.getName()

        viewModel.successResponseLiveData.observe(viewLifecycleOwner) {
            programList = mutableListOf()
            binding.rvRequired.adapter = SublectListAdapter(requireContext(), it!!.programs, this)
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            requireActivity().finish()
        }

        binding.include.back.setOnClickListener {
            onBackPressed()
        }
    }

    override fun initView() {
        binding.apply {

            rvRequired.layoutManager = GridLayoutManager(context, 2)
            rvRequired.setHasFixedSize(true)
        }
    }

    override fun onResume() {
        super.onResume()


        initView()
        viewModel.getProgramList()
    }

    override fun subscribeObservers() {
    }

    override fun onCellClickListener(data: Program) {
        if (data.type.equals(getString(R.string.type_class),true)){
            val bundle = bundleOf(
                "programId" to data.id
            )
            navController.navigate(R.id.action_selectProgramFragment_to_selectClassFragment, bundle)
        }
//                        if (data.name.equals(getString(R.string.profile_photo_text)))
//                            view.findNavController()
//                                .navigate(R.id.action_documentStatusFragment_to_profilePictureUploadFragment)
//                        if (data.name.equals(getString(R.string.dving_licence_text)))
//                            view.findNavController()
//                                .navigate(R.id.action_documentStatusFragment_to_drivingLicenceUploadFragment)
//                        if (data.name.equals(getString(R.string.reg_paper_text)))
//                            view.findNavController()
//                                .navigate(R.id.action_documentStatusFragment_to_registrationPapaerUploadFragment)
    }


}