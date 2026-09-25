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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ErrorCategory
import com.example.data.model.ExamTrack
import com.example.data.model.MistakeEntity
import com.example.data.model.SubjectDomain
import com.example.ui.MainViewModel
import com.example.ui.components.GlassCard
import com.example.ui.components.SubjectBadge
import com.example.ui.theme.AlertRed
import com.example.ui.theme.BorderBright
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.ElectricCyan
import com.example.ui.theme.FocusPurple
import com.example.ui.theme.MathsAmber
import com.example.ui.theme.SpaceBlack
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.SurfaceElevated
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun PlannerScreen(
    viewModel: MainViewModel
) {
    var activeTab by remember { mutableStateOf(0) } // 0 = Spaced Revisions, 1 = Mistake Journal
    val pendingRevisions by viewModel.pendingRevisionTasks.collectAsState()
    val mistakes by viewModel.allMistakes.collectAsState()
    val activeTrack by viewModel.activeExamTrack.collectAsState()

    var showAddMistakeDialog by remember { mutableStateOf(false) }

    val availableSubjects = if (activeTrack == ExamTrack.JEE) {
        listOf(SubjectDomain.PHYSICS, SubjectDomain.CHEMISTRY, SubjectDomain.MATHEMATICS)
    } else {
        listOf(SubjectDomain.PHYSICS, SubjectDomain.CHEMISTRY, SubjectDomain.BOTANY, SubjectDomain.ZOOLOGY)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SpaceBlack)
            .testTag("planner_screen_container")
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(16.dp))

                // Top Tab Selector (Spaced Revisions vs Mistake Journal)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(SurfaceElevated)
                        .border(1.dp, BorderSubtle, RoundedCornerShape(12.dp))
                        .padding(4.dp)
                ) {
                    listOf("Spaced Revisions (${pendingRevisions.size})", "Mistake Journal (${mistakes.size})").forEachIndexed { idx, label ->
                        val isSelected = activeTab == idx
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) FocusPurple else Color.Transparent)
                                .clickable { activeTab = idx }
                                .padding(vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = label,
                                color = if (isSelected) Color.White else TextMuted,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            if (activeTab == 0) {
                // Tab 0: Spaced Revisions Queue
                item {
                    GlassCard(borderColor = BorderBright) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "SUPERMEMO-2 REVISION ENGINE",
                                    color = ElectricCyan,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Black,
                                    letterSpacing = 1.sp
                                )
                                Text(
                                    text = "+100 XP per review",
                                    color = Color(0xFF22C55E),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Concepts scheduled at expanding intervals (1d, 3d, 7d, 15d, 30d) before neural memory decay occurs.",
                                color = TextSecondary,
                                fontSize = 12.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "QUEUED FOR TODAY (${pendingRevisions.size})",
                        color = TextSecondary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                }

                if (pendingRevisions.isEmpty()) {
                    item {
                        GlassCard {
                            Text(
                                text = "✨ Zero overdue revisions! All spaced review targets are fully up to date.",
                                color = TextMuted,
                                fontSize = 13.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                } else {
                    items(pendingRevisions) { task ->
                        GlassCard(modifier = Modifier.padding(bottom = 10.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        SubjectBadge(subject = task.subject)
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = task.chapterName,
                                            color = TextMuted,
                                            fontSize = 11.sp
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = task.title,
                                        color = TextPrimary,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                Button(
                                    onClick = { viewModel.completeRevisionTask(task.id) },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF22C55E)),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Done", fontSize = 12.sp)
                                }
                            }
                        }
                    }
                }
            } else {
                // Tab 1: Mistake Journal
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "MISTAKE ENTRIES (${mistakes.size})",
                            color = TextSecondary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )

                        Text(
                            text = "+ Log Mistake",
                            color = FocusPurple,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .clickable { showAddMistakeDialog = true }
                                .padding(4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                }

                if (mistakes.isEmpty()) {
                    item {
                        GlassCard {
                            Text(
                                text = "No mistake logs recorded yet. Tap '+ Log Mistake' below when you make an error in practice to schedule SM-2 revisions!",
                                color = TextMuted,
                                fontSize = 13.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                } else {
                    items(mistakes) { mistake ->
                        GlassCard(modifier = Modifier.padding(bottom = 10.dp)) {
                            Column {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        SubjectBadge(subject = mistake.subject)
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(4.dp))
                                                .background(SurfaceElevated)
                                                .padding(horizontal = 6.dp, vertical = 2.dp)
                                        ) {
                                            Text(
                                                text = mistake.errorCategory.displayName,
                                                color = AlertRed,
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }

                                    IconButton(
                                        onClick = { viewModel.deleteMistake(mistake.id) },
                                        modifier = Modifier.size(28.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "Delete Mistake",
                                            tint = TextMuted,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = mistake.questionText,
                                    color = TextPrimary,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium
                                )

                                if (mistake.solutionNotes.isNotBlank()) {
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(SurfaceElevated)
                                            .padding(10.dp)
                                    ) {
                                        Text(
                                            text = "💡 Solution & Insight: ${mistake.solutionNotes}",
                                            color = ElectricCyan,
                                            fontSize = 12.sp
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "SM-2 Interval: ${mistake.intervalDays}d • Reviewed ${mistake.reviewCount} times",
                                        color = TextMuted,
                                        fontSize = 11.sp
                                    )

                                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                        listOf(1, 3, 5).forEach { rating ->
                                            Box(
                                                modifier = Modifier
                                                    .clip(RoundedCornerShape(6.dp))
                                                    .background(if (rating >= 4) Color(0x3322C55E) else FocusPurple.copy(0.2f))
                                                    .clickable { viewModel.reviewMistake(mistake, rating) }
                                                    .padding(horizontal = 8.dp, vertical = 3.dp)
                                            ) {
                                                Text(
                                                    text = if (rating >= 4) "Mastered" else "Re-quiz",
                                                    color = if (rating >= 4) Color(0xFF22C55E) else Color.White,
                                                    fontSize = 10.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }

        // Floating Action Button to Log Mistake
        FloatingActionButton(
            onClick = { showAddMistakeDialog = true },
            containerColor = FocusPurple,
            contentColor = Color.White,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp)
                .testTag("fab_add_mistake")
        ) {
            Icon(Icons.Default.Add, contentDescription = "Log New Mistake")
        }
    }

    // Add Mistake Dialog
    if (showAddMistakeDialog) {
        var subject by remember { mutableStateOf(availableSubjects.first()) }
        var chapter by remember { mutableStateOf("") }
        var category by remember { mutableStateOf(ErrorCategory.CONCEPTUAL) }
        var question by remember { mutableStateOf("") }
        var solution by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { showAddMistakeDialog = false },
            title = {
                Text(text = "Log Mistake in Journal", color = Color.White, fontWeight = FontWeight.Bold)
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    // Subject Row
                    Text(text = "Subject: ${subject.displayName}", color = ElectricCyan, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        items(availableSubjects) { s ->
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(if (subject == s) FocusPurple else SurfaceElevated)
                                    .clickable { subject = s }
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(text = s.displayName, color = Color.White, fontSize = 11.sp)
                            }
                        }
                    }

                    OutlinedTextField(
                        value = chapter,
                        onValueChange = { chapter = it },
                        label = { Text("Chapter Name") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ElectricCyan,
                            unfocusedBorderColor = BorderSubtle
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = question,
                        onValueChange = { question = it },
                        label = { Text("Question Summary / What went wrong?") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ElectricCyan,
                            unfocusedBorderColor = BorderSubtle
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = solution,
                        onValueChange = { solution = it },
                        label = { Text("Correct Concept / Key Learning") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ElectricCyan,
                            unfocusedBorderColor = BorderSubtle
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (chapter.isNotBlank() && question.isNotBlank()) {
                            viewModel.logMistake(
                                subject = subject,
                                chapter = chapter,
                                category = category,
                                questionText = question,
                                solutionNotes = solution,
                                difficulty = "Medium"
                            )
                            showAddMistakeDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = FocusPurple)
                ) {
                    Text("Save to Journal")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddMistakeDialog = false }) {
                    Text("Cancel", color = TextMuted)
                }
            },
            containerColor = SurfaceCard,
            shape = RoundedCornerShape(16.dp)
        )
    }
}
