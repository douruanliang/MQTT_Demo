package io.dourl.mqtt.utils
import android.util.Base64
import io.dourl.mqtt.manager.LoginManager
import io.dourl.mqtt.utils.log.LoggerUtil
import java.security.AlgorithmParameters
import java.security.InvalidKeyException
import java.security.Key
import java.security.NoSuchProviderException
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec


/**
 * 简单的加密
 */
object AESUtil {

    private const val KEY_ALGORITHM = "AES"
    private const val TAG = "AESUtil"
    private val KEY = LoginManager.getCurrentUserId() // 用户ID
    private val IV_KEY = "bmBQNm5yMXYxQyV0L21yNQ=="


    /**
     * 默认的加密算法
     */
    private const val DEFAULT_CIPHER_ALGORITHM = "AES/CBC/PKCS7Padding"

    fun test(content: String) {//可以用来自测测试
        LoggerUtil.e(TAG, "原文=$content")
        val s1 = encrypt(content, "")
        LoggerUtil.e(TAG, "加密结果=$s1")
        val decrypt = decrypt(s1)
        LoggerUtil.e(TAG, "解密结果=$decrypt")
    }

    /**
     *加密
     */
    fun encrypt(originalContent: String?, key: String?): String? {
        return try {
            val cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM)
            val skeySpec = SecretKeySpec(
                key?.let { generateKey(it) },
                KEY_ALGORITHM
            ) //返回基本编码格式的密钥，如果此密钥不支持编码，则返回
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
            val sKeySpec: Key = SecretKeySpec(generateKey(KEY), KEY_ALGORITHM)
            cipher.init(Cipher.DECRYPT_MODE, sKeySpec, generateIV(base64Decode(IV_KEY))) // 初始化
            val decrypted = cipher.doFinal(base64Decode(content))
            String(decrypted)
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

    fun decrypt(content: String,key: String): String? {
        return try {
            val cipher =
                Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM)
            val sKeySpec: Key = SecretKeySpec(generateKey(key), KEY_ALGORITHM)
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
     * 补充字典
     */
    private const val DIC = "1231g81f5456hhssg84h1f9q3f2x789s"

    @Throws(java.lang.Exception::class)
    private fun generateKey(key: String): ByteArray? {
        val dics = DIC.toByteArray(charset("UTF-8"))
        val bkeys = key.toByteArray(charset("UTF-8"))
        var keys = bkeys
        val keylength = bkeys.size
        if (keylength > 0 && keylength < 16) {
            keys = ByteArray(16)
            System.arraycopy(bkeys, 0, keys, 0, keylength)
            for (i in keylength..15) {
                keys[i] = dics[i]
            }
        }
        if (keylength > 16 && keylength < 24) {
            keys = ByteArray(24)
            System.arraycopy(bkeys, 0, keys, 0, keylength)
            for (i in keylength..23) {
                keys[i] = dics[i]
            }
        }
        if (keylength > 24 && keylength < 32) {
            keys = ByteArray(32)
            System.arraycopy(bkeys, 0, keys, 0, keylength)
            for (i in keylength..31) {
                keys[i] = dics[i]
            }
        }
        if (keylength > 32) {
            keys = ByteArray(32)
            System.arraycopy(bkeys, 0, keys, 0, 32)
        }
        return keys
    }


}