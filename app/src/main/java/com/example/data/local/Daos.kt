package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.BlockedAppEntity
import com.example.data.model.DistractionLogEntity
import com.example.data.model.ExamTrack
import com.example.data.model.MistakeEntity
import com.example.data.model.RevisionTaskEntity
import com.example.data.model.StudySessionEntity
import com.example.data.model.SubjectDomain
import com.example.data.model.SyllabusChapterEntity
import com.example.data.model.UserProfileEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserProfileDao {
    @Query("SELECT * FROM user_profile WHERE id = :id LIMIT 1")
    fun getUserProfile(id: String = "local_user"): Flow<UserProfileEntity?>

    @Query("SELECT * FROM user_profile WHERE id = :id LIMIT 1")
    suspend fun getUserProfileOnce(id: String = "local_user"): UserProfileEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(profile: UserProfileEntity)

    @Query("UPDATE user_profile SET totalXp = totalXp + :xp, level = CAST(SQRT((totalXp + :xp)/100.0) + 1 AS INT) WHERE id = :id")
    suspend fun addXp(xp: Int, id: String = "local_user")

    @Query("UPDATE user_profile SET currentStreak = currentStreak + 1 WHERE id = :id")
    suspend fun incrementStreak(id: String = "local_user")

    @Query("UPDATE user_profile SET examTrack = :track WHERE id = :id")
    suspend fun setExamTrack(track: ExamTrack, id: String = "local_user")
}

@Dao
interface StudySessionDao {
    @Query("SELECT * FROM study_sessions ORDER BY startTimeMs DESC")
    fun getAllSessions(): Flow<List<StudySessionEntity>>

    @Query("SELECT * FROM study_sessions WHERE startTimeMs >= :sinceMs ORDER BY startTimeMs DESC")
    fun getSessionsSince(sinceMs: Long): Flow<List<StudySessionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: StudySessionEntity)

    @Query("SELECT COUNT(*) FROM study_sessions WHERE wasCompleted = 1")
    fun getCompletedSessionCount(): Flow<Int>

    @Query("SELECT SUM(actualMinutes) FROM study_sessions WHERE wasCompleted = 1")
    fun getTotalFocusMinutes(): Flow<Int?>
}

@Dao
interface BlockedAppDao {
    @Query("SELECT * FROM blocked_apps ORDER BY appName ASC")
    fun getAllBlockedApps(): Flow<List<BlockedAppEntity>>

    @Query("SELECT * FROM blocked_apps WHERE isBlocked = 1")
    suspend fun getActiveBlockedApps(): List<BlockedAppEntity>

    @Query("SELECT * FROM blocked_apps WHERE isBlocked = 1")
    fun getActiveBlockedAppsFlow(): Flow<List<BlockedAppEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateApp(app: BlockedAppEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(apps: List<BlockedAppEntity>)

    @Query("UPDATE blocked_apps SET isBlocked = :isBlocked WHERE packageName = :packageName")
    suspend fun toggleBlocked(packageName: String, isBlocked: Boolean)

    @Query("DELETE FROM blocked_apps WHERE packageName = :packageName")
    suspend fun deleteApp(packageName: String)
}

@Dao
interface DistractionLogDao {
    @Query("SELECT * FROM distraction_logs ORDER BY timestampMs DESC LIMIT 100")
    fun getRecentLogs(): Flow<List<DistractionLogEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLog(log: DistractionLogEntity)

    @Query("DELETE FROM distraction_logs")
    suspend fun clearAll()

    @Query("SELECT COUNT(*) FROM distraction_logs WHERE isBlocked = 1")
    fun getBlockedAttemptCount(): Flow<Int>
}

@Dao
interface MistakeDao {
    @Query("SELECT * FROM mistake_entries ORDER BY createdAtMs DESC")
    fun getAllMistakes(): Flow<List<MistakeEntity>>

    @Query("SELECT * FROM mistake_entries WHERE isResolved = 0 ORDER BY nextReviewDateMs ASC")
    fun getUnresolvedMistakes(): Flow<List<MistakeEntity>>

    @Query("SELECT * FROM mistake_entries WHERE subject = :subject ORDER BY createdAtMs DESC")
    fun getMistakesBySubject(subject: SubjectDomain): Flow<List<MistakeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMistake(mistake: MistakeEntity)

    @Update
    suspend fun updateMistake(mistake: MistakeEntity)

    @Query("DELETE FROM mistake_entries WHERE id = :id")
    suspend fun deleteMistake(id: String)
}

@Dao
interface RevisionDao {
    @Query("SELECT * FROM revision_tasks ORDER BY dueDateMs ASC")
    fun getAllTasks(): Flow<List<RevisionTaskEntity>>

    @Query("SELECT * FROM revision_tasks WHERE isCompleted = 0 ORDER BY dueDateMs ASC")
    fun getPendingTasks(): Flow<List<RevisionTaskEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: RevisionTaskEntity)

    @Query("UPDATE revision_tasks SET isCompleted = 1 WHERE id = :id")
    suspend fun completeTask(id: String)

    @Query("DELETE FROM revision_tasks WHERE id = :id")
    suspend fun deleteTask(id: String)
}

@Dao
interface SyllabusDao {
    @Query("SELECT * FROM syllabus_chapters WHERE examTrack = :track ORDER BY subject ASC, chapterName ASC")
    fun getChaptersForTrack(track: ExamTrack): Flow<List<SyllabusChapterEntity>>

    @Query("SELECT * FROM syllabus_chapters WHERE examTrack = :track AND subject = :subject ORDER BY chapterName ASC")
    fun getChaptersForSubject(track: ExamTrack, subject: SubjectDomain): Flow<List<SyllabusChapterEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(chapters: List<SyllabusChapterEntity>)

    @Query("UPDATE syllabus_chapters SET completedTopics = :completed, solvedPYQs = :pyqs WHERE id = :id")
    suspend fun updateProgress(id: String, completed: Int, pyqs: Int)

    @Query("UPDATE syllabus_chapters SET isWeakTopic = :isWeak WHERE id = :id")
    suspend fun toggleWeakTopic(id: String, isWeak: Boolean)
}
