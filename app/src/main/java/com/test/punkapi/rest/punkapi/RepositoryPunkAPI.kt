package com.test.punkapi.rest.punkapi

import android.util.Log
import androidx.lifecycle.ViewModelStoreOwner
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.test.punkapi.App
import com.test.punkapi.R
import com.test.punkapi.rest.interceptors.NetworkAvailableInterceptor
import com.test.punkapi.rest.interceptors.ServerErrorInterceptor
import com.test.punkapi.ui.mainscreen.models.BeerModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RepositoryPunkAPI {


    companion object {

        private val TAG = "RepositoryPunkAPI"

        private fun httpInterceptor(): HttpLoggingInterceptor {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            return interceptor
        }

        private var service: InterfacePunkAPI? = null

        private fun getService(viewModel: ViewModelStoreOwner): InterfacePunkAPI =
            service ?: synchronized(this){
                val httpCLient = OkHttpClient.Builder()
                    .addInterceptor(httpInterceptor())
//                    .addInterceptor(ServerErrorInterceptor(viewModel))
                    .addInterceptor(NetworkAvailableInterceptor(viewModel))
                    .callTimeout(App.instance.resources.getInteger(R.integer.timeOut).toLong(), TimeUnit.SECONDS)
                    .readTimeout(App.instance.resources.getInteger(R.integer.timeOut).toLong(), TimeUnit.SECONDS)
                    .connectTimeout(App.instance.resources.getInteger(R.integer.timeOut).toLong(), TimeUnit.SECONDS)
                    .build()
                val builder = Retrofit.Builder()
                builder.client(httpCLient)
                builder.baseUrl(App.instance.getString(R.string.punkapi_url))
                builder.addCallAdapterFactory(CoroutineCallAdapterFactory())
                builder.addConverterFactory(GsonConverterFactory.create())

                service ?: builder.build().create(InterfacePunkAPI::class.java)
            }

        suspend fun getBeers(viewModel: ViewModelStoreOwner, page: Int, elements: Int) =
            try {
                getService(viewModel).getBeers(page, elements)
            } catch (ex: Exception) {
                null
                Log.e(TAG, ex.message!!)
            }
    }
}