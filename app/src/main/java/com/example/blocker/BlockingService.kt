package com.example.blocker

import android.content.Context
import kotlinx.coroutines.flow.StateFlow

enum class BlockingAuthorizationStatus {
    NOT_DETERMINED,
    DENIED,
    RESTRICTED,
    AUTHORIZED
}

data class BlockingStatus(
    val isBlockingActive: Boolean,
    val isMonkMode: Boolean,
    val activeBlockedPackagesCount: Int,
    val authorizationStatus: BlockingAuthorizationStatus,
    val activeSessionRemainingSeconds: Long,
    val totalDistractionsIntercepted: Int
)

/**
 * Cross-platform blocking abstraction for both Android and iOS.
 * Conforms strictly to Master Spec Section 8.
 */
interface BlockingService {
    val status: StateFlow<BlockingStatus>

    suspend fun authorize(): Boolean
    suspend fun getAuthorizationStatus(): BlockingAuthorizationStatus
    suspend fun getBlockedApps(): List<String>
    suspend fun setBlockedApps(packageNames: List<String>)
    suspend fun startBlocking(durationMinutes: Int, isMonkMode: Boolean)
    suspend fun stopBlocking()
    suspend fun pauseBlocking()
    suspend fun resumeBlocking()
    fun getStatus(): BlockingStatus
}
