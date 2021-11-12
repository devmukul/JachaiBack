package com.jachai.jachaimart.ui.base


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.jachai.jachai_driver.utils.JachaiLog
import com.jachai.jachai_driver.utils.isConnectionAvailable
import com.jachai.jachai_driver.utils.showShortToast
import com.jachai.jachaimart.JachaiFoodApplication
import com.jachai.jachaimart.R
import com.jachai.jachaimart.model.order.details.OrderDetailsResponse
import com.jachai.jachaimart.model.response.GenericResponse
import com.jachai.jachaimart.model.response.address.AddressResponse
import com.jachai.jachaimart.model.response.address.Location
import com.jachai.jachaimart.model.response.grocery.NearestJCShopResponse
import com.jachai.jachaimart.ui.groceries.GroceriesShopViewModel
import com.jachai.jachaimart.utils.HttpStatusCode
import com.jachai.jachaimart.utils.RetrofitConfig
import com.jachai.jachaimart.utils.SharedPreferenceUtil
import com.jachai.jachaimart.utils.constant.CommonConstants
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

    private var nearestJCShopCall: Call<NearestJCShopResponse>? = null
    private var addressCall: Call<AddressResponse>? = null
    private var deleteAddressCall: Call<AddressResponse>? = null
    private var orderCall: Call<OrderDetailsResponse>? = null


    var successOrderDetailsLiveData = MutableLiveData<OrderDetailsResponse>()
    var errorDetailsLiveData = MutableLiveData<String>()


    var successAddressResponseLiveData = MutableLiveData<AddressResponse?>()
    var deleteAddressResponseLiveData = MutableLiveData<AddressResponse?>()
    var successNearestJCShopUpdate = MutableLiveData<Boolean?>()
    var errorAddressResponseLiveData = MutableLiveData<String?>()

    fun requestAllAddress() {
        try {
            if (addressCall != null) {
                return
            } else if (!getApplication<JachaiFoodApplication>().isConnectionAvailable()) {
                getApplication<JachaiFoodApplication>().showShortToast(R.string.networkError)
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

    fun getNearestJCShop(location: Location) {
        try {

            if (nearestJCShopCall != null) {
                return
            } else if (!getApplication<JachaiFoodApplication>().isConnectionAvailable()) {
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
                                if (mResponse.shops.isNotEmpty()) {
                                    SharedPreferenceUtil.setJCShopId(mResponse.shops[0].id)
                                    SharedPreferenceUtil.saveNearestShop(mResponse.shops[0])
                                    successNearestJCShopUpdate.value = true
                                } else {
                                    SharedPreferenceUtil.setJCShopId(null)
                                    successNearestJCShopUpdate.value = false
                                }
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<NearestJCShopResponse>?, t: Throwable?) {
                    nearestJCShopCall = null
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
            } else if (!getApplication<JachaiFoodApplication>().isConnectionAvailable()) {
                getApplication<JachaiFoodApplication>().showShortToast(R.string.networkError)
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

    fun getDiscountPrice() = JachaiFoodApplication.mDatabase.daoAccess().geDiscountPrice()


    fun deleteAddress(addressID: String) {
        try {
            if (deleteAddressCall != null) {
                return
            } else if (!getApplication<JachaiFoodApplication>().isConnectionAvailable()) {
                getApplication<JachaiFoodApplication>().showShortToast(R.string.networkError)
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
}