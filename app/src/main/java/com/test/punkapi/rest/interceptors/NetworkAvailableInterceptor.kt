package com.test.punkapi.rest.interceptors

import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.test.punkapi.rest.exceptions.NoNetworkAvalaibleException
import com.test.punkapi.ui.base.BaseModel
import com.test.punkapi.ui.tools.NetworkChecker
import okhttp3.Interceptor
import okhttp3.Response

class NetworkAvailableInterceptor(private val viewModel: ViewModelStoreOwner): Interceptor {

    private val TAG = "NetworkInterceptor"
    private lateinit var model: BaseModel

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        model = ViewModelProvider(viewModel)[BaseModel::class.java]
        if (!NetworkChecker.isConnected()) {
            Log.d(TAG, "Device is not connect to any network")
            model.isConnected.postValue(false)
            throw NoNetworkAvalaibleException("No connected to Internet")
        } else
            model.isConnected.postValue(true)
        return chain.proceed(request)
    }
}