package com.example.focus

import android.content.Context
import com.example.FocusForgeApplication
import com.example.data.model.DistractionLogEntity
import com.example.data.model.SessionStatus
import com.example.data.model.StudySessionEntity
import com.example.data.model.SubjectDomain
import com.example.data.repository.FocusForgeRepository
import com.example.timer.FocusTimerService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

data class ActiveSessionConfig(
    val id: String = UUID.randomUUID().toString(),
    val subject: SubjectDomain? = SubjectDomain.PHYSICS,
    val chapterName: String = "Units & Measurements",
    val plannedMinutes: Int = 25,
    val status: SessionStatus = SessionStatus.IDLE,
    val isMonkMode: Boolean = false,
    val ambientSound: String = "Rain"
)

object FocusEngine {

    private val engineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private var timerJob: Job? = null

    private val _sessionState = MutableStateFlow(ActiveSessionConfig())
    val sessionState: StateFlow<ActiveSessionConfig> = _sessionState.asStateFlow()

    private val _remainingSeconds = MutableStateFlow(25L * 60L)
    val remainingSeconds: StateFlow<Long> = _remainingSeconds.asStateFlow()

    private val _elapsedSeconds = MutableStateFlow(0L)
    val elapsedSeconds: StateFlow<Long> = _elapsedSeconds.asStateFlow()

    private val _isMonkMode = MutableStateFlow(false)
    val isMonkMode: StateFlow<Boolean> = _isMonkMode.asStateFlow()

    // Offline-first cached blocked packages
    private val blockedPackages = mutableSetOf<String>()

    init {
        // Preload default blocked packages
        blockedPackages.addAll(
            listOf(
                "com.instagram.android",
                "com.google.android.youtube",
                "com.reddit.frontpage",
                "com.discord",
                "com.twitter.android",
                "com.snapchat.android",
                "com.facebook.katana",
                "com.netflix.mediaclient",
                "com.amazon.avod.thirdpartyclient",
                "com.disney.disneyplus",
                "tv.twitch.android.app",
                "com.zhiliaoapp.musically",
                "com.pinterest",
                "com.valvesoftware.android.steam.community",
                "com.ninegag.android.app",
                "com.whatsapp",
                "org.telegram.messenger",
                "com.roblox.client",
                "com.chess"
            )
        )
    }

    fun isBlockingActive(): Boolean {
        return _sessionState.value.status == SessionStatus.ACTIVE
    }

    fun isPackageBlocked(packageName: String): Boolean {
        return blockedPackages.contains(packageName)
    }

    fun setBlockedPackages(packages: Collection<String>) {
        blockedPackages.clear()
        blockedPackages.addAll(packages)
    }

    fun addBlockedPackage(packageName: String) {
        blockedPackages.add(packageName)
    }

    fun removeBlockedPackage(packageName: String) {
        blockedPackages.remove(packageName)
    }

    fun configureSession(subject: SubjectDomain, chapter: String, durationMinutes: Int, monkMode: Boolean) {
        if (_sessionState.value.status == SessionStatus.IDLE) {
            _sessionState.value = _sessionState.value.copy(
                id = UUID.randomUUID().toString(),
                subject = subject,
                chapterName = chapter,
                plannedMinutes = durationMinutes,
                isMonkMode = monkMode
            )
            _remainingSeconds.value = durationMinutes * 60L
            _elapsedSeconds.value = 0L
            _isMonkMode.value = monkMode
        }
    }

    fun toggleMonkMode(enabled: Boolean) {
        _isMonkMode.value = enabled
        _sessionState.value = _sessionState.value.copy(isMonkMode = enabled)
    }

    fun startSession(context: Context? = null) {
        if (_sessionState.value.status == SessionStatus.IDLE || _sessionState.value.status == SessionStatus.PAUSED) {
            _sessionState.value = _sessionState.value.copy(status = SessionStatus.ACTIVE)
            context?.let { FocusTimerService.startService(it) }
            startTimerLoop(context)
        }
    }

    fun pauseSession() {
        if (_sessionState.value.status == SessionStatus.ACTIVE) {
            _sessionState.value = _sessionState.value.copy(status = SessionStatus.PAUSED)
            timerJob?.cancel()
        }
    }

    fun resumeSession(context: Context? = null) {
        if (_sessionState.value.status == SessionStatus.PAUSED) {
            _sessionState.value = _sessionState.value.copy(status = SessionStatus.ACTIVE)
            startTimerLoop(context)
        }
    }

    fun completeSession(context: Context? = null) {
        timerJob?.cancel()
        context?.let { FocusTimerService.stopService(it) }
        val current = _sessionState.value
        val actualMins = (_elapsedSeconds.value / 60).toInt().coerceAtLeast(1)
        val xpEarned = actualMins * 10

        _sessionState.value = current.copy(status = SessionStatus.COMPLETED)

        engineScope.launch(Dispatchers.IO) {
            FocusForgeApplication.instance?.repository?.let { repo ->
                val entity = StudySessionEntity(
                    id = current.id,
                    subject = current.subject ?: SubjectDomain.PHYSICS,
                    chapterName = current.chapterName,
                    plannedMinutes = current.plannedMinutes,
                    actualMinutes = actualMins,
                    status = SessionStatus.COMPLETED,
                    startTimeMs = System.currentTimeMillis() - (_elapsedSeconds.value * 1000),
                    endTimeMs = System.currentTimeMillis(),
                    xpEarned = xpEarned,
                    wasCompleted = true
                )
                repo.saveCompletedSession(entity)
            }
        }
    }

    fun cancelSession(context: Context? = null) {
        timerJob?.cancel()
        context?.let { FocusTimerService.stopService(it) }
        _sessionState.value = _sessionState.value.copy(status = SessionStatus.CANCELLED)
        _remainingSeconds.value = _sessionState.value.plannedMinutes * 60L
        _elapsedSeconds.value = 0L
        _sessionState.value = _sessionState.value.copy(status = SessionStatus.IDLE)
    }

    private fun startTimerLoop(context: Context?) {
        timerJob?.cancel()
        timerJob = engineScope.launch {
            while (_remainingSeconds.value > 0 && _sessionState.value.status == SessionStatus.ACTIVE) {
                delay(1000)
                _remainingSeconds.value = (_remainingSeconds.value - 1).coerceAtLeast(0)
                _elapsedSeconds.value += 1

                if (_remainingSeconds.value == 0L) {
                    completeSession(context)
                    break
                }
            }
        }
    }

    suspend fun recordDistraction(packageName: String, repository: FocusForgeRepository) {
        val appName = packageName.substringAfterLast(".").replaceFirstChar { it.uppercase() }
        val log = DistractionLogEntity(
            packageName = packageName,
            appName = appName,
            durationSeconds = 0,
            timestampMs = System.currentTimeMillis(),
            isBlocked = true,
            wasAllowed = false,
            context = "${_sessionState.value.subject?.displayName ?: "Focus"}: ${_sessionState.value.chapterName}"
        )
        repository.recordDistraction(log)
    }
}
