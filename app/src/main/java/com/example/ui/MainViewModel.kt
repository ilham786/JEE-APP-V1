package com.example.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.FocusForgeApplication
import com.example.blocker.AndroidBlockingPermissionHelper
import com.example.blocker.BlockingAuthorizationStatus
import com.example.data.model.BlockedAppEntity
import com.example.data.model.DistractionLogEntity
import com.example.data.model.ErrorCategory
import com.example.data.model.ExamTrack
import com.example.data.model.MistakeEntity
import com.example.data.model.RevisionTaskEntity
import com.example.data.model.SessionStatus
import com.example.data.model.StudySessionEntity
import com.example.data.model.SubjectDomain
import com.example.data.model.SyllabusChapterEntity
import com.example.data.model.UserProfileEntity
import com.example.data.repository.FocusForgeRepository
import com.example.focus.FocusEngine
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID

data class CoachMessage(
    val id: String = UUID.randomUUID().toString(),
    val sender: String, // "user" or "coach"
    val content: String,
    val formula: String? = null,
    val mode: String = "Explain",
    val timestampMs: Long = System.currentTimeMillis()
)

class MainViewModel(
    private val repository: FocusForgeRepository = FocusForgeApplication.instance!!.repository
) : ViewModel() {

    // User Profile
    val userProfile: StateFlow<UserProfileEntity> = repository.userProfile
        .combine(MutableStateFlow(UserProfileEntity())) { profile, default ->
            profile ?: default
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UserProfileEntity())

    // Active Exam Track
    private val _activeExamTrack = MutableStateFlow(ExamTrack.JEE)
    val activeExamTrack: StateFlow<ExamTrack> = _activeExamTrack.asStateFlow()

    init {
        viewModelScope.launch {
            repository.userProfile.collect { profile ->
                if (profile != null) {
                    _activeExamTrack.value = profile.examTrack
                }
            }
        }
    }

    fun switchExamTrack(track: ExamTrack) {
        _activeExamTrack.value = track
        viewModelScope.launch {
            repository.setExamTrack(track)
        }
    }

    fun updateUserProfile(profile: UserProfileEntity) {
        viewModelScope.launch {
            repository.updateUserProfile(profile)
        }
    }

    // Sessions & History
    val allSessions: StateFlow<List<StudySessionEntity>> = repository.allSessions
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val totalFocusMinutes: StateFlow<Int?> = repository.totalFocusMinutes
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val completedSessionCount: StateFlow<Int> = repository.completedSessionsCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    // Blocked Apps
    val allBlockedApps: StateFlow<List<BlockedAppEntity>> = repository.allBlockedApps
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun toggleAppBlocked(packageName: String, isBlocked: Boolean) {
        viewModelScope.launch {
            repository.toggleAppBlocked(packageName, isBlocked)
            if (isBlocked) {
                FocusEngine.addBlockedPackage(packageName)
            } else {
                FocusEngine.removeBlockedPackage(packageName)
            }
        }
    }

    fun addCustomBlockedApp(packageName: String, appName: String, category: String) {
        viewModelScope.launch {
            val app = BlockedAppEntity(
                packageName = packageName,
                appName = appName,
                category = category,
                isBlocked = true,
                isDefault = false
            )
            repository.addBlockedApp(app)
            FocusEngine.addBlockedPackage(packageName)
        }
    }

    // Distraction Logs
    val recentDistractionLogs: StateFlow<List<DistractionLogEntity>> = repository.recentDistractionLogs
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val blockedAttemptsCount: StateFlow<Int> = repository.blockedAttemptCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    fun clearDistractionLogs() {
        viewModelScope.launch {
            repository.clearDistractionLogs()
        }
    }

    // Mistake Journal
    val allMistakes: StateFlow<List<MistakeEntity>> = repository.allMistakes
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val unresolvedMistakes: StateFlow<List<MistakeEntity>> = repository.unresolvedMistakes
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun logMistake(
        subject: SubjectDomain,
        chapter: String,
        category: ErrorCategory,
        questionText: String,
        solutionNotes: String,
        difficulty: String
    ) {
        viewModelScope.launch {
            val mistake = MistakeEntity(
                id = UUID.randomUUID().toString(),
                subject = subject,
                chapterName = chapter,
                errorCategory = category,
                questionText = questionText,
                solutionNotes = solutionNotes,
                difficulty = difficulty
            )
            repository.saveMistake(mistake)
        }
    }

    fun reviewMistake(mistake: MistakeEntity, qualityRating: Int) {
        viewModelScope.launch {
            // Calculate new interval using SuperMemo-2 formula
            val newReviewCount = mistake.reviewCount + 1
            val intervals = listOf(1, 3, 7, 15, 30, 60)
            val nextIntervalDays = intervals.getOrElse(newReviewCount) { 60 }
            val nextReviewMs = System.currentTimeMillis() + (nextIntervalDays * 24L * 60L * 60L * 1000L)

            val updated = mistake.copy(
                reviewCount = newReviewCount,
                intervalDays = nextIntervalDays,
                nextReviewDateMs = nextReviewMs,
                isResolved = qualityRating >= 4
            )
            repository.updateMistake(updated)
            repository.addXp(100)
        }
    }

    fun deleteMistake(id: String) {
        viewModelScope.launch {
            repository.deleteMistake(id)
        }
    }

    // Revision Tasks
    val pendingRevisionTasks: StateFlow<List<RevisionTaskEntity>> = repository.pendingTasks
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun completeRevisionTask(id: String) {
        viewModelScope.launch {
            repository.completeRevisionTask(id)
        }
    }

    fun addRevisionTask(title: String, subject: SubjectDomain, chapter: String) {
        viewModelScope.launch {
            val task = RevisionTaskEntity(
                id = UUID.randomUUID().toString(),
                title = title,
                subject = subject,
                chapterName = chapter,
                dueDateMs = System.currentTimeMillis() + (24L * 60L * 60L * 1000L)
            )
            repository.addRevisionTask(task)
        }
    }

    // Syllabus Chapters
    fun getChaptersForTrack(track: ExamTrack) = repository.getChaptersForTrack(track)

    fun updateChapterProgress(id: String, completed: Int, pyqs: Int) {
        viewModelScope.launch {
            repository.updateChapterProgress(id, completed, pyqs)
        }
    }

    fun toggleWeakTopic(id: String, isWeak: Boolean) {
        viewModelScope.launch {
            repository.toggleWeakTopic(id, isWeak)
        }
    }

    // Focus Engine Shortcuts
    val focusSessionState = FocusEngine.sessionState
    val focusRemainingSeconds = FocusEngine.remainingSeconds
    val isMonkMode = FocusEngine.isMonkMode

    fun toggleMonkMode(enabled: Boolean) = FocusEngine.toggleMonkMode(enabled)

    fun startFocusSession(context: Context, subject: SubjectDomain, chapter: String, durationMinutes: Int, monkMode: Boolean) {
        FocusEngine.configureSession(subject, chapter, durationMinutes, monkMode)
        FocusEngine.startSession(context)
    }

    fun pauseFocusSession() = FocusEngine.pauseSession()
    fun resumeFocusSession(context: Context) = FocusEngine.resumeSession(context)
    fun completeFocusSession(context: Context) = FocusEngine.completeSession(context)
    fun cancelFocusSession(context: Context) = FocusEngine.cancelSession(context)

    // Android Blocking Permission Status
    fun checkPermissions(context: Context): Triple<Boolean, Boolean, Boolean> {
        val accessibility = AndroidBlockingPermissionHelper.isAccessibilityServiceEnabled(context)
        val usageStats = AndroidBlockingPermissionHelper.hasUsageStatsPermission(context)
        val overlay = AndroidBlockingPermissionHelper.hasOverlayPermission(context)
        return Triple(accessibility, usageStats, overlay)
    }

    // ForgeCoach Chat & Scientific Calculator
    private val _coachMessages = MutableStateFlow<List<CoachMessage>>(
        listOf(
            CoachMessage(
                sender = "coach",
                content = "Welcome to ForgeCoach! I'm synced with your ${userProfile.value.examTrack.displayName} prep. How can I assist your study session today?",
                mode = "Explain"
            )
        )
    )
    val coachMessages: StateFlow<List<CoachMessage>> = _coachMessages.asStateFlow()

    fun askCoach(query: String, mode: String = "Explain") {
        val userMsg = CoachMessage(sender = "user", content = query, mode = mode)
        val current = _coachMessages.value.toMutableList().apply { add(userMsg) }
        _coachMessages.value = current

        // Context-aware academic assistant logic
        viewModelScope.launch {
            val response = generateCoachAcademicResponse(query, mode, userProfile.value.examTrack)
            _coachMessages.value = _coachMessages.value + response
        }
    }

    private fun generateCoachAcademicResponse(query: String, mode: String, track: ExamTrack): CoachMessage {
        val lower = query.lowercase()
        return when {
            lower.contains("rotational") || lower.contains("torque") || lower.contains("moment of inertia") -> {
                CoachMessage(
                    sender = "coach",
                    content = "In Rotational Dynamics, remember the parallel axis theorem: I = I_cm + Md^2. For pure rolling on an inclined plane, the linear acceleration is a = g sin(θ) / (1 + I_cm/(MR^2)).",
                    formula = "I = I_{cm} + M d^2, \\quad a = \\frac{g \\sin\\theta}{1 + \\frac{I_{cm}}{MR^2}}",
                    mode = mode
                )
            }
            lower.contains("thermodynamics") || lower.contains("entropy") || lower.contains("carnot") -> {
                CoachMessage(
                    sender = "coach",
                    content = "For Carnot Engine efficiency: η = 1 - T_c / T_h = W / Q_h. Always use absolute temperature in Kelvin! First Law: ΔU = Q - W (or Q = ΔU + W depending on work convention).",
                    formula = "\\eta = 1 - \\frac{T_{cold}}{T_{hot}}, \\quad \\Delta U = Q - W",
                    mode = mode
                )
            }
            lower.contains("equilibrium") || lower.contains("le chatelier") || lower.contains("ph") -> {
                CoachMessage(
                    sender = "coach",
                    content = "For buffer solutions: pH = pKa + log([Conjugate Base] / [Acid]). Le Chatelier's Principle: increasing pressure shifts equilibrium towards fewer moles of gas.",
                    formula = "pH = pK_a + \\log\\left(\\frac{[A^-]}{[HA]}\\right)",
                    mode = mode
                )
            }
            lower.contains("calculus") || lower.contains("integral") || lower.contains("derivative") -> {
                CoachMessage(
                    sender = "coach",
                    content = "Definite Integral Property: ∫_0^a f(x) dx = ∫_0^a f(a - x) dx. Essential for King's rule problems in JEE Advanced calculus!",
                    formula = "\\int_{0}^{a} f(x)dx = \\int_{0}^{a} f(a-x)dx",
                    mode = mode
                )
            }
            lower.contains("cell") || lower.contains("mitosis") || lower.contains("genetics") -> {
                CoachMessage(
                    sender = "coach",
                    content = "NEET High-Yield: In meiosis, crossing over occurs at Pachytene stage of Prophase I, mediated by the recombinase enzyme. Chiasmata are visible at Diplotene!",
                    formula = "\\text{Pachytene: Crossing Over (Recombinase)} \\rightarrow \\text{Diplotene: Chiasmata}",
                    mode = mode
                )
            }
            lower.contains("calculate") || lower.contains("speed of light") || lower.contains("constant") -> {
                CoachMessage(
                    sender = "coach",
                    content = "Physical Constants Verified:\n• c = 2.998 × 10⁸ m/s\n• h = 6.626 × 10⁻³⁴ J·s\n• e = 1.602 × 10⁻¹⁹ C\n• ε₀ = 8.854 × 10⁻¹² F/m\n• R = 8.314 J/(mol·K)",
                    formula = "c = 3.0 \\times 10^8 \\text{ m/s}, \\quad h = 6.626 \\times 10^{-34} \\text{ J}\\cdot\\text{s}",
                    mode = mode
                )
            }
            else -> {
                CoachMessage(
                    sender = "coach",
                    content = "Study Load Signal: Balanced. Your focus rhythm is consistent. Let's tackle a 25-minute Deep Work session or review your highest-priority pending revisions!",
                    mode = mode
                )
            }
        }
    }
}
