package com.example.blocker

import android.content.Context
import com.example.FocusForgeApplication
import com.example.focus.FocusEngine
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AndroidBlockingService(private val context: Context) : BlockingService {

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val _status = MutableStateFlow(
        BlockingStatus(
            isBlockingActive = false,
            isMonkMode = false,
            activeBlockedPackagesCount = 15,
            authorizationStatus = checkAuthorizationStatus(),
            activeSessionRemainingSeconds = 0,
            totalDistractionsIntercepted = 0
        )
    )
    override val status: StateFlow<BlockingStatus> = _status.asStateFlow()

    init {
        updateInternalStatus()
    }

    private fun checkAuthorizationStatus(): BlockingAuthorizationStatus {
        val hasAccessibility = AndroidBlockingPermissionHelper.isAccessibilityServiceEnabled(context)
        val hasUsage = AndroidBlockingPermissionHelper.hasUsageStatsPermission(context)
        return when {
            hasAccessibility -> BlockingAuthorizationStatus.AUTHORIZED
            hasUsage -> BlockingAuthorizationStatus.AUTHORIZED
            else -> BlockingAuthorizationStatus.NOT_DETERMINED
        }
    }

    private fun updateInternalStatus() {
        val authStatus = checkAuthorizationStatus()
        _status.value = _status.value.copy(
            isBlockingActive = FocusEngine.isBlockingActive(),
            isMonkMode = FocusEngine.isMonkMode.value,
            authorizationStatus = authStatus,
            activeSessionRemainingSeconds = FocusEngine.remainingSeconds.value
        )
    }

    override suspend fun authorize(): Boolean {
        return checkAuthorizationStatus() == BlockingAuthorizationStatus.AUTHORIZED
    }

    override suspend fun getAuthorizationStatus(): BlockingAuthorizationStatus {
        val status = checkAuthorizationStatus()
        _status.value = _status.value.copy(authorizationStatus = status)
        return status
    }

    override suspend fun getBlockedApps(): List<String> {
        val repo = FocusForgeApplication.instance?.repository ?: return emptyList()
        return repo.getActiveBlockedApps().map { it.packageName }
    }

    override suspend fun setBlockedApps(packageNames: List<String>) {
        FocusEngine.setBlockedPackages(packageNames)
        _status.value = _status.value.copy(activeBlockedPackagesCount = packageNames.size)
    }

    override suspend fun startBlocking(durationMinutes: Int, isMonkMode: Boolean) {
        FocusEngine.toggleMonkMode(isMonkMode)
        FocusEngine.startSession(context)
        updateInternalStatus()
    }

    override suspend fun stopBlocking() {
        FocusEngine.completeSession(context)
        updateInternalStatus()
    }

    override suspend fun pauseBlocking() {
        FocusEngine.pauseSession()
        updateInternalStatus()
    }

    override suspend fun resumeBlocking() {
        FocusEngine.resumeSession(context)
        updateInternalStatus()
    }

    override fun getStatus(): BlockingStatus {
        updateInternalStatus()
        return _status.value
    }
}
