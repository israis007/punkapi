package com.test.punkapi.rest.interceptors

import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.test.punkapi.rest.exceptions.NoServiceAvailableException
import com.test.punkapi.ui.base.BaseModel
import com.test.punkapi.ui.base.models.NotServiceResponse
import okhttp3.Interceptor
import okhttp3.Response

class ServerErrorInterceptor(private val viewModel: ViewModelStoreOwner): Interceptor {

    private val TAG = "ServerErrorInterceptor"
    private lateinit var model: BaseModel

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)
        Log.d(TAG, "Response code: ${response.code()}")
        if (response.code() > 500){
            Log.e(TAG, "Server is Broken, body: ${response.body()}")
            model = ViewModelProvider(viewModel)[BaseModel::class.java]
            model.notServiceResponse.postValue(NotServiceResponse(response.code(), response.body().toString(), response.message()))
            throw NoServiceAvailableException()
        }
        return response
    }
}