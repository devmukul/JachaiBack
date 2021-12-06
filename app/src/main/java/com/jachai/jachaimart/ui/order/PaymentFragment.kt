package com.jachai.jachaimart.ui.order

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Bitmap
import android.net.Uri
import android.net.http.SslError
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import androidx.activity.addCallback
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialDialogs
import com.jachai.jachaimart.R
import com.jachai.jachaimart.databinding.OnGoingOrderFragmentBinding
import com.jachai.jachaimart.databinding.PaymentFragmentBinding
import com.jachai.jachaimart.model.order.details.OrderDetailsResponse
import com.jachai.jachaimart.ui.base.BaseFragment
import com.jachai.jachaimart.ui.checkout.CheckoutFragmentDirections
import com.jachai.jachaimart.ui.order.adapter.OrderDetailsAdapter
import com.jachai.jachaimart.utils.constant.ApiConstants
import es.dmoral.toasty.Toasty

class PaymentFragment :
    BaseFragment<PaymentFragmentBinding>(R.layout.payment_fragment) {

    companion object {
        fun newInstance() = PaymentFragment()
    }

    private val viewModel: PaymentViewModel by viewModels()
    private lateinit var navController: NavController
    private val args: PaymentFragmentArgs by navArgs()
    private lateinit var orderId: String
    private lateinit var url: String
    private var transactionCancelDialog: Dialog? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        url = args.url
        orderId = args.orderID


        initView()
        subscribeObservers()

        val callback = requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            if (binding.webView.canGoBack()) {
                binding.webView.goBack()
            } else {
            if (transactionCancelDialog != null && transactionCancelDialog!!.isShowing()) {
                transactionCancelDialog!!.dismiss()
            }
                AlertDialog.Builder(requireContext())
                    .setTitle("Cancel Payment?")
                    .setMessage("Are you sure you want to Cancel?")
                    .setNegativeButton("No", null)
                    .setPositiveButton("Yes") { arg0, arg1 ->
                        val action = PaymentFragmentDirections.actionPaymentFragmentToOnGoingOrderFragment(orderId)
                        Toasty.error(requireContext(),"Payment Canceled!!").show()
                        navController.navigate(action)
                    }.create().show()

            }
        }
    }

    override fun onResume() {
        super.onResume()
    }

    override fun initView() {
        binding.apply {
            progressBar.max = 100
            webView.webViewClient = object : WebViewClient() {
                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    progressBar.visibility = View.VISIBLE
                    progressBar.progress = 0
                    super.onPageStarted(view, url, favicon)
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    progressBar.visibility = View.GONE
                    progressBar.progress = 100
                    super.onPageFinished(view, url)
                }

                override fun onReceivedSslError(
                    view: WebView,
                    handler: SslErrorHandler,
                    error: SslError
                ) {
                    super.onReceivedSslError(view, handler, error)
                }

                override fun shouldInterceptRequest(
                    view: WebView,
                    request: WebResourceRequest
                ): WebResourceResponse? {
                    return super.shouldInterceptRequest(view, request)
                }

                override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                    val action = PaymentFragmentDirections.actionPaymentFragmentToOnGoingOrderFragment(orderId
                    return if (url.contains("payment/cancel")) {
                        Toasty.error(requireContext(),"Payment Canceled!!").show()
                        navController.navigate(action)
                        true
                    } else if (url.contains("payment/fail")) {

                        Toasty.error(requireContext(),"Payment Failed!!").show()
                        navController.navigate(action)
                        true
                    } else if (url.contains("payment/success")) {
                        Toasty.success(requireContext(),"Payment Success!!").show()
                        navController.navigate(action)
                        true
                    } else {
                        return super.shouldOverrideUrlLoading(view, url)
                    }
                }

                override fun onLoadResource(view: WebView, url: String) {
                    super.onLoadResource(view, url)
                }
            }

            webView.webChromeClient = object : WebChromeClient() {
                override fun onProgressChanged(view: WebView?, newProgress: Int) {
                    progressBar.progress = newProgress
                    super.onProgressChanged(view, newProgress)
                }
            }

            webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
            webView.settings.javaScriptEnabled = true
            webView.loadUrl(url!!)
            webView.clearCache(true)
            webView.clearHistory()
            webView.settings.loadWithOverviewMode = true
            webView.settings.useWideViewPort = true
            webView.settings.builtInZoomControls = true
            webView.settings.displayZoomControls = false
        }

        binding.toolbarMain.back.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    override fun subscribeObservers() {
    }


}

