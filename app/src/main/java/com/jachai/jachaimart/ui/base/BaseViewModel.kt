package com.jachai.jachaimart.ui.base


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.jachai.jachai_driver.utils.JachaiLog
import com.jachai.jachai_driver.utils.isConnectionAvailable
import com.jachai.jachai_driver.utils.showShortToast
import com.jachai.jachaimart.JachaiApplication
import com.jachai.jachaimart.R
import com.jachai.jachaimart.model.order.PaymentRequestResponse
import com.jachai.jachaimart.model.order.ProductOrder
import com.jachai.jachaimart.model.order.details.OrderDetailsResponse
import com.jachai.jachaimart.model.order.history.Order
import com.jachai.jachaimart.model.order.history.OrderHistoryResponse
import com.jachai.jachaimart.model.request.PaymentRequest
import com.jachai.jachaimart.model.response.GenericResponse
import com.jachai.jachaimart.model.response.address.Address
import com.jachai.jachaimart.model.response.address.AddressResponse
import com.jachai.jachaimart.model.response.address.Location
import com.jachai.jachaimart.model.response.grocery.NearestJCShopResponse
import com.jachai.jachaimart.model.response.pay.PaymentListResponse
import com.jachai.jachaimart.model.response.product.Product
import com.jachai.jachaimart.model.response.product.Shop
import com.jachai.jachaimart.model.response.product.VariationsItem
import com.jachai.jachaimart.ui.groceries.GroceriesShopViewModel
import com.jachai.jachaimart.utils.HttpStatusCode
import com.jachai.jachaimart.utils.RetrofitConfig
import com.jachai.jachaimart.utils.SharedPreferenceUtil
import com.jachai.jachaimart.utils.constant.ApiConstants
import com.jachai.jachaimart.utils.constant.CommonConstants
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


abstract class BaseViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        val TAG = BaseViewModel::class.java
    }

    private val driverService = RetrofitConfig.driverService
    private val groceryService = RetrofitConfig.groceryService
    private val orderService = RetrofitConfig.orderService
    private val paymentService = RetrofitConfig.paymentService
    private var paymentMethodCall: Call<PaymentListResponse>? = null
    var successAddToCartData = MutableLiveData<Boolean?>()
    var errorResponseLiveData =  MutableLiveData<String>()
    var failedResponseLiveData = MutableLiveData<GenericResponse?>()
    var successPaymentMethodListLiveData = MutableLiveData<PaymentListResponse>()

    private var nearestJCShopCall: Call<NearestJCShopResponse>? = null
    private var addressCall: Call<AddressResponse>? = null
    private var deleteAddressCall: Call<AddressResponse>? = null
    private var orderCall: Call<OrderDetailsResponse>? = null
    private var orderListCall: Call<OrderHistoryResponse>? = null


    var successOrderDetailsLiveData = MutableLiveData<OrderDetailsResponse>()
    var errorDetailsLiveData = MutableLiveData<String>()


    var successAddressResponseLiveData = MutableLiveData<AddressResponse?>()
    var deleteAddressResponseLiveData = MutableLiveData<AddressResponse?>()
    var successNearestJCShopUpdate = MutableLiveData<Boolean?>()

    var successNearestJCShop = MutableLiveData<NearestJCShopResponse?>()
    var errorNearestJCShop = MutableLiveData<String?>()
    var errorAddressResponseLiveData = MutableLiveData<String?>()

    var successOrderHistoryDetailsLiveData = MutableLiveData<OrderHistoryResponse>()
    var successOnGoingOrderListLiveData = MutableLiveData<List<Order>>()
    var successPreviousOrderListLiveData = MutableLiveData<List<Order>>()
    var errorOrderDetailsLiveData = MutableLiveData<String>()

    var errorPaymentResponseLiveData =  MutableLiveData<String>()


    fun requestAllAddress() {
        try {
            if (addressCall != null) {
                return
            } else if (!getApplication<JachaiApplication>().isConnectionAvailable()) {
                getApplication<JachaiApplication>().showShortToast(R.string.networkError)
                return
            }

            addressCall = driverService.getAllUserAddress()

            addressCall?.enqueue(object : Callback<AddressResponse> {
                override fun onResponse(
                    call: Call<AddressResponse>,
                    response: Response<AddressResponse>
                ) {
                    addressCall = null
                    if (response.isSuccessful) {
                        successAddressResponseLiveData.value = response.body()
                    }
                    JachaiLog.d(GroceriesShopViewModel.TAG, response.body().toString())

                }

                override fun onFailure(call: Call<AddressResponse>, t: Throwable) {
                    addressCall = null
                    errorAddressResponseLiveData.value = "failed"
                    JachaiLog.d(GroceriesShopViewModel.TAG, t.localizedMessage)

                }
            })


        } catch (ex: Exception) {
            JachaiLog.d(GroceriesShopViewModel.TAG, ex.message!!)
        }
    }

    fun getNearestJCShop(location: Location, isFromCheckout: Boolean, address: Address?) {
        try {

            if (nearestJCShopCall != null) {
                return
            } else if (!getApplication<JachaiApplication>().isConnectionAvailable()) {
                // alertMessageListener?.showAlertMessage(R.string.no_internet_connection_message, AlertType.Snackbar)

                return
            }

            nearestJCShopCall = location.let {
                it.latitude?.let { it1 ->
                    it.longitude?.let { it2 ->
                        groceryService.getNearestJCShopAroundMe(
                            CommonConstants.JC_MART_TYPE,
                            it1,
                            it2,
                            0,
                            1

                        )
                    }
                }
            }
//            nearestJCShopCall = location?.let {
//                groceryService.getNearestJCShopAroundMe(
//                    CommonConstants.JC_MART_TYPE,
//                    23.7639972,
//                    90.4328452,
//                    0,
//                    1
//
//                )
//            }

            nearestJCShopCall?.enqueue(object : Callback<NearestJCShopResponse> {
                override fun onResponse(
                    call: Call<NearestJCShopResponse>?,
                    response: Response<NearestJCShopResponse>?
                ) {
                    nearestJCShopCall = null
                    when (response?.code()) {
                        HttpStatusCode.HTTP_OK -> {
                            val mResponse = response.body()
                            if (mResponse != null) {
                                if (isFromCheckout){
                                    mResponse.userDeliveryAddress = address
                                    successNearestJCShop.value = mResponse

                                }else {
                                    if (mResponse.shops.isNotEmpty()) {
                                        SharedPreferenceUtil.setJCShopId(mResponse.shops[0].id)
                                        SharedPreferenceUtil.saveNearestShop(mResponse.shops[0])
                                        successNearestJCShopUpdate.value = true
                                    } else {
                                        SharedPreferenceUtil.setJCShopId(null)
                                        successNearestJCShopUpdate.value = false
                                    }
                                }
                            }else{
                                errorNearestJCShop.value= "failed"
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<NearestJCShopResponse>?, t: Throwable?) {
                    nearestJCShopCall = null
                    errorNearestJCShop.value= "failed"
                    if (t != null) {
                        t.message?.let { JachaiLog.e(BaseFragment.TAG, it) }
                    }
                }
            })


        } catch (ex: Exception) {
            JachaiLog.e(BaseFragment.TAG, ex.localizedMessage)
        }


    }


    fun getOrderDetails(orderId: String) {
        try {


            if (orderCall != null) {
                return
            } else if (!getApplication<JachaiApplication>().isConnectionAvailable()) {
                getApplication<JachaiApplication>().showShortToast(R.string.networkError)
                return
            }

            orderCall = orderService.orderDetailsRequest(orderId)

            orderCall?.enqueue(object : Callback<OrderDetailsResponse> {
                override fun onResponse(
                    call: Call<OrderDetailsResponse>,
                    response: Response<OrderDetailsResponse>
                ) {
                    JachaiLog.d(TAG, response.body().toString())
                    orderCall = null
                    successOrderDetailsLiveData.postValue(response.body())


                }

                override fun onFailure(call: Call<OrderDetailsResponse>, t: Throwable) {
                    orderCall = null
                    JachaiLog.d(TAG, t.localizedMessage)
                    errorDetailsLiveData.postValue("Failed")

                }
            })

        } catch (ex: Exception) {
            JachaiLog.d(TAG, ex.message!!)
        }
    }

    fun getDiscountPrice() = JachaiApplication.mDatabase.daoAccess().geDiscountPrice()


    fun deleteAddress(addressID: String) {
        try {
            if (deleteAddressCall != null) {
                return
            } else if (!getApplication<JachaiApplication>().isConnectionAvailable()) {
                getApplication<JachaiApplication>().showShortToast(R.string.networkError)
                return
            }

            deleteAddressCall = driverService.deleteUserAddress(addressID)

            deleteAddressCall?.enqueue(object : Callback<AddressResponse> {
                override fun onResponse(
                    call: Call<AddressResponse>,
                    response: Response<AddressResponse>
                ) {
                    deleteAddressCall = null
                    if (response.isSuccessful) {
                        deleteAddressResponseLiveData.value = response.body()
                    }else{
                        errorAddressResponseLiveData.value = "deleted failed"
                    }
                    JachaiLog.d(GroceriesShopViewModel.TAG, response.body().toString())

                }

                override fun onFailure(call: Call<AddressResponse>, t: Throwable) {
                    deleteAddressCall = null
                    errorAddressResponseLiveData.value = "deleted failed"
                    JachaiLog.d(GroceriesShopViewModel.TAG, t.localizedMessage)

                }
            })


        } catch (ex: Exception) {
            JachaiLog.d(GroceriesShopViewModel.TAG, ex.message!!)
        }
    }


    fun getAllOrder() {
        try {
            if (orderListCall != null) {
                return
            } else if (!getApplication<JachaiApplication>().isConnectionAvailable()) {
                getApplication<JachaiApplication>().showShortToast(R.string.networkError)
                return
            }

            orderListCall = orderService.getMyAllOrder(
                "2021-10-01",
                "2022-12-01",
                0,
                100

            )

            orderListCall?.enqueue(object : Callback<OrderHistoryResponse> {
                override fun onResponse(
                    call: Call<OrderHistoryResponse>,
                    response: Response<OrderHistoryResponse>
                ) {
                    JachaiLog.d(TAG, response.body().toString())
                    orderListCall = null
                    successOrderHistoryDetailsLiveData.postValue(response.body())
                    if (response.isSuccessful) {
                        if (response.body()?.statusCode ?: 0 == HttpStatusCode.HTTP_OK) {
                            postData(response.body())
                        }
                    }


                }

                override fun onFailure(call: Call<OrderHistoryResponse>, t: Throwable) {
                    orderListCall = null
                    JachaiLog.d(TAG, t.localizedMessage)
                    errorOrderDetailsLiveData.postValue("Failed")

                }
            })

        } catch (ex: Exception) {
            errorDetailsLiveData.postValue("Failed")
            JachaiLog.d(BaseViewModel.TAG, ex.message!!)
        }
    }

    private fun postData(orderHistoryResponse: OrderHistoryResponse?) {
        val orders = orderHistoryResponse?.orders
        val onGoingOrder = mutableListOf<Order>()
        val completedOrder = mutableListOf<Order>()
        if (orders != null) {

            JachaiApplication.mDatabase
                .daoAccess().clearLiveOrderTable()
            for (i in orders.indices) {
                if (orders[i].status.equals(ApiConstants.ORDER_COMPLETED)
                    ||
                    orders[i].status.equals(ApiConstants.ORDER_DELIVERED)
                    ||
                    orders[i].status.equals(ApiConstants.ORDER_REVIEWED)
                    ||
                    orders[i].status.equals(ApiConstants.ORDER_CANCELLED)
                ) {
                    completedOrder.add(orders[i])
                } else {
                    onGoingOrder.add(orders[i])
                    JachaiApplication.mDatabase
                        .daoAccess()
                        .insertOngoingOrder(orders[i])

                }

            }

            successOnGoingOrderListLiveData.postValue(onGoingOrder)
            successPreviousOrderListLiveData.postValue(completedOrder)


        }
    }

    private val paymetService = RetrofitConfig.paymentService
    private var paymentCall: Call<PaymentRequestResponse>? = null

    var successPaymentRequestLiveData = MutableLiveData<PaymentRequestResponse>()

    fun requestPayment( paymentRequest: PaymentRequest) {

        try {
            if (paymentCall != null) {
                return
            } else if (!getApplication<JachaiApplication>().isConnectionAvailable()) {
                getApplication<JachaiApplication>().showShortToast(R.string.networkError)
                return
            }
            paymentCall = paymetService.paymentRequest(paymentRequest)
            JachaiLog.d(TAG, Gson().toJson(paymentRequest).toString())

            paymentCall?.enqueue(object : Callback<PaymentRequestResponse> {
                override fun onResponse(
                    call: Call<PaymentRequestResponse>,
                    response: Response<PaymentRequestResponse>
                ) {
                    JachaiLog.d(TAG, response.body().toString())
                    paymentCall = null
                    successPaymentRequestLiveData.postValue(response.body())


                }

                override fun onFailure(call: Call<PaymentRequestResponse>, t: Throwable) {
                    paymentCall = null
                    JachaiLog.d(TAG, t.localizedMessage)
                    errorPaymentResponseLiveData.postValue("failed")
                }
            })


        } catch (ex: Exception) {
            JachaiLog.d(TAG, ex.message!!)
        }


    }


    fun saveProduct(item: Product, quantity: Int, shopItem: Shop?, isFromSameShop: Boolean) {
        val productOrder = ProductOrder()
        productOrder.product = item.id.toString()
        productOrder.productName = item.name
//        productOrder.quantity = quantity.toInt()
        productOrder.shopId = shopItem?.id!!
        productOrder.shopName = shopItem.name
        productOrder.shopSubtitle = "na"
        productOrder.shopImage = shopItem.logo
        productOrder.image = item.productImage
        productOrder.isChecked = false
        productOrder.isPopular = false


//        var finalProductOrder =
//            item.variations?.get(0)?.let {
//                updatedProductOrder(productOrder, quantity, it)
//            }
        val finalProductOrder =
            item.variations?.get(0)?.let {
                when {
                    item.variations[0]?.maximumOrderLimit == 0 -> {
                        updatedProductOrder(productOrder, quantity, it, false)
                    }
                    quantity <= item.variations[0]?.maximumOrderLimit!! -> {
                        updatedProductOrder(productOrder, quantity, it, false)

                    }
                    else -> {

                        val tempProductOrder = updatedProductOrder(
                            productOrder,
                            item.variations[0]?.maximumOrderLimit!!,
                            it,
                            true
                        )
                        JachaiApplication.mDatabase.daoAccess()
                            .insertOrder(tempProductOrder, isFromSameShop)
                        val mQuantity = quantity - item.variations[0]?.maximumOrderLimit!!
                        val nextVariationsId = item.variations[0]?.regularVariationId

                        val nextVariationsItem: VariationsItem? =
                            getVariationItemById(item.variations, nextVariationsId)

                        if (nextVariationsItem != null) {
                            updatedProductOrder(productOrder, mQuantity, nextVariationsItem, true)
                        } else {
                            null
                        }

                    }
                }
            }



        successAddToCartData.postValue(
            JachaiApplication.mDatabase.daoAccess().insertOrder(finalProductOrder!!, isFromSameShop)
        )


    }

    private fun getVariationItemById(
        variations: List<VariationsItem?>,
        nextVariationsId: String?
    ): VariationsItem? {
        for (item in variations) {
            if (item != null) {
                if (item.variationId.equals(nextVariationsId)) {
                    return item
                }

            }
        }
        return null
    }

    private fun updatedProductOrder(
        tProductOrder: ProductOrder,
        mQuantity: Int,
        variationsItem: VariationsItem,
        isRegularPrice: Boolean
    ): ProductOrder {

        val tempPrice = variationsItem.price?.mrp ?: 0.0
        val tempDiscountedPrice = variationsItem.price?.discountedPrice ?: 0.0

        val mPrice: Double
        val mDiscountPrice: Double


        when {
            tempDiscountedPrice <= 0.0 -> {
                mPrice = tempPrice
                mDiscountPrice = tempPrice
            }
            tempDiscountedPrice > 0 && tempDiscountedPrice < tempPrice -> {
                mPrice = tempPrice
                mDiscountPrice = tempDiscountedPrice
            }
            else -> {
                mPrice = tempPrice
                mDiscountPrice = tempPrice

            }
        }

        tProductOrder.variant = variationsItem.variationName
        tProductOrder.quantity = mQuantity
        tProductOrder.price = mPrice
        tProductOrder.discountedPrice = mDiscountPrice
        tProductOrder.variationId = variationsItem.variationId ?: ""
        tProductOrder.maximumOrderLimit = variationsItem.maximumOrderLimit
        tProductOrder.stock = variationsItem.stock
        return tProductOrder
    }

    fun getAllPaymentMethods() {

        try {
            if (paymentMethodCall != null) {
                return
            } else if (!getApplication<JachaiApplication>().isConnectionAvailable()) {
                getApplication<JachaiApplication>().showShortToast(R.string.networkError)
                return
            }
            paymentMethodCall = paymentService.paymentMethodRequest()

            paymentMethodCall?.enqueue(object : Callback<PaymentListResponse> {
                override fun onResponse(
                    call: Call<PaymentListResponse>,
                    response: Response<PaymentListResponse>
                ) {
                    paymentCall = null

                    when (response.code()) {
                        HttpStatusCode.HTTP_OK -> successPaymentMethodListLiveData.postValue(response.body())
                        else -> {
                            val jObjError = JSONObject(response.errorBody()!!.string())
                            failedResponseLiveData.value =
                                CommonConstants.DEFAULT_NON_NULL_GSON.fromJson(
                                    jObjError.toString(), GenericResponse::class.java
                                ) ?: GenericResponse()
                        }
                    }


                }

                override fun onFailure(call: Call<PaymentListResponse>, t: Throwable) {
                    paymentMethodCall = null
                    JachaiLog.d(TAG, t.localizedMessage)
                    errorResponseLiveData.postValue("failed")
                }
            })


        } catch (ex: Exception) {
            JachaiLog.d(TAG, ex.message!!)
        }


    }


}