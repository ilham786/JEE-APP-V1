package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class ExamTrack(val displayName: String, val badge: String) {
    JEE("IIT-JEE", "⚡ Main & Advanced"),
    NEET("NEET-UG", "🩺 Medical Entrance")
}

enum class SubjectDomain(val displayName: String, val code: String) {
    PHYSICS("Physics", "PHY"),
    CHEMISTRY("Chemistry", "CHEM"),
    MATHEMATICS("Mathematics", "MATH"),
    BOTANY("Botany", "BOT"),
    ZOOLOGY("Zoology", "ZOO")
}

enum class SessionStatus {
    IDLE, STARTING, ACTIVE, PAUSED, COMPLETING, COMPLETED, CANCELLED
}

enum class ErrorCategory(val displayName: String) {
    CONCEPTUAL("Conceptual Error"),
    CALCULATION("Calculation Mistake"),
    SILLY("Silly Slip"),
    TIME_PRESSURE("Time Pressure"),
    ASSERTION_REASON("Assertion-Reason"),
    NCERT_FACT("NCERT Fact Recall")
}

@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey val id: String = "local_user",
    val displayName: String = "Aspirant",
    val username: String = "ilham_aspirant",
    val email: String = "aspirant@focusforge.app",
    val examTrack: ExamTrack = ExamTrack.JEE,
    val targetYear: Int = 2027,
    val dailyGoalMinutes: Int = 360,
    val totalXp: Long = 850L,
    val level: Int = 3,
    val currentStreak: Int = 7,
    val longestStreak: Int = 14,
    val monkModeEnabled: Boolean = false,
    val whitelistOnly: Boolean = false,
    val examLockdown: Boolean = false,
    val soundEnabled: Boolean = true,
    val selectedAmbientSound: String = "Rain",
    val ambientVolume: Float = 0.6f
)

@Entity(tableName = "study_sessions")
data class StudySessionEntity(
    @PrimaryKey val id: String,
    val subject: SubjectDomain,
    val chapterName: String,
    val plannedMinutes: Int,
    val actualMinutes: Int,
    val status: SessionStatus,
    val startTimeMs: Long,
    val endTimeMs: Long? = null,
    val xpEarned: Int = 0,
    val rating: Int = 5,
    val mood: String = "Focused",
    val notes: String = "",
    val wasCompleted: Boolean = true
)

@Entity(tableName = "blocked_apps")
data class BlockedAppEntity(
    @PrimaryKey val packageName: String,
    val appName: String,
    val category: String,
    val isBlocked: Boolean = true,
    val isDefault: Boolean = false,
    val domainFallback: String = ""
)

@Entity(tableName = "distraction_logs")
data class DistractionLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val packageName: String,
    val appName: String,
    val durationSeconds: Int,
    val timestampMs: Long = System.currentTimeMillis(),
    val isBlocked: Boolean = true,
    val wasAllowed: Boolean = false,
    val context: String = "Active Focus Session"
)

@Entity(tableName = "mistake_entries")
data class MistakeEntity(
    @PrimaryKey val id: String,
    val subject: SubjectDomain,
    val chapterName: String,
    val errorCategory: ErrorCategory,
    val questionText: String,
    val solutionNotes: String,
    val difficulty: String = "Medium",
    val intervalDays: Int = 1,
    val easeFactor: Float = 2.5f,
    val reviewCount: Int = 0,
    val nextReviewDateMs: Long = System.currentTimeMillis() + (1L * 24 * 60 * 60 * 1000),
    val createdAtMs: Long = System.currentTimeMillis(),
    val isResolved: Boolean = false
)

@Entity(tableName = "revision_tasks")
data class RevisionTaskEntity(
    @PrimaryKey val id: String,
    val title: String,
    val subject: SubjectDomain,
    val chapterName: String,
    val dueDateMs: Long,
    val isCompleted: Boolean = false,
    val priority: String = "High",
    val xpReward: Int = 100
)

@Entity(tableName = "syllabus_chapters")
data class SyllabusChapterEntity(
    @PrimaryKey val id: String,
    val examTrack: ExamTrack,
    val subject: SubjectDomain,
    val chapterName: String,
    val totalTopics: Int,
    val completedTopics: Int = 0,
    val solvedPYQs: Int = 0,
    val targetPYQs: Int = 50,
    val isWeakTopic: Boolean = false,
    val weightage: String = "High",
    val formulaCount: Int = 12
)
