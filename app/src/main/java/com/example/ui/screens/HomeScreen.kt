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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ExamTrack
import com.example.ui.MainViewModel
import com.example.ui.components.ConsistencyHeatmap
import com.example.ui.components.GlassCard
import com.example.ui.components.StatChip
import com.example.ui.navigation.Screen
import com.example.ui.theme.BorderBright
import com.example.ui.theme.BorderSubtle
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

@Composable
fun HomeScreen(
    viewModel: MainViewModel,
    onNavigate: (Screen) -> Unit
) {
    val userProfile by viewModel.userProfile.collectAsState()
    val activeTrack by viewModel.activeExamTrack.collectAsState()
    val totalMinutes by viewModel.totalFocusMinutes.collectAsState()
    val completedSessions by viewModel.completedSessionCount.collectAsState()
    val blockedAttempts by viewModel.blockedAttemptsCount.collectAsState()
    val pendingRevisions by viewModel.pendingRevisionTasks.collectAsState()
    val chapters by viewModel.getChaptersForTrack(activeTrack).collectAsState(initial = emptyList())

    val totalChaptersCount = chapters.size.coerceAtLeast(1)
    val completedChaptersCount = chapters.count { it.completedTopics >= it.totalTopics }
    val syllabusPercent = (completedChaptersCount * 100) / totalChaptersCount

    val totalPYQsSolved = chapters.sumOf { it.solvedPYQs }

    // Sample study distribution hours for last 28 days
    val consistencyHours = listOf(
        0f, 2.5f, 3f, 4.5f, 1.5f, 5f, 6.5f,
        2f, 3.5f, 0f, 4f, 5.5f, 3f, 2.5f,
        1.5f, 4f, 5f, 2f, 6f, 3.5f, 4.5f,
        3f, 5.5f, 4f, 6.5f, 2f, 5f, 3.5f
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(SpaceBlack)
            .padding(horizontal = 16.dp)
            .testTag("home_screen_column")
    ) {
        item {
            Spacer(modifier = Modifier.height(16.dp))

            // Hero Header Card
            GlassCard(
                borderColor = BorderBright,
                backgroundColor = SurfaceCard
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(46.dp)
                                    .clip(CircleShape)
                                    .background(
                                        Brush.linearGradient(
                                            listOf(FocusPurple, ElectricCyan)
                                        )
                                    )
                            ) {
                                Text(
                                    text = userProfile.displayName.take(1).uppercase(),
                                    color = Color.White,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Black
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "Welcome back, ${userProfile.displayName}",
                                    color = TextPrimary,
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "@${userProfile.username}",
                                    color = TextMuted,
                                    fontSize = 12.sp
                                )
                            }
                        }

                        // Exam Track Badge Pill
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(FocusPurple.copy(alpha = 0.2f))
                                .border(1.dp, FocusPurple.copy(alpha = 0.5f), RoundedCornerShape(20.dp))
                                .clickable {
                                    val next = if (activeTrack == ExamTrack.JEE) ExamTrack.NEET else ExamTrack.JEE
                                    viewModel.switchExamTrack(next)
                                }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "${activeTrack.displayName} • ${userProfile.targetYear}",
                                color = ElectricCyan,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // Level and XP Bar
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "LEVEL ${userProfile.level} ASPIRANT",
                            color = FocusPurple,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "${userProfile.totalXp} Total XP",
                            color = TextSecondary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    LinearProgressIndicator(
                        progress = { ((userProfile.totalXp % 1000) / 1000f).coerceIn(0.05f, 1f) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = FocusPurple,
                        trackColor = SurfaceElevated
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Primary Focus CTA Card
            GlassCard(
                borderColor = ElectricCyan.copy(alpha = 0.4f),
                backgroundColor = Color(0xFF131129),
                onClick = { onNavigate(Screen.FOCUS) }
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = ElectricCyan,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "DEEP WORK ZONE",
                                color = ElectricCyan,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 1.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Start Focus Session",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Timer, Native Blocker & Ambient Sound",
                            color = TextSecondary,
                            fontSize = 12.sp
                        )
                    }

                    Button(
                        onClick = { onNavigate(Screen.FOCUS) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = FocusPurple,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.testTag("home_start_focus_button")
                    ) {
                        Icon(Icons.Default.PlayArrow, contentDescription = null)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Start")
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Stats Strip Grid
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                StatChip(
                    iconText = "🔥",
                    title = "Streak",
                    value = "${userProfile.currentStreak} Days",
                    modifier = Modifier.weight(1f)
                )
                StatChip(
                    iconText = "⚡",
                    title = "Focus Time",
                    value = "${totalMinutes ?: 0}m",
                    modifier = Modifier.weight(1f)
                )
                StatChip(
                    iconText = "🛡️",
                    title = "Blocked",
                    value = "$blockedAttempts",
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Consistency Heatmap Card
            GlassCard {
                ConsistencyHeatmap(
                    studyHoursList = consistencyHours,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Active Syllabus Snapshot Card
            GlassCard(
                onClick = { onNavigate(Screen.TRACKER) }
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${activeTrack.displayName} SYLLABUS SNAPSHOT",
                            color = TextSecondary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "View All",
                                color = PhysicsCyan,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.Default.ArrowForward,
                                contentDescription = null,
                                tint = PhysicsCyan,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Text(
                            text = "$syllabusPercent%",
                            color = Color.White,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Black
                        )
                        Text(
                            text = "$completedChaptersCount of $totalChaptersCount chapters covered",
                            color = TextMuted,
                            fontSize = 12.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    LinearProgressIndicator(
                        progress = { (syllabusPercent / 100f).coerceIn(0.02f, 1f) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = PhysicsCyan,
                        trackColor = SurfaceElevated
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "🎯 Solved PYQs: $totalPYQsSolved questions",
                            color = MathsAmber,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${chapters.count { it.isWeakTopic }} Weak Topics",
                            color = Color(0xFFEF4444),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Pending Spaced Revisions Preview
            GlassCard(
                onClick = { onNavigate(Screen.PLANNER) }
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "DUE REVISIONS (SPACED REPETITION)",
                            color = TextSecondary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(FocusPurple.copy(alpha = 0.2f))
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "${pendingRevisions.size} Due",
                                color = FocusPurple,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    if (pendingRevisions.isEmpty()) {
                        Text(
                            text = "✨ You are completely caught up! No due revisions today.",
                            color = TextMuted,
                            fontSize = 13.sp
                        )
                    } else {
                        pendingRevisions.take(2).forEach { task ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = task.title,
                                        color = TextPrimary,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Text(
                                        text = "${task.subject.displayName} • ${task.chapterName}",
                                        color = TextMuted,
                                        fontSize = 11.sp
                                    )
                                }
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(Color(0x3322C55E))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = "+100 XP",
                                        color = Color(0xFF22C55E),
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}
