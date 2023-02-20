package dev.omkartenkale.explodable

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

@Composable
fun rememberExplosionController() = remember { ExplosionController() }

internal enum class ExplosionRequest {
    EXPLODE, RESET
}

class ExplosionController {
    private val _explosionRequests = MutableSharedFlow<ExplosionRequest>(
        replay = 0, extraBufferCapacity = 1, onBufferOverflow = BufferOverflow.DROP_LATEST
    )
    internal val explosionRequests: SharedFlow<ExplosionRequest> = _explosionRequests

    fun explode() {
        _explosionRequests.tryEmit(ExplosionRequest.EXPLODE)
    }

    fun reset() {
        _explosionRequests.tryEmit(ExplosionRequest.RESET)
    }
}