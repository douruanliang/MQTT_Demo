package io.dourl.http.coroutine.adapter

import io.dourl.http.coroutine.ApiResponse
import io.dourl.http.coroutine.internal.ApiResponseCallDelegate
import kotlinx.coroutines.CoroutineScope
import retrofit2.Call
import retrofit2.CallAdapter
import java.lang.reflect.Type

/**
 * File description.
 *
 * @author dourl
 * @date 2022/10/25
 */
internal class ApiResponseCallAdapter constructor(
    private val resultType:Type,
    private val coroutineScope: CoroutineScope
) :CallAdapter<Type,Call<ApiResponse<Type>>>{
    override fun responseType(): Type {
        return resultType
    }

    override fun adapt(call: Call<Type>): Call<ApiResponse<Type>> {
        return  ApiResponseCallDelegate(call,coroutineScope)
    }
}