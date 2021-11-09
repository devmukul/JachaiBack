package com.jachai.jachaimart.ui.base

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import bd.com.evaly.ehealth.models.common.CurrentLocation
import com.google.android.material.appbar.MaterialToolbar
import com.jachai.jachaimart.BuildConfig
import com.jachai.jachaimart.R


abstract class BaseFragment<BINDING : ViewDataBinding>(
    @LayoutRes private val layoutId: Int
) : Fragment() {
    companion object {
        val TAG = BaseFragment::class.java
    }

    private var _binding: BINDING? = null
    private var baseActivity: BaseActivity<BINDING>? = null

    protected val binding get() = _binding!!

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BaseActivity<*>) {
            val activity: BaseActivity<BINDING> = context as BaseActivity<BINDING>
            baseActivity = activity
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        return _binding?.let {
            it.lifecycleOwner = viewLifecycleOwner
            it.executePendingBindings()
            printClassName()
            _binding?.root
        }
    }


    private fun printClassName() {
        if (BuildConfig.DEBUG) {
            try {
                Log.e("myClassName", javaClass.simpleName)
            } catch (exp: Exception) {
                Log.e("classNameExp", exp.toString())
            }
        }
    }

    override fun onDestroyView() {

        super.onDestroyView()
        _binding = null
    }

    protected fun <VIEW_MODEL : ViewModel> bindViewModel(viewModel: VIEW_MODEL) {
        _binding?.setVariable(1, viewModel)
    }

    fun changeStatusBarColor(colorId: Int = R.color.white, isSetTextColorWhite: Boolean = false) {
        val color = ContextCompat.getColor(requireContext(), colorId)
        baseActivity?.window?.apply {
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                decorView.systemUiVisibility =
                    if (isSetTextColorWhite) 0 else View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                statusBarColor = color
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                statusBarColor = color
        }
    }

    fun navigateTo(
        defaultDestination: NavDirections? = null,
        deepLink: String? = null,
        navOptions: NavOptions? = null
    ) = baseActivity?.navigateTo(defaultDestination, deepLink, navOptions)


    fun MaterialToolbar.setup(
        title: String = "",
        backgroundColor: Int = R.color.white,
        textColor: Int = R.color.black,
        menu: Int? = null,
        onBackNavigation: () -> Unit = { onBackPressed() }
    ) {
        setTitle(title)
        setBackgroundColor(ContextCompat.getColor(requireContext(), backgroundColor))
        setTitleTextColor(ContextCompat.getColor(requireContext(), textColor))
        setNavigationIconTint(ContextCompat.getColor(requireContext(), textColor))
        setNavigationOnClickListener { onBackNavigation() }
        menu?.let { inflateMenu(it) }
    }

    fun getBaseActivity(): BaseActivity<BINDING>? = baseActivity

    fun hideKeyboard() = baseActivity?.hideKeyboard()

    fun isNetworkConnected(): Boolean = baseActivity?.isNetworkConnected() ?: false

    fun openActivityOnTokenExpire() = baseActivity?.openActivityOnTokenExpire()

    fun onBackPressed() = baseActivity?.onBackPressed()

    fun hideBottomNavigation() = baseActivity?.hideBottomNavigation()

    fun showBottomNavigation() = baseActivity?.showBottomNavigation()

    fun showLoader() = baseActivity?.showLoader()

    fun dismissLoader() = baseActivity?.dismissLoader()
    fun phoneCall(phoneNumber: String) = baseActivity?.phoneCall(phoneNumber)

    fun fetchCurrentLocation(onLocationResultUpdate: (location: CurrentLocation?) -> Unit) =
        baseActivity?.fetchCurrentLocation(onLocationResultUpdate)

    override fun onStop() {
        changeStatusBarColor()
        super.onStop()
    }

    fun connectStomp() = baseActivity?.connectStomp()

    fun disconnectStomp() = baseActivity?.disconnectStomp()


    protected abstract fun initView()
    protected abstract fun subscribeObservers()
}
