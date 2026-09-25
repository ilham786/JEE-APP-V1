package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ExamTrack
import com.example.data.model.SubjectDomain
import com.example.data.model.SyllabusChapterEntity
import com.example.ui.MainViewModel
import com.example.ui.components.GlassCard
import com.example.ui.theme.AlertRed
import com.example.ui.theme.BiologyTeal
import com.example.ui.theme.BorderBright
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.BotanyGreen
import com.example.ui.theme.ChemistryGreen
import com.example.ui.theme.ElectricCyan
import com.example.ui.theme.FocusPurple
import com.example.ui.theme.MathsAmber
import com.example.ui.theme.PhysicsCyan
import com.example.ui.theme.SpaceBlack
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.SurfaceElevated
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.ZoologyPurple

@Composable
fun TrackerScreen(
    viewModel: MainViewModel
) {
    val activeTrack by viewModel.activeExamTrack.collectAsState()
    val allChapters by viewModel.getChaptersForTrack(activeTrack).collectAsState(initial = emptyList())

    val subjects = if (activeTrack == ExamTrack.JEE) {
        listOf(SubjectDomain.PHYSICS, SubjectDomain.CHEMISTRY, SubjectDomain.MATHEMATICS)
    } else {
        listOf(SubjectDomain.PHYSICS, SubjectDomain.CHEMISTRY, SubjectDomain.BOTANY, SubjectDomain.ZOOLOGY)
    }

    var selectedSubject by remember { mutableStateOf(subjects.first()) }
    val filteredChapters = allChapters.filter { it.subject == selectedSubject }

    val trackCompletedChapters = allChapters.count { it.completedTopics >= it.totalTopics }
    val trackTotalChapters = allChapters.size.coerceAtLeast(1)
    val trackPercentage = (trackCompletedChapters * 100) / trackTotalChapters
    val totalPYQsSolved = allChapters.sumOf { it.solvedPYQs }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(SpaceBlack)
            .padding(horizontal = 16.dp)
            .testTag("tracker_screen_column")
    ) {
        item {
            Spacer(modifier = Modifier.height(16.dp))

            // Track Switcher Bar (JEE vs NEET)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(SurfaceElevated)
                    .border(1.dp, BorderSubtle, RoundedCornerShape(12.dp))
                    .padding(4.dp)
            ) {
                listOf(ExamTrack.JEE, ExamTrack.NEET).forEach { track ->
                    val isSelected = activeTrack == track
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isSelected) FocusPurple else Color.Transparent)
                            .clickable {
                                viewModel.switchExamTrack(track)
                                selectedSubject = if (track == ExamTrack.JEE) SubjectDomain.PHYSICS else SubjectDomain.BOTANY
                            }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${track.badge} ${track.displayName}",
                            color = if (isSelected) Color.White else TextMuted,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Overall Progress Summary Card
            GlassCard(
                borderColor = BorderBright
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${activeTrack.displayName} PREPARATION OVERVIEW",
                            color = TextSecondary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "$trackPercentage% Syllabus Complete",
                            color = ElectricCyan,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    LinearProgressIndicator(
                        progress = { (trackPercentage / 100f).coerceIn(0.02f, 1f) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = ElectricCyan,
                        trackColor = SurfaceElevated
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "📚 $trackCompletedChapters of $trackTotalChapters Chapters Mastered",
                            color = TextPrimary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "🎯 $totalPYQsSolved PYQs Solved",
                            color = MathsAmber,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Subject Filter Tabs
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(subjects) { subject ->
                    val isSelected = selectedSubject == subject
                    val color = when (subject) {
                        SubjectDomain.PHYSICS -> PhysicsCyan
                        SubjectDomain.CHEMISTRY -> ChemistryGreen
                        SubjectDomain.MATHEMATICS -> MathsAmber
                        SubjectDomain.BOTANY -> BotanyGreen
                        SubjectDomain.ZOOLOGY -> ZoologyPurple
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (isSelected) color.copy(alpha = 0.2f) else SurfaceElevated)
                            .border(
                                1.dp,
                                if (isSelected) color else BorderSubtle,
                                RoundedCornerShape(10.dp)
                            )
                            .clickable { selectedSubject = subject }
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = subject.displayName,
                            color = if (isSelected) color else TextSecondary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Chapters List Header
            Text(
                text = "${selectedSubject.displayName.uppercase()} CHAPTERS (${filteredChapters.size})",
                color = TextSecondary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(8.dp))
        }

        // Chapters List
        items(filteredChapters) { chapter ->
            val isCompleted = chapter.completedTopics >= chapter.totalTopics
            val chapterPercent = if (chapter.totalTopics > 0) (chapter.completedTopics * 100) / chapter.totalTopics else 0

            GlassCard(
                modifier = Modifier.padding(bottom = 10.dp),
                borderColor = if (chapter.isWeakTopic) AlertRed.copy(0.4f) else BorderSubtle
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = chapter.chapterName,
                                    color = TextPrimary,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                if (chapter.isWeakTopic) {
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(AlertRed.copy(0.2f))
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text(text = "Weak Topic", color = AlertRed, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                            Text(
                                text = "Weightage: ${chapter.weightage} • ${chapter.formulaCount} Key Formulas",
                                color = TextMuted,
                                fontSize = 11.sp
                            )
                        }

                        // Star Bookmark
                        IconButton(
                            onClick = { viewModel.toggleWeakTopic(chapter.id, !chapter.isWeakTopic) },
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                imageVector = if (chapter.isWeakTopic) Icons.Default.Star else Icons.Default.StarBorder,
                                contentDescription = "Weak Topic Bookmark",
                                tint = if (chapter.isWeakTopic) MathsAmber else TextMuted
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Progress Bar
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        LinearProgressIndicator(
                            progress = { (chapterPercent / 100f).coerceIn(0f, 1f) },
                            modifier = Modifier
                                .weight(1f)
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp)),
                            color = if (isCompleted) Color(0xFF22C55E) else PhysicsCyan,
                            trackColor = SurfaceElevated
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "${chapter.completedTopics}/${chapter.totalTopics} topics",
                            color = TextSecondary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Action: PYQ Increment + Mark Topics
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "PYQs: ${chapter.solvedPYQs}/${chapter.targetPYQs} solved",
                            color = MathsAmber,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(SurfaceElevated)
                                    .clickable {
                                        val newPyqs = chapter.solvedPYQs + 5
                                        viewModel.updateChapterProgress(chapter.id, chapter.completedTopics, newPyqs)
                                    }
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(text = "+5 PYQs", color = ElectricCyan, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isCompleted) Color(0x3322C55E) else SurfaceElevated)
                                    .clickable {
                                        val newCompleted = if (isCompleted) 0 else chapter.totalTopics
                                        viewModel.updateChapterProgress(chapter.id, newCompleted, chapter.solvedPYQs)
                                    }
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = if (isCompleted) "✓ Mastered" else "Complete",
                                    color = if (isCompleted) Color(0xFF22C55E) else TextPrimary,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}
