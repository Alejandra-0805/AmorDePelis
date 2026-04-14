package com.alejandra.amordepelis.core.storage

import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

@Singleton
class TokenProvider @Inject constructor(
    private val tokenDataStore: TokenDataStore
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    @Volatile
    private var cachedToken: String? = null

    init {
        scope.launch {
            cachedToken = tokenDataStore.getToken()
            tokenDataStore.tokenFlow.collect { token ->
                cachedToken = token
            }
        }
    }

    fun getToken(): String? = cachedToken

    fun forceUpdateToken(token: String?) {
        cachedToken = token
    }
}
