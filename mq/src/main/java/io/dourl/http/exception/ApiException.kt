package io.dourl.http.exception

import java.lang.Exception

open class ApiException(val code: Int, message:String) :Exception(message) {
}