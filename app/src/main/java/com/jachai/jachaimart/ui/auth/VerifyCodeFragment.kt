package com.jachai.jachaimart.ui.auth

import android.app.AlertDialog
import android.content.IntentFilter
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.view.View
import androidx.activity.addCallback
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.jachai.jachai_driver.manager.smshelper.MySMSBroadcastReceiver
import com.jachai.jachai_driver.utils.ToastUtils
import com.jachai.jachaimart.R
import com.jachai.jachaimart.databinding.VerifyCodeFragmentBinding
import com.jachai.jachaimart.ui.base.BaseFragment
import com.jachai.jachaimart.utils.SharedPreferenceUtil
import com.jachai.jachaimart.ui.auth.LoginViewModel.Companion.OTP_EXPIRED_MESSAGE
import com.jachai.user.model.response.auth.signup.AuthRequest

class VerifyCodeFragment : BaseFragment<VerifyCodeFragmentBinding>(R.layout.verify_code_fragment),
    MySMSBroadcastReceiver.OTPReceiveListener {

    private lateinit var navController: NavController

    private val viewModel: VerifyCodeViewModel by viewModels()

    private val phoneNumberUtil = PhoneNumberUtil.getInstance()
    private var mSmsBroadcastReceiver: MySMSBroadcastReceiver? = null
    private var intentFilter: IntentFilter? = null

    private var name: String = ""
    private var password: String = ""
    private var mobile: String = ""
    private var refCode: String = ""


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)
        mobile = arguments?.getString("mobile_number")!!
        initView()
        subscribeObservers()

        observeOTPTimer()

        binding.closeButton.setOnClickListener {
            onBackPressed()
        }

        val callback = requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            view.findNavController()
                .navigate(R.id.action_verifyCodeFragment_to_loginFragment)
        }

        binding.titleTextView.text = "Enter 5 digit verification code we have sent to $mobile"

        viewModel.successResponseLiveData.observe(viewLifecycleOwner) {
            dismissLoader()
            SharedPreferenceUtil.setAuthToken(it?.token!!)
            SharedPreferenceUtil.setName(it.name)
            SharedPreferenceUtil.setMobileNo(it.mobileNumber)
            view.findNavController()
                .navigate(R.id.verifyCode_to_groceriesShop)
        }

        viewModel.createdResponseLiveData.observe(viewLifecycleOwner) {
            dismissLoader()
            SharedPreferenceUtil.setAuthToken(it?.token!!)
            SharedPreferenceUtil.setMobileNo(it.mobileNumber)
            view.findNavController()
                .navigate(R.id.verifyCode_to_nameUpdate)
        }

        viewModel.failedResponseLiveData.observe( viewLifecycleOwner, {
            dismissLoader()
            ToastUtils.error(it!!.message!!)
        })

        viewModel.otpSuccessResponseLiveData.observe(viewLifecycleOwner, { _ ->
            dismissLoader()
            binding.textView7.visibility = View.GONE
            viewModel.startTimer()
        })
        viewModel.otpErrorResponseLiveData.observe(viewLifecycleOwner, { message ->
            dismissLoader()
            ToastUtils.error(message ?: getString(R.string.text_something_went_wrong))
        })

        binding.textView7.setOnClickListener {
            showLoader()
            viewModel.registerMobileNumber(mobile)
        }


        viewModel.startTimer()

    }

    private fun observeOTPTimer() {
        viewModel.timerLiveData.observe(viewLifecycleOwner,  {
            if (it == null || it == OTP_EXPIRED_MESSAGE) {
                binding.textView7.visibility = View.VISIBLE
            } else {
                binding.textView7.visibility = View.INVISIBLE
                binding.otpTimerTextView.text = it
            }
        })
    }

    override fun initView() {
        mSmsBroadcastReceiver = MySMSBroadcastReceiver()
        mSmsBroadcastReceiver!!.initOTPListener(this)
        val intentFilter = IntentFilter()
        intentFilter.addAction(SmsRetriever.SMS_RETRIEVED_ACTION)
        requireContext().registerReceiver(mSmsBroadcastReceiver, intentFilter)
        startSMSListener()


        binding.apply {
            submitButton.setOnClickListener {
                if (isFromValidationSuccess()) {
                    showLoader()
                    val authRequest = AuthRequest()
                    authRequest.mobileNumber = phoneNumberUtil.format(
                        phoneNumberUtil.parse(mobile, "BD"),
                        PhoneNumberUtil.PhoneNumberFormat.E164
                    )
                    authRequest.otp = binding.otpView.otp
                    viewModel.performSignupRequest(authRequest)
                }
            }
        }
    }

    override fun subscribeObservers() {
    }

    fun startSMSListener() {
        val mClient = SmsRetriever.getClient(requireContext())
        val mTask: Task<Void> = mClient.startSmsRetriever()
        mTask.addOnSuccessListener(OnSuccessListener<Void?> {
        })
        mTask.addOnFailureListener(OnFailureListener {
        })
    }

    private fun isFromValidationSuccess(): Boolean {
        var errorMessage: String? = null

        errorMessage = when {
            TextUtils.isEmpty(binding.otpView.otp.toString()) -> "Invalid OTP"

            binding.otpView.otp.toString().length < 5 -> "Invalid OTP"

            binding.otpView.otp.toString().length > 5 -> "Invalid OTP"

            else -> null
        }
        if (errorMessage == null) return true

        ToastUtils.warning(errorMessage)
        return false
    }


    override fun onOTPReceived(otp: String) {
        binding.otpView.setOTP(otp)
        Handler().postDelayed(Runnable { binding.submitButton.performClick() }, 500)

    }

}