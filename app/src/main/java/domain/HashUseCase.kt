package domain

import config.AppConstant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.math.BigInteger
import java.security.MessageDigest

class HashUseCase {

    suspend fun hash(s : String) : String = withContext(Dispatchers.Default) {
        val messageDigest = MessageDigest.getInstance(AppConstant.HASH_ALGORITHM)
        val arrayContent = messageDigest.digest(s.toByteArray())
        val bigInteger = BigInteger(1, arrayContent)
        var hexValue = bigInteger.toString(16)
        while (hexValue.length < 32) {
            hexValue = "0$hexValue"
        }
        return@withContext hexValue
    }
}