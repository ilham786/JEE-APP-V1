package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
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

class FocusForgeConverters {
    @TypeConverter
    fun fromExamTrack(value: ExamTrack): String = value.name

    @TypeConverter
    fun toExamTrack(value: String): ExamTrack = try {
        ExamTrack.valueOf(value)
    } catch (_: Exception) {
        ExamTrack.JEE
    }

    @TypeConverter
    fun fromSubjectDomain(value: SubjectDomain): String = value.name

    @TypeConverter
    fun toSubjectDomain(value: String): SubjectDomain = try {
        SubjectDomain.valueOf(value)
    } catch (_: Exception) {
        SubjectDomain.PHYSICS
    }

    @TypeConverter
    fun fromSessionStatus(value: SessionStatus): String = value.name

    @TypeConverter
    fun toSessionStatus(value: String): SessionStatus = try {
        SessionStatus.valueOf(value)
    } catch (_: Exception) {
        SessionStatus.IDLE
    }

    @TypeConverter
    fun fromErrorCategory(value: ErrorCategory): String = value.name

    @TypeConverter
    fun toErrorCategory(value: String): ErrorCategory = try {
        ErrorCategory.valueOf(value)
    } catch (_: Exception) {
        ErrorCategory.CONCEPTUAL
    }
}

@Database(
    entities = [
        UserProfileEntity::class,
        StudySessionEntity::class,
        BlockedAppEntity::class,
        DistractionLogEntity::class,
        MistakeEntity::class,
        RevisionTaskEntity::class,
        SyllabusChapterEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(FocusForgeConverters::class)
abstract class FocusForgeDatabase : RoomDatabase() {
    abstract fun userProfileDao(): UserProfileDao
    abstract fun studySessionDao(): StudySessionDao
    abstract fun blockedAppDao(): BlockedAppDao
    abstract fun distractionLogDao(): DistractionLogDao
    abstract fun mistakeDao(): MistakeDao
    abstract fun revisionDao(): RevisionDao
    abstract fun syllabusDao(): SyllabusDao

    companion object {
        @Volatile
        private var INSTANCE: FocusForgeDatabase? = null

        fun getDatabase(context: Context): FocusForgeDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FocusForgeDatabase::class.java,
                    "focusforge_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
