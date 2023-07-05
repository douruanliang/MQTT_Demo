package io.dourl.http.coroutine.exception

/**
 * File description.
 *
 * @author dourl
 * @date 2022/10/24
 */
class NoContentException constructor(
    public val code:Int,
    override val message: String?  =
        "The server has successfully fulfilled the request with the code ($code) and that there is " +
                "no additional content to send in the response payload body."
) :Throwable(message)