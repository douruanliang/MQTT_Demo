package io.dourl.mqtt.const

import android.os.Build

/**
 * File description.
 *
 * @author dourl
 * @date 2022/2/23
 */
class Constants {
    var VERSION_NAME: String? = null

    var VERSION_CODE = 0

    var PACKAGE_NAME: String? = null

    var MAC_ADDRESS: String? = null

    var DEVICEID = ""

    var SCREENWIDTH = 0

    var SCREENHEIGHT = 0

    var SCREENDENSITY = 0f

    var SCREENDENSITYDPI = 0f

    var SCREENSCALEDDENSIT = 0f

    var CHANNEL = "main"

    var NETWORKCOUNTRYISO = ""

    var SIMCOUNTRYISO = ""

    var NETWORKOPERATOR = ""

    var SIMOPERATOR = ""

    var TimeZone = ""

    val DEVICE_NAME: String = "DeviceInfoUtils.getDeviceName()"

    val OS_VERSION = "Android " + Build.VERSION.RELEASE

    val USER_AGENT: String = "DeviceInfoUtils.getUserAgent()"
}