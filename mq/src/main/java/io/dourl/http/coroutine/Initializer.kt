package io.dourl.http.coroutine

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import okio.Timeout

/**
 * File description.
 *
 * @author dourl
 * @date 2022/10/24
 */
public object Initializer {
    @JvmSynthetic
    public var  mScope:CoroutineScope = CoroutineScope(Dispatchers.IO)
    @JvmStatic
    public var successCodeRange: IntRange = 200..299
    @JvmStatic
    public var sandwichTimeout: Timeout? = null
}