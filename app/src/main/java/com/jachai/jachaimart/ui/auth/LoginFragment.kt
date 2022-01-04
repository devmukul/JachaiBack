package com.jachai.jachaimart.ui.auth

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_WEAK
import androidx.biometric.BiometricPrompt
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.google.android.gms.auth.api.credentials.Credential
import com.google.android.gms.auth.api.credentials.Credentials
import com.google.android.gms.auth.api.credentials.CredentialsOptions
import com.google.android.gms.auth.api.credentials.HintRequest
import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.jachai.jachai_driver.utils.ToastUtils
import com.jachai.jachai_driver.utils.getDeviceId
import com.jachai.jachaimart.R
import com.jachai.jachaimart.databinding.LoginFragmentBinding
import com.jachai.jachaimart.decorator.BiometricPromptUtils
import com.jachai.jachaimart.decorator.CryptographyManager
import com.jachai.jachaimart.model.response.auth.BiometricLoginRequest
import com.jachai.jachaimart.ui.base.BaseFragment
import com.jachai.jachaimart.utils.SharedPreferenceUtil
import com.jachai.jachaimart.utils.constant.SharedPreferenceConstants
import com.jachai.jachaimart.utils.constant.SharedPreferenceConstants.CIPHERTEXT_WRAPPER
import com.jachai.jachaimart.utils.constant.SharedPreferenceConstants.TAG_JACHAI
import com.jachai.jachaimart.utils.constant.SharedPreferenceConstants.TAG_JACHAI_TOKEN

class LoginFragment : BaseFragment<LoginFragmentBinding>(R.layout.login_fragment) {

    companion object {
        fun newInstance() = LoginFragment()
        var CREDENTIAL_PICKER_REQUEST = 1
    }

    private val viewModel: LoginViewModel by viewModels()
    private lateinit var biometricPrompt: BiometricPrompt
    private val cryptographyManager = CryptographyManager()
    private val ciphertextWrapper
        get() = cryptographyManager.getCiphertextWrapperFromSharedPrefs(
            requireContext(),
            TAG_JACHAI_TOKEN,
            Context.MODE_PRIVATE,
            CIPHERTEXT_WRAPPER
        )

    private val phoneNumberUtil = PhoneNumberUtil.getInstance()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        subscribeObservers()
       // phoneSelection()

        viewModel.successResponseLiveData.observe(viewLifecycleOwner, { _ ->
            //onSignUpSuccess()
            dismissLoader()
            var bundle = bundleOf(
                "mobile_number" to binding.mobileNumber.text.toString())
            view.findNavController()
                .navigate(R.id.login_to_verifyCode, bundle)
        })
        viewModel.errorResponseLiveData.observe(viewLifecycleOwner, { message ->
            dismissLoader()
            ToastUtils.error(message ?: getString(R.string.text_something_went_wrong))
        })

        viewModel.biometricSuccessResponseLiveData.observe(viewLifecycleOwner) {
            dismissLoader()
            SharedPreferenceUtil.setAuthToken(it?.token!!)
            SharedPreferenceUtil.setName(it.name)
            SharedPreferenceUtil.setMobileNo(it.mobileNumber)
            view.findNavController()
                .navigate(R.id.login_to_groceriesShop)
        }


        viewModel.failedResponseLiveData.observe( viewLifecycleOwner, {
            dismissLoader()
            ToastUtils.error(it!!.message!!)
        })

        val callback = requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            requireActivity().finish()
        }

        if (ciphertextWrapper != null) {
            showBiometricPromptForDecryption()
        }

    }
    override fun initView() {
        binding.apply {

            loginButton.setOnClickListener {
                if (isFromValidationSuccess()) {
                    showLoader()

                    var number = binding.mobileNumber.text.toString()
                    if (number.contains("+88")) {
                        number = number.replace("+88", "")
                    }
                    viewModel.registerMobileNumber(number)
                }
            }
        }
    }

    private fun isFromValidationSuccess(): Boolean {
        var errorMessage: String? = null

        errorMessage = when {
            TextUtils.isEmpty(binding.mobileNumber.text.toString()) -> getString(R.string.please_enter_your_phone_number)

            !TextUtils.isDigitsOnly(binding.mobileNumber.text.toString().replace("\\D".toRegex(), "")) -> getString(R.string.please_enter_your_phone_number_correctly)

            else -> null
        }
        if (errorMessage == null) return true

        ToastUtils.warning(errorMessage)
        return false
    }

    private fun phoneSelection() {
        val hintRequest = HintRequest.Builder()
            .setPhoneNumberIdentifierSupported(true)
            .build()

        val options = CredentialsOptions.Builder()
            .forceEnableSaveDialog()
            .build()

        val credentialsClient = Credentials.getClient(requireContext(), options)
        val intent = credentialsClient.getHintPickerIntent(hintRequest)
        try {
            startIntentSenderForResult(
                intent.intentSender,
                CREDENTIAL_PICKER_REQUEST, null, 0, 0, 0, Bundle()
            )
        } catch (e: IntentSender.SendIntentException) {
            e.printStackTrace()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CREDENTIAL_PICKER_REQUEST && resultCode == Activity.RESULT_OK) {
            val credential: Credential? = data?.getParcelableExtra(Credential.EXTRA_KEY)
            credential?.apply {

                var number = credential.id
                if (number.contains("+88")) {
                    number = number.replace("+88", "")
                }
                binding.mobileNumber.setText(number)
            }
        }
    }

    override fun subscribeObservers() {
    }

    // BIOMETRICS SECTION

    private fun showBiometricPromptForDecryption() {
        ciphertextWrapper?.let { textWrapper ->
            val canAuthenticate = BiometricManager.from(requireContext()).canAuthenticate(BIOMETRIC_STRONG)
            if (canAuthenticate == BiometricManager.BIOMETRIC_SUCCESS) {
                val secretKeyName = getString(R.string.secret_key_name)
                val cipher = cryptographyManager.getInitializedCipherForDecryption(
                    secretKeyName, textWrapper.initializationVector
                )
                biometricPrompt =
                    BiometricPromptUtils.createBiometricPrompt(
                        requireActivity() as AppCompatActivity,
                        ::decryptServerTokenFromStorage
                    )
                val promptInfo = BiometricPromptUtils.createPromptInfo(requireActivity() as AppCompatActivity)
                biometricPrompt.authenticate(promptInfo, BiometricPrompt.CryptoObject(cipher))
            }
        }
    }

    private fun decryptServerTokenFromStorage(authResult: BiometricPrompt.AuthenticationResult) {
        ciphertextWrapper?.let { textWrapper ->
            authResult.cryptoObject?.cipher?.let {
                val plaintext =
                    cryptographyManager.decryptData(textWrapper.ciphertext, it)
                showLoader()
                viewModel.performBiometricRequest(BiometricLoginRequest(requireActivity().getDeviceId(), plaintext))

            }
        }
    }


}