package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.curriculum.CurriculumData
import com.example.data.model.ExamTrack
import com.example.focus.FocusEngine
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    @Test
    fun `read app_name from context matches FocusForge`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("FocusForge", appName)
    }

    @Test
    fun `default 15 blocked apps contains instagram and youtube`() {
        val blockedApps = CurriculumData.defaultBlockedApps
        assertEquals(15, blockedApps.size)
        assertTrue(blockedApps.any { it.packageName == "com.instagram.android" })
        assertTrue(blockedApps.any { it.packageName == "com.google.android.youtube" })
    }

    @Test
    fun `curriculum chapters seed properly for JEE and NEET`() {
        val jeeChapters = CurriculumData.getInitialJeeChapters()
        val neetChapters = CurriculumData.getInitialNeetChapters()
        assertTrue(jeeChapters.isNotEmpty())
        assertTrue(neetChapters.isNotEmpty())
        assertTrue(jeeChapters.all { it.examTrack == ExamTrack.JEE })
        assertTrue(neetChapters.all { it.examTrack == ExamTrack.NEET })
    }

    @Test
    fun `focus engine blocks default packages`() {
        assertTrue(FocusEngine.isPackageBlocked("com.instagram.android"))
        assertTrue(FocusEngine.isPackageBlocked("com.google.android.youtube"))
    }
}
