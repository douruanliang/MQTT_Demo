package io.dourl.mqtt.utils

import android.util.Base64
import io.dourl.mqtt.utils.log.LoggerUtil
import java.lang.Exception
import java.lang.RuntimeException
import java.security.*
import java.util.*
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec


/**
 */
object AESUtil {

    private const val KEY_ALGORITHM = "AES"
    private const val TAG = "AESUtil"
    private val KEY = "cE1id0ZhcWpkOycmeHE/dw=="
    private val IV_KEY = "bmBQNm5yMXYxQyV0L21yNQ=="


    /**
     * 默认的加密算法
     */
    private const val DEFAULT_CIPHER_ALGORITHM = "AES/CBC/PKCS7Padding"

    fun test(content:String) {//可以用来自测测试
        LoggerUtil.e(TAG, "原文=$content")
        val s1 = encrypt(content)
        LoggerUtil.e(TAG, "加密结果=$s1")
        val decrypt = decrypt(s1)
        LoggerUtil.e(TAG, "解密结果=$decrypt")
    }

    /**
     *加密
     */
    fun encrypt(originalContent: String?): String? {
        return try {
            val cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM)
            val skeySpec = SecretKeySpec(base64Decode(KEY), KEY_ALGORITHM)
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, IvParameterSpec(base64Decode(IV_KEY)))
            val encrypted = cipher.doFinal(originalContent?.toByteArray())
            base64Encode(encrypted)
        } catch (e: java.lang.Exception) {
            throw RuntimeException(e)

        }
    }

    /**
     * AES解密
     * 填充模式AES/CBC/PKCS7Padding
     * 解密模式128
     * @param content
     * 目标密文
     * @return
     * @throws Exception
     * @throws InvalidKeyException
     * @throws NoSuchProviderException
     */
    fun decrypt(content: String?): String? {
        return try {
            val cipher =
                Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM)
            val sKeySpec: Key = SecretKeySpec(base64Decode(KEY), KEY_ALGORITHM)
            cipher.init(Cipher.DECRYPT_MODE, sKeySpec, generateIV(base64Decode(IV_KEY))) // 初始化
            val decrypted = cipher.doFinal(base64Decode(content))
            String(decrypted)
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }


    // 生成iv
    @Throws(Exception::class)
    fun generateIV(iv: ByteArray?): AlgorithmParameters? {
        val params = AlgorithmParameters.getInstance("AES")
        params.init(IvParameterSpec(iv))
        return params
    }

    /**
     * 将 字节数组 转换成 Base64 编码
     * 用Base64.DEFAULT模式会导致加密的text下面多一行（在应用中显示是这样）
     */
    fun base64Encode(data: ByteArray?): String {
        return Base64.encodeToString(data, Base64.NO_WRAP)
    }

    /**
     * 将 Base64 字符串 解码成 字节数组
     */
    fun base64Decode(data: String?): ByteArray {
        return Base64.decode(data, Base64.NO_WRAP)
    }


    /**
     * 使用指定的字符串生成秘钥
     */
    fun getKeyByPass(keyRaw: String): String {
        return try {
            val kg = KeyGenerator.getInstance("AES")
            // kg.init(128);//要生成多少位，只需要修改这里即可128, 192或256
            //SecureRandom是生成安全随机数序列，password.getBytes()是种子，只要种子相同，序列就一样，所以生成的秘钥就一样。
            kg.init(128, SecureRandom(keyRaw.toByteArray()))
            val sk = kg.generateKey()
            val b = sk.encoded
            byteToHexString(b)
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
            ""
        }
    }

    private fun byteToHexString(bytes: ByteArray): String {
        val sb = StringBuffer()
        for (i in bytes.indices) {
            val strHex = Integer.toHexString(bytes[i].toInt())
            if (strHex.length > 3) {
                sb.append(strHex.substring(6))
            } else {
                if (strHex.length < 2) {
                    sb.append("0$strHex")
                } else {
                    sb.append(strHex)
                }
            }
        }
        return sb.toString()
    }


}