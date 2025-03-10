package nz.co.thewarehouse.productsearch.api

import android.util.Log
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import nz.co.thewarehouse.productsearch.BuildConfig
import javax.inject.Inject
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okio.IOException


class AuthInterceptor @Inject constructor(
    private val tokenManager: TokenManager
) : Interceptor {

    private val client: OkHttpClient = OkHttpClient.Builder().build()
    private var token: String? = null
    private val mutex = Mutex()

    override fun intercept(chain: Interceptor.Chain): Response {
        runBlocking {
            ensureToken()
        }

        val request = chain.request()
            .newBuilder()
            .addHeader("Ocp-Apim-Subscription-Key", BuildConfig.APIM_SUB_KEY)
            .addHeader("X-TWL-Token", token ?: "")
            .build()

        val response = chain.proceed(request)

        if (response.code == 401) {
            runBlocking {
                ensureToken(forceRefresh = true)
            }

            return chain.proceed(
                request.newBuilder()
                    .header("X-TWL-Token", token ?: "")
                    .build()
            )
        }

        return response
    }

    private suspend fun ensureToken(forceRefresh: Boolean = false) {
        mutex.withLock {
            if (token.isNullOrEmpty() || forceRefresh) {
                Log.i("AuthInterceptor", "Fetching token from TokenManager")
                token = tokenManager.getToken()

                if (token.isNullOrEmpty() || forceRefresh) {
                    Log.i("AuthInterceptor", "No valid token found, requesting new one")
                    token = fetchNewToken()
                }
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun fetchNewToken(): String? {
        val request = Request.Builder()
            .url("https://twg.azure-api.net/twgCSharpTest/Login.json")
            .header("Authorization", "Guest")
            .header("X-TWL-Device", "Android")
            .header("Ocp-Apim-Subscription-Key", BuildConfig.APIM_SUB_KEY)
            .build()

        return try {
            val response = client.newCall(request).execute()
            if (response.isSuccessful) {
                val newToken = response.header("X-TWL-Token")

                if (!newToken.isNullOrBlank()) {
                    GlobalScope.launch { tokenManager.saveToken(newToken) }
                    Log.d("AuthInterceptor", "Fetched new token: $newToken")
                    newToken
                } else {
                    Log.e("AuthInterceptor", "Token is empty")
                    null
                }
            } else {
                Log.e("AuthInterceptor", "Failed to refresh token, response code: ${response.code}")
                null
            }
        } catch (e: IOException) {
            Log.e("AuthInterceptor", "Error fetching token: ${e.message}")
            null
        }
    }
}
