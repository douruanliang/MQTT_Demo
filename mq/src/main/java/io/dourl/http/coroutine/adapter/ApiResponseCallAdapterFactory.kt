package io.dourl.http.coroutine.adapter

import io.dourl.http.coroutine.ApiResponse
import io.dourl.http.coroutine.Initializer
import kotlinx.coroutines.CoroutineScope
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * File description.
 *
 * @author dourl
 * @date 2022/10/25
 */
class ApiResponseCallAdapterFactory private constructor(private val coroutineScope: CoroutineScope) :
    CallAdapter.Factory() {
    override fun get(
        returnType: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        // check(returnType is ParameterizedType) { "Call must have generic type (e.g., Call<ResponseBody>)" }
        when (getRawType(returnType)) {
            Call::class.java -> {
                val callType = getParameterUpperBound(0, returnType as ParameterizedType)
                val rawType = getRawType(callType)
                if (rawType != ApiResponse::class.java) {
                    return null
                }
                val resultType = getParameterUpperBound(0, callType as ParameterizedType)
                return ApiResponseCallAdapter(resultType, coroutineScope)
            }
            else -> return null
        }
    }

    public companion object {
        fun create(coroutineScope: CoroutineScope = Initializer.mScope): ApiResponseCallAdapterFactory =
            ApiResponseCallAdapterFactory(coroutineScope)
    }
}