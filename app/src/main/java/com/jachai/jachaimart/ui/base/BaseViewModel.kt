package com.jachai.jachaimart.ui.base


import android.app.Application
import androidx.lifecycle.AndroidViewModel


abstract class BaseViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        val TAG = BaseViewModel::class.java
    }


}