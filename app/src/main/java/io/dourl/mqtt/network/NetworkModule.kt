package io.dourl.mqtt.network

import io.dourl.http.coroutine.adapter.ApiResponseCallAdapterFactory
import io.dourl.mqtt.api_service.ServicePic
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkModule {
    private val okHttp: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(RequestInterceptor())
        .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .client(okHttp)
        .baseUrl("https://pixabay.com")
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(ApiResponseCallAdapterFactory.create())
        .build()

    val  picService :ServicePic = retrofit.create(ServicePic::class.java)
}