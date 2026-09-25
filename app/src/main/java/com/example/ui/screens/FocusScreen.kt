package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ExamTrack
import com.example.data.model.SessionStatus
import com.example.data.model.SubjectDomain
import com.example.ui.MainViewModel
import com.example.ui.components.GlassCard
import com.example.ui.components.SubjectBadge
import com.example.ui.theme.AlertRed
import com.example.ui.theme.BorderBright
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.ElectricCyan
import com.example.ui.theme.FocusPurple
import com.example.ui.theme.SpaceBlack
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.SurfaceElevated
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun FocusScreen(
    viewModel: MainViewModel
) {
    val context = LocalContext.current
    val activeTrack by viewModel.activeExamTrack.collectAsState()
    val sessionConfig by viewModel.focusSessionState.collectAsState()
    val remainingSeconds by viewModel.focusRemainingSeconds.collectAsState()
    val isMonkMode by viewModel.isMonkMode.collectAsState()
    val blockedApps by viewModel.allBlockedApps.collectAsState()
    val recentSessions by viewModel.allSessions.collectAsState()

    val availableSubjects = if (activeTrack == ExamTrack.JEE) {
        listOf(SubjectDomain.PHYSICS, SubjectDomain.CHEMISTRY, SubjectDomain.MATHEMATICS)
    } else {
        listOf(SubjectDomain.PHYSICS, SubjectDomain.CHEMISTRY, SubjectDomain.BOTANY, SubjectDomain.ZOOLOGY)
    }

    var selectedSubject by remember { mutableStateOf(availableSubjects.first()) }
    var selectedDuration by remember { mutableIntStateOf(25) }
    var selectedChapter by remember { mutableStateOf("Kinematics in 1D & 2D") }
    var selectedSound by remember { mutableStateOf("Rain") }
    var ambientVolume by remember { mutableStateOf(0.6f) }
    var showBlockedAppsSheet by remember { mutableStateOf(false) }

    val isRunning = sessionConfig.status == SessionStatus.ACTIVE
    val isPaused = sessionConfig.status == SessionStatus.PAUSED

    val mins = remainingSeconds / 60
    val secs = remainingSeconds % 60
    val timeFormatted = String.format("%02d:%02d", mins, secs)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(SpaceBlack)
            .padding(horizontal = 16.dp)
            .testTag("focus_screen_column")
    ) {
        item {
            Spacer(modifier = Modifier.height(16.dp))

            // Main Circular Timer Card
            GlassCard(
                borderColor = if (isRunning) ElectricCyan else BorderBright,
                backgroundColor = if (isMonkMode && isRunning) Color(0xFF150D24) else SurfaceCard
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Status Badge Pill
                    val statusText = when (sessionConfig.status) {
                        SessionStatus.ACTIVE -> if (isMonkMode) "🔒 MONK MODE ACTIVE" else "● FOCUS ZONE ACTIVE"
                        SessionStatus.PAUSED -> "⏸ SESSION PAUSED"
                        SessionStatus.COMPLETED -> "✓ SESSION COMPLETED"
                        else -> "SHIELD READY"
                    }
                    val statusColor = when (sessionConfig.status) {
                        SessionStatus.ACTIVE -> if (isMonkMode) AlertRed else ElectricCyan
                        SessionStatus.PAUSED -> Color(0xFFF59E0B)
                        else -> FocusPurple
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(statusColor.copy(alpha = 0.15f))
                            .border(1.dp, statusColor.copy(alpha = 0.4f), RoundedCornerShape(20.dp))
                            .padding(horizontal = 14.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = statusText,
                            color = statusColor,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Circular Countdown Display Dial
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(220.dp)
                            .clip(CircleShape)
                            .background(SurfaceElevated)
                            .border(
                                3.dp,
                                Brush.sweepGradient(
                                    listOf(FocusPurple, ElectricCyan, FocusPurple)
                                ),
                                CircleShape
                            )
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = timeFormatted,
                                color = Color.White,
                                fontSize = 48.sp,
                                fontWeight = FontWeight.Black,
                                fontFamily = FontFamily.Monospace
                            )
                            Text(
                                text = "${selectedSubject.displayName} • $selectedChapter",
                                color = TextSecondary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Timer Control Buttons
                    if (!isRunning && !isPaused) {
                        Button(
                            onClick = {
                                viewModel.startFocusSession(
                                    context = context,
                                    subject = selectedSubject,
                                    chapter = selectedChapter,
                                    durationMinutes = selectedDuration,
                                    monkMode = isMonkMode
                                )
                                Toast.makeText(context, "Focus session started! Distracting apps are blocked.", Toast.LENGTH_SHORT).show()
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = FocusPurple,
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp)
                                .testTag("start_timer_button")
                        ) {
                            Icon(Icons.Default.PlayArrow, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Start Focus Session ($selectedDuration min)",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    } else if (isRunning) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            OutlinedButton(
                                onClick = { viewModel.pauseFocusSession() },
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                                border = ButtonDefaults.outlinedButtonBorder.copy(brush = Brush.linearGradient(listOf(BorderBright, BorderBright))),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp)
                            ) {
                                Icon(Icons.Default.Pause, contentDescription = null)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Pause")
                            }

                            Button(
                                onClick = {
                                    viewModel.completeFocusSession(context)
                                    Toast.makeText(context, "Session completed! +XP awarded!", Toast.LENGTH_SHORT).show()
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF22C55E)),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .weight(1.3f)
                                    .height(48.dp)
                            ) {
                                Icon(Icons.Default.Check, contentDescription = null)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Complete")
                            }

                            OutlinedButton(
                                onClick = { viewModel.cancelFocusSession(context) },
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = AlertRed),
                                border = ButtonDefaults.outlinedButtonBorder.copy(brush = Brush.linearGradient(listOf(AlertRed.copy(0.4f), AlertRed.copy(0.4f)))),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp)
                            ) {
                                Icon(Icons.Default.Stop, contentDescription = null)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Cancel")
                            }
                        }
                    } else { // Paused
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Button(
                                onClick = { viewModel.resumeFocusSession(context) },
                                colors = ButtonDefaults.buttonColors(containerColor = FocusPurple),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .weight(1.5f)
                                    .height(48.dp)
                            ) {
                                Icon(Icons.Default.PlayArrow, contentDescription = null)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Resume")
                            }

                            OutlinedButton(
                                onClick = { viewModel.cancelFocusSession(context) },
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = AlertRed),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp)
                            ) {
                                Text("Abandon")
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Duration Mode Selector
            Text(
                text = "SESSION DURATION",
                color = TextSecondary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(listOf(15, 25, 50, 90)) { duration ->
                    val isSelected = selectedDuration == duration
                    val label = when (duration) {
                        15 -> "15m Sprint"
                        25 -> "25m Pomodoro"
                        50 -> "50m Deep Work"
                        else -> "90m Monk Zone"
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (isSelected) FocusPurple else SurfaceElevated)
                            .border(1.dp, if (isSelected) ElectricCyan else BorderSubtle, RoundedCornerShape(10.dp))
                            .clickable(enabled = !isRunning) {
                                selectedDuration = duration
                            }
                            .padding(horizontal = 14.dp, vertical = 10.dp)
                    ) {
                        Text(
                            text = label,
                            color = if (isSelected) Color.White else TextSecondary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Subject Selector
            Text(
                text = "TARGET SUBJECT",
                color = TextSecondary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(availableSubjects) { subject ->
                    val isSelected = selectedSubject == subject
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (isSelected) SurfaceCard else SurfaceElevated)
                            .border(1.dp, if (isSelected) ElectricCyan else BorderSubtle, RoundedCornerShape(10.dp))
                            .clickable(enabled = !isRunning) {
                                selectedSubject = subject
                            }
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        SubjectBadge(subject = subject)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Monk Mode & Native Blocker Configuration Card
            GlassCard {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(if (isMonkMode) AlertRed.copy(0.2f) else FocusPurple.copy(0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = if (isMonkMode) Icons.Default.Lock else Icons.Default.Security,
                                    contentDescription = null,
                                    tint = if (isMonkMode) AlertRed else FocusPurple,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "Monk Mode (Strict Focus)",
                                    color = TextPrimary,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = if (isMonkMode) "Strict lockout with XP penalty on early exit" else "Standard focus mode",
                                    color = TextMuted,
                                    fontSize = 11.sp
                                )
                            }
                        }

                        Switch(
                            checked = isMonkMode,
                            onCheckedChange = { viewModel.toggleMonkMode(it) },
                            enabled = !isRunning,
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = AlertRed,
                                uncheckedThumbColor = TextMuted,
                                uncheckedTrackColor = SurfaceElevated
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Real Native Blocker summary row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(SurfaceElevated)
                            .clickable { showBlockedAppsSheet = !showBlockedAppsSheet }
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Block,
                                contentDescription = null,
                                tint = ElectricCyan,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Active Distraction Shield: ${blockedApps.count { it.isBlocked }} Apps Blocked",
                                color = TextPrimary,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        Text(
                            text = if (showBlockedAppsSheet) "Hide" else "Manage",
                            color = FocusPurple,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Expandable Blocked Apps List
                    AnimatedVisibility(visible = showBlockedAppsSheet) {
                        Column(modifier = Modifier.padding(top = 10.dp)) {
                            Text(
                                text = "Default 15 Distracting Apps Protected:",
                                color = TextSecondary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(bottom = 6.dp)
                            )
                            blockedApps.take(15).forEach { app ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(text = app.appName, color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                                        Text(text = app.packageName, color = TextMuted, fontSize = 10.sp)
                                    }
                                    Switch(
                                        checked = app.isBlocked,
                                        onCheckedChange = { viewModel.toggleAppBlocked(app.packageName, it) },
                                        colors = SwitchDefaults.colors(
                                            checkedThumbColor = Color.White,
                                            checkedTrackColor = FocusPurple
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Ambient Sound Synthesizer Card
            GlassCard {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Headphones,
                                contentDescription = null,
                                tint = ElectricCyan,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "AMBIENT FOCUS SOUNDS",
                                color = TextSecondary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                        }
                        Text(
                            text = selectedSound,
                            color = ElectricCyan,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("Rain", "Lofi Drops", "White Noise", "Mute").forEach { sound ->
                            val isSelected = selectedSound == sound
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSelected) FocusPurple else SurfaceElevated)
                                    .clickable { selectedSound = sound }
                                    .padding(vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = sound,
                                    color = if (isSelected) Color.White else TextSecondary,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.VolumeUp,
                            contentDescription = null,
                            tint = TextMuted,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Slider(
                            value = ambientVolume,
                            onValueChange = { ambientVolume = it },
                            colors = SliderDefaults.colors(
                                thumbColor = ElectricCyan,
                                activeTrackColor = FocusPurple,
                                inactiveTrackColor = SurfaceElevated
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Recent Sessions History Feed
            Text(
                text = "RECENT STUDY SESSIONS",
                color = TextSecondary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (recentSessions.isEmpty()) {
                GlassCard {
                    Text(
                        text = "No focus sessions recorded yet. Start your first session above to earn XP!",
                        color = TextMuted,
                        fontSize = 13.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            } else {
                recentSessions.take(5).forEach { session ->
                    GlassCard(modifier = Modifier.padding(bottom = 8.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "${session.actualMinutes}m • ${session.subject.displayName}",
                                    color = TextPrimary,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = session.chapterName,
                                    color = TextMuted,
                                    fontSize = 12.sp
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Color(0x3322C55E))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "+${session.xpEarned} XP",
                                    color = Color(0xFF22C55E),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}
