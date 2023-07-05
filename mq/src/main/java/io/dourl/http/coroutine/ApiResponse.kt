package io.dourl.http.coroutine

import io.dourl.http.coroutine.exception.NoContentException
import okhttp3.Headers
import okhttp3.ResponseBody
import retrofit2.Response

public sealed class ApiResponse<out T> {

    /**
     *  API Success response class from OkHttp request call
     */
    data class Success<T>(val response: Response<T>) : ApiResponse<T>() {
        val statusCode: ResponseStatusCode = getStatusCodeFromResponse(response)
        val headers: Headers = response.headers()
        val raw: okhttp3.Response = response.raw()
        val data: T by lazy { response.body() ?: throw NoContentException(statusCode.code) }

    }


    /**
     *  API Failure response class from OkHttp request call.
     */
    public sealed class Failure<T> : ApiResponse<T>() {
        /**
         * API response error case.
         * API communication conventions do not match or applications need to handle errors.
         */
        public data class Error<T>(val response: Response<T>) : Failure<T>() {
            val statusCode: ResponseStatusCode = getStatusCodeFromResponse(response)
            val headers: Headers = response.headers()
            val raw: okhttp3.Response = response.raw()
            val errorBody: ResponseBody? = response.errorBody()
            override fun toString(): String {
                val errorBody = errorBody?.string()
                return if (!errorBody.isNullOrEmpty()) {
                    errorBody
                } else {
                    "[ApiResponse.Failure.Error-$statusCode](errorResponse=$response)"
                }
            }
        }

        /**
         * API request Exception case.
         */
        public data class Exception<T>(val exception: Throwable) : Failure<T>() {
            val message: String? = exception.localizedMessage
            override fun toString(): String = "[ApiResponse.Failure.Exception](message=$message)"
        }

    }


    fun <T> getStatusCodeFromResponse(response: Response<T>): ResponseStatusCode {
        return ResponseStatusCode.values().find { it.code == response.code() }
            ?: ResponseStatusCode.Unknown
    }


    public companion object {

        public fun <T> error(exception: Throwable): Failure.Exception<T> =
            Failure.Exception<T>(exception).apply {

            }

        @JvmSynthetic
        public inline fun <T> of(
            successCodeRange: IntRange = Initializer.successCodeRange,
            crossinline f: () -> Response<T>
        ): ApiResponse<T> = try {
            val response = f()
            if (response.raw().code in successCodeRange) {
                Success(response)
            } else {
                Failure.Error(response)
            }
        } catch (ex: Exception) {
            Failure.Exception(ex)
        }
    }
}
