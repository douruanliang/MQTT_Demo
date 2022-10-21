package io.dourl.mqtt.bean

import io.dourl.mqtt.decoration.ILetterCategoryInterface

/**
 * File description.
 *
 * @author dourl
 * @date 2022/10/21
 */
abstract class BaseIndexBean : ILetterCategoryInterface {

    //所属的分类（城市的汉语拼音首字母）
    var firstLetter: String = ""
        private set

    fun setBaseLetter(baseIndexTag: String): BaseIndexBean {
        firstLetter = baseIndexTag
        return this
    }
    /**
     * 首字母
     */
    override fun getLetter(): String {
        return firstLetter
    }

    //是否需要显示悬停title
    override fun isShowLetterItem(): Boolean {
        return true
    }
}

