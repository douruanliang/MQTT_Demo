package io.dourl.mqtt.bean

/**
 * File description.
 * 索引类的汉语拼音的接口
 * @author dourl
 * @date 2022/10/21
 */
abstract class BaseIndexPinyinBean : BaseIndexBean() {

     var baseIndexPinyin //城市的拼音
            : String? = null
    /**
     * 是否需要被转化成拼音， 类似微信头部那种就不需要 美团的也不需要
       默认应该是需要的
     */
    abstract fun isNeedToPinyin(): Boolean

    //需要转化成拼音的目标字段
    abstract fun getTarget(): String?

}