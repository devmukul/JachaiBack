package com.jachai.jachaimart.elearning.ui.onboard

import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import com.jachai.jachai_driver.ui.register.status.SelectProgramViewModel
import com.jachai.jachaimart.R
import com.jachai.jachaimart.databinding.ProgramListFragmentBinding
import com.jachai.jachaimart.elearning.model.Curriculum
import com.jachai.jachaimart.elearning.model.Discipline
import com.jachai.jachaimart.elearning.model.Program
import com.jachai.jachaimart.ui.base.BaseFragment
import com.jachai.jachaimart.utils.SharedPreferenceUtil

class SelectDiciplineFragment : BaseFragment<ProgramListFragmentBinding>(R.layout.program_list_fragment),
    DiciplinListAdapter.CellClickListener {

    private lateinit var programList: MutableList<Program>
    companion object {
        fun newInstance() = SelectDiciplineFragment()
    }
    private lateinit var navController: NavController
    private var programId: String? = null
    private var curriculumId: String? = null

    private val viewModel: SelectProgramViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        programId = arguments?.getString("programId")!!
        curriculumId = arguments?.getString("curriculumId")!!


        viewModel.getDiciplineList(programId!!, curriculumId!!)

        binding.head.text = getString(R.string.wellcome)+ SharedPreferenceUtil.getName()

        viewModel.diciplineListSuccessResponseLiveData.observe(viewLifecycleOwner) {
            programList = mutableListOf()
            binding.rvRequired.adapter = DiciplinListAdapter(requireContext(), it!!.disciplines,
                object : DiciplinListAdapter.CellClickListener {
                    override fun onCellClickListener(data: Discipline) {
//                        if (data.type.equals(getString(R.string.type_class),true))
//                            navController.navigate(R.id.action_selectProgramFragment_to_selectClassFragment)
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
                })

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
    }

    override fun subscribeObservers() {
    }

    override fun onCellClickListener(data: Discipline) {
    }



}