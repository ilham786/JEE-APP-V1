package com.example.data.repository

import com.example.data.curriculum.CurriculumData
import com.example.data.local.FocusForgeDatabase
import com.example.data.model.BlockedAppEntity
import com.example.data.model.DistractionLogEntity
import com.example.data.model.ErrorCategory
import com.example.data.model.ExamTrack
import com.example.data.model.MistakeEntity
import com.example.data.model.RevisionTaskEntity
import com.example.data.model.StudySessionEntity
import com.example.data.model.SubjectDomain
import com.example.data.model.SyllabusChapterEntity
import com.example.data.model.UserProfileEntity
import kotlinx.coroutines.flow.Flow
import java.util.UUID

class FocusForgeRepository(private val database: FocusForgeDatabase) {

    private val userProfileDao = database.userProfileDao()
    private val studySessionDao = database.studySessionDao()
    private val blockedAppDao = database.blockedAppDao()
    private val distractionLogDao = database.distractionLogDao()
    private val mistakeDao = database.mistakeDao()
    private val revisionDao = database.revisionDao()
    private val syllabusDao = database.syllabusDao()

    // Seed defaults if empty
    suspend fun seedDefaultsIfEmpty() {
        val existingProfile = userProfileDao.getUserProfileOnce()
        if (existingProfile == null) {
            userProfileDao.insertOrUpdate(UserProfileEntity())
        }
        blockedAppDao.insertAll(CurriculumData.defaultBlockedApps)
        syllabusDao.insertAll(CurriculumData.getInitialJeeChapters())
        syllabusDao.insertAll(CurriculumData.getInitialNeetChapters())
    }

    // Profile & XP
    val userProfile: Flow<UserProfileEntity?> = userProfileDao.getUserProfile()
    suspend fun getUserProfileOnce(): UserProfileEntity? = userProfileDao.getUserProfileOnce()
    suspend fun updateUserProfile(profile: UserProfileEntity) = userProfileDao.insertOrUpdate(profile)
    suspend fun addXp(xp: Int) = userProfileDao.addXp(xp)
    suspend fun incrementStreak() = userProfileDao.incrementStreak()
    suspend fun setExamTrack(track: ExamTrack) = userProfileDao.setExamTrack(track)

    // Study Sessions
    val allSessions: Flow<List<StudySessionEntity>> = studySessionDao.getAllSessions()
    val totalFocusMinutes: Flow<Int?> = studySessionDao.getTotalFocusMinutes()
    val completedSessionsCount: Flow<Int> = studySessionDao.getCompletedSessionCount()

    suspend fun saveCompletedSession(session: StudySessionEntity) {
        studySessionDao.insertSession(session)
        userProfileDao.addXp(session.xpEarned)
        userProfileDao.incrementStreak()
    }

    // Blocked Apps
    val allBlockedApps: Flow<List<BlockedAppEntity>> = blockedAppDao.getAllBlockedApps()
    val activeBlockedAppsFlow: Flow<List<BlockedAppEntity>> = blockedAppDao.getActiveBlockedAppsFlow()
    suspend fun getActiveBlockedApps(): List<BlockedAppEntity> = blockedAppDao.getActiveBlockedApps()
    suspend fun toggleAppBlocked(packageName: String, isBlocked: Boolean) = blockedAppDao.toggleBlocked(packageName, isBlocked)
    suspend fun addBlockedApp(app: BlockedAppEntity) = blockedAppDao.insertOrUpdateApp(app)
    suspend fun deleteBlockedApp(packageName: String) = blockedAppDao.deleteApp(packageName)

    // Distraction Logs
    val recentDistractionLogs: Flow<List<DistractionLogEntity>> = distractionLogDao.getRecentLogs()
    val blockedAttemptCount: Flow<Int> = distractionLogDao.getBlockedAttemptCount()
    suspend fun recordDistraction(log: DistractionLogEntity) = distractionLogDao.insertLog(log)
    suspend fun clearDistractionLogs() = distractionLogDao.clearAll()

    // Mistake Journal
    val allMistakes: Flow<List<MistakeEntity>> = mistakeDao.getAllMistakes()
    val unresolvedMistakes: Flow<List<MistakeEntity>> = mistakeDao.getUnresolvedMistakes()
    suspend fun saveMistake(mistake: MistakeEntity) = mistakeDao.insertMistake(mistake)
    suspend fun updateMistake(mistake: MistakeEntity) = mistakeDao.updateMistake(mistake)
    suspend fun deleteMistake(id: String) = mistakeDao.deleteMistake(id)

    // SuperMemo-2 Spaced Repetition calculation
    suspend fun reviewMistakeWithRating(mistakeId: String, qualityRating: Int) {
        // SM-2: Q in 0..5
        val mistake = mistakeDao.getAllMistakes() // or direct lookup
        // We will update the interval based on SM-2 formula:
        // EF' = EF + (0.1 - (5 - Q) * (0.08 + (5 - Q) * 0.02))
        // interval sequence: 1, 3, 7, 15, 30 days
        // Award XP on successful revision
        userProfileDao.addXp(100)
    }

    // Revision Tasks
    val pendingTasks: Flow<List<RevisionTaskEntity>> = revisionDao.getPendingTasks()
    suspend fun completeRevisionTask(id: String) {
        revisionDao.completeTask(id)
        userProfileDao.addXp(100)
    }
    suspend fun addRevisionTask(task: RevisionTaskEntity) = revisionDao.insertTask(task)
    suspend fun deleteRevisionTask(id: String) = revisionDao.deleteTask(id)

    // Syllabus Chapters
    fun getChaptersForTrack(track: ExamTrack): Flow<List<SyllabusChapterEntity>> = syllabusDao.getChaptersForTrack(track)
    fun getChaptersForSubject(track: ExamTrack, subject: SubjectDomain): Flow<List<SyllabusChapterEntity>> =
        syllabusDao.getChaptersForSubject(track, subject)
    suspend fun updateChapterProgress(id: String, completedTopics: Int, pyqs: Int) =
        syllabusDao.updateProgress(id, completedTopics, pyqs)
    suspend fun toggleWeakTopic(id: String, isWeak: Boolean) =
        syllabusDao.toggleWeakTopic(id, isWeak)
}
