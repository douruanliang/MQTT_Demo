package io.dourl.http.coroutine


@JvmSynthetic
public suspend inline fun <T> ApiResponse<T>.suspendOnSuccess(
    crossinline onResult: suspend ApiResponse.Success<T>.() -> Unit
): ApiResponse<T> {
    if (this is ApiResponse.Success) {
        onResult(this)
    }
    return this
}

@JvmSynthetic
public inline fun <T> ApiResponse<T>.onError(
    crossinline onResult: ApiResponse.Failure.Error<T>.() -> Unit
): ApiResponse<T> {
    if (this is ApiResponse.Failure.Error) {
        onResult(this)
    }
    return this
}

@JvmSynthetic
public inline fun <T> ApiResponse<T>.onException(
    crossinline onResult: ApiResponse.Failure.Exception<T>.() -> Unit
): ApiResponse<T> {
    if (this is ApiResponse.Failure.Exception) {
        onResult(this)
    }
    return this
}


public fun <T> ApiResponse.Failure.Error<T>.message(): String = toString()

/**
 * Returns an error message from the [ApiResponse.Failure.Exception] that consists of the localized message.
 *
 * @return An error message from the [ApiResponse.Failure.Exception].
 */
public fun <T> ApiResponse.Failure.Exception<T>.message(): String = toString()