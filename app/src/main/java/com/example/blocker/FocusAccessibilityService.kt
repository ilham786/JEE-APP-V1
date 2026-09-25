package com.example.blocker

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.view.accessibility.AccessibilityEvent
import com.example.FocusForgeApplication
import com.example.focus.FocusEngine
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class FocusAccessibilityService : AccessibilityService() {

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    private var lastInterceptionTime = 0L

    override fun onServiceConnected() {
        super.onServiceConnected()
        instance = this
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event == null || event.eventType != AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) return

        val foregroundPackage = event.packageName?.toString() ?: return
        val myPackage = packageName

        // Ignore FocusForge itself, system UI, and launcher packages
        if (foregroundPackage == myPackage ||
            foregroundPackage == "com.android.systemui" ||
            foregroundPackage == "android" ||
            foregroundPackage.contains("launcher", ignoreCase = true)
        ) {
            return
        }

        // Check if Focus Engine is active and package is blocked
        if (FocusEngine.isBlockingActive()) {
            val isBlocked = FocusEngine.isPackageBlocked(foregroundPackage)
            if (isBlocked) {
                val now = System.currentTimeMillis()
                // Prevent event spamming within 1.5 seconds
                if (now - lastInterceptionTime > 1500) {
                    lastInterceptionTime = now
                    handleBlockedAppIntervention(foregroundPackage)
                }
            }
        }
    }

    private fun handleBlockedAppIntervention(packageName: String) {
        // Record distraction log asynchronously in Room database
        serviceScope.launch(Dispatchers.IO) {
            FocusForgeApplication.instance?.repository?.let { repo ->
                FocusEngine.recordDistraction(packageName, repo)
            }
        }

        // Send HOME global action to collapse blocked app
        performGlobalAction(GLOBAL_ACTION_HOME)

        // Launch the native FocusForge intervention screen
        val interventionIntent = Intent(this, BlockingInterventionActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_CLEAR_TOP or
                    Intent.FLAG_ACTIVITY_SINGLE_TOP
            putExtra(BlockingInterventionActivity.EXTRA_BLOCKED_PACKAGE, packageName)
        }
        startActivity(interventionIntent)
    }

    override fun onInterrupt() {
        // Service interrupted by OS
    }

    override fun onDestroy() {
        super.onDestroy()
        instance = null
    }

    companion object {
        var instance: FocusAccessibilityService? = null
            private set

        fun isConnected(): Boolean = instance != null
    }
}
