package com.jachai.jachai_driver.ui.register.status

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.jachai.jachai_driver.utils.isConnectionAvailable
import com.jachai.jachaimart.JachaiApplication
import com.jachai.jachaimart.elearning.model.response.ClassListResponse
import com.jachai.jachaimart.elearning.model.response.DiciplineListResponse
import com.jachai.jachaimart.elearning.model.response.ProgramListResponse
import com.jachai.jachaimart.utils.HttpStatusCode
import com.jachai.jachaimart.utils.RetrofitConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SelectProgramViewModel (application: Application) : AndroidViewModel(application) {

    private var programmListCall: Call<ProgramListResponse>? = null
    private var classListCall: Call<ClassListResponse>? = null
    private var diciplineListCall: Call<DiciplineListResponse>? = null
    private val eLearningService = RetrofitConfig.eLearningService
    var successResponseLiveData = MutableLiveData<ProgramListResponse?>()
    var classListSuccessResponseLiveData = MutableLiveData<ClassListResponse?>()
    var diciplineListSuccessResponseLiveData = MutableLiveData<DiciplineListResponse?>()

    fun getProgramList() {
        if (programmListCall != null) {
            return
        } else if (!getApplication<JachaiApplication>().isConnectionAvailable()) {
            // alertMessageListener?.showAlertMessage(R.string.no_internet_connection_message, AlertType.Snackbar)

            return
        }

        programmListCall = eLearningService.getAllProgram()

        programmListCall?.enqueue(object : Callback<ProgramListResponse> {
            override fun onResponse(call: Call<ProgramListResponse>?, response: Response<ProgramListResponse>?) {
                programmListCall = null
                when (response?.code()) {
                    HttpStatusCode.HTTP_OK -> successResponseLiveData.value = response.body()
                }
            }

            override fun onFailure(call: Call<ProgramListResponse>?, t: Throwable?) {
            }
        })
    }


    fun getClassList(programId: String) {
        if (classListCall != null) {
            return
        } else if (!getApplication<JachaiApplication>().isConnectionAvailable()) {
            // alertMessageListener?.showAlertMessage(R.string.no_internet_connection_message, AlertType.Snackbar)

            return
        }

        classListCall = eLearningService.getAllClass(programId)

        classListCall?.enqueue(object : Callback<ClassListResponse> {
            override fun onResponse(call: Call<ClassListResponse>?, response: Response<ClassListResponse>?) {
                classListCall = null
                when (response?.code()) {
                    HttpStatusCode.HTTP_OK -> classListSuccessResponseLiveData.value = response.body()
                }
            }

            override fun onFailure(call: Call<ClassListResponse>?, t: Throwable?) {
            }
        })
    }

    fun getDiciplineList(programId: String, curriculumId: String) {
        if (diciplineListCall != null) {
            return
        } else if (!getApplication<JachaiApplication>().isConnectionAvailable()) {
            // alertMessageListener?.showAlertMessage(R.string.no_internet_connection_message, AlertType.Snackbar)

            return
        }

        diciplineListCall = eLearningService.getAllDicipline(programId, curriculumId)

        diciplineListCall?.enqueue(object : Callback<DiciplineListResponse> {
            override fun onResponse(call: Call<DiciplineListResponse>?, response: Response<DiciplineListResponse>?) {
                diciplineListCall = null
                when (response?.code()) {
                    HttpStatusCode.HTTP_OK -> diciplineListSuccessResponseLiveData.value = response.body()
                }
            }

            override fun onFailure(call: Call<DiciplineListResponse>?, t: Throwable?) {
            }
        })
    }
}