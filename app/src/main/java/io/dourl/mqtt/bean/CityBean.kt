package io.dourl.mqtt.bean


data class CityBean(
    val name: String,
    var isHeaderType : Boolean = false
) : BaseIndexPinyinBean() {


    override fun getTarget(): String? {
        return name
    }

    override fun isNeedToPinyin(): Boolean {
        return !isHeaderType
    }

    override fun isShowLetterItem(): Boolean {
        return !isHeaderType
    }
}