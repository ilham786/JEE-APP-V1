package com.example.ui.screens

import android.widget.Toast
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.PhoneIphone
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.blocker.AndroidBlockingPermissionHelper
import com.example.data.model.ExamTrack
import com.example.ui.MainViewModel
import com.example.ui.components.GlassCard
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
fun ProfileScreen(
    viewModel: MainViewModel
) {
    val context = LocalContext.current
    val userProfile by viewModel.userProfile.collectAsState()
    val blockedApps by viewModel.allBlockedApps.collectAsState()

    var (hasAccessibility, hasUsageStats, hasOverlay) = remember { viewModel.checkPermissions(context) }
    var showAddAppDialog by remember { mutableStateOf(false) }

    val upcomingYears = listOf(2027, 2028, 2029, 2030)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(SpaceBlack)
            .padding(horizontal = 16.dp)
            .testTag("profile_screen_column")
    ) {
        item {
            Spacer(modifier = Modifier.height(16.dp))

            // Profile Header Card
            GlassCard(borderColor = BorderBright) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(56.dp)
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
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Black
                            )
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = userProfile.displayName,
                                color = Color.White,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "@${userProfile.username} • ${userProfile.email}",
                                color = TextMuted,
                                fontSize = 12.sp
                            )
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(FocusPurple.copy(alpha = 0.2f))
                                .border(1.dp, FocusPurple.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "Lvl ${userProfile.level}",
                                color = ElectricCyan,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // Target Exam Year Selector
                    Text(
                        text = "TARGET EXAMINATION YEAR",
                        color = TextSecondary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(upcomingYears) { year ->
                            val isSelected = userProfile.targetYear == year
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSelected) FocusPurple else SurfaceElevated)
                                    .border(1.dp, if (isSelected) ElectricCyan else BorderSubtle, RoundedCornerShape(8.dp))
                                    .clickable {
                                        viewModel.updateUserProfile(userProfile.copy(targetYear = year))
                                    }
                                    .padding(horizontal = 14.dp, vertical = 8.dp)
                            ) {
                                Text(
                                    text = "$year",
                                    color = if (isSelected) Color.White else TextSecondary,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Daily Study Target Goal
                    Text(
                        text = "DAILY FOCUS GOAL: ${userProfile.dailyGoalMinutes / 60} HOURS (${userProfile.dailyGoalMinutes}m)",
                        color = TextSecondary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )

                    Slider(
                        value = userProfile.dailyGoalMinutes.toFloat(),
                        onValueChange = {
                            viewModel.updateUserProfile(userProfile.copy(dailyGoalMinutes = it.toInt()))
                        },
                        valueRange = 60f..720f,
                        steps = 10,
                        colors = SliderDefaults.colors(
                            thumbColor = ElectricCyan,
                            activeTrackColor = FocusPurple,
                            inactiveTrackColor = SurfaceElevated
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Real Android Native Blocking Diagnostic Center Card
            GlassCard(
                borderColor = if (hasAccessibility) Color(0xFF22C55E).copy(alpha = 0.4f) else AlertRed.copy(alpha = 0.4f)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.PhoneAndroid,
                                contentDescription = null,
                                tint = ElectricCyan,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "ANDROID NATIVE BLOCKING SYSTEM",
                                color = TextPrimary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 1.sp
                            )
                        }

                        IconButton(
                            onClick = {
                                val p = viewModel.checkPermissions(context)
                                hasAccessibility = p.first
                                hasUsageStats = p.second
                                hasOverlay = p.third
                                Toast.makeText(context, "Permissions refreshed!", Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(Icons.Default.Refresh, contentDescription = "Refresh", tint = TextMuted)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // 1. Accessibility Service Status
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(SurfaceElevated)
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = "Accessibility Interception Service", color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            Text(
                                text = if (hasAccessibility) "Active: Intercepts blocked apps instantaneously" else "Required to detect distracting apps on foreground",
                                color = if (hasAccessibility) Color(0xFF22C55E) else AlertRed,
                                fontSize = 11.sp
                            )
                        }
                        if (!hasAccessibility) {
                            Button(
                                onClick = { AndroidBlockingPermissionHelper.openAccessibilitySettings(context) },
                                colors = ButtonDefaults.buttonColors(containerColor = FocusPurple),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("Enable", fontSize = 11.sp)
                            }
                        } else {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF22C55E), modifier = Modifier.size(20.dp))
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // 2. Usage Access Status
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(SurfaceElevated)
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = "Usage Access Stats", color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            Text(
                                text = if (hasUsageStats) "Granted: Fallback usage stats active" else "Optional secondary app usage tracking",
                                color = if (hasUsageStats) Color(0xFF22C55E) else TextMuted,
                                fontSize = 11.sp
                            )
                        }
                        if (!hasUsageStats) {
                            OutlinedButton(
                                onClick = { AndroidBlockingPermissionHelper.openUsageAccessSettings(context) },
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("Grant", fontSize = 11.sp)
                            }
                        } else {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF22C55E), modifier = Modifier.size(20.dp))
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // 3. Display Over Other Apps
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(SurfaceElevated)
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = "Display Over Other Apps (Overlay)", color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            Text(
                                text = if (hasOverlay) "Granted: Intervention screen launches over blocked apps" else "Enables native FocusForge blocking banner",
                                color = if (hasOverlay) Color(0xFF22C55E) else TextMuted,
                                fontSize = 11.sp
                            )
                        }
                        if (!hasOverlay) {
                            OutlinedButton(
                                onClick = { AndroidBlockingPermissionHelper.openOverlaySettings(context) },
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("Grant", fontSize = 11.sp)
                            }
                        } else {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF22C55E), modifier = Modifier.size(20.dp))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Blocked Apps Registry Card
            GlassCard {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "BLOCKED APPLICATIONS (${blockedApps.count { it.isBlocked }} ACTIVE)",
                            color = TextSecondary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )

                        Text(
                            text = "+ Add Package",
                            color = ElectricCyan,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .clickable { showAddAppDialog = true }
                                .padding(4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    blockedApps.take(15).forEach { app ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(text = app.appName, color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                                Text(text = app.packageName, color = TextMuted, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
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

            Spacer(modifier = Modifier.height(16.dp))

            // iOS Screen Time Architecture Card
            GlassCard {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.PhoneIphone,
                            contentDescription = null,
                            tint = ElectricCyan,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "iOS SCREEN TIME ARCHITECTURE (APPLE API)",
                            color = TextPrimary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "FocusForge on iOS utilizes Apple's official Screen Time Framework Suite (FamilyControls, ManagedSettings, DeviceActivity). Shields are applied to user-authorized application tokens without infringing on privacy.",
                        color = TextSecondary,
                        fontSize = 12.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "• Entitlement: com.apple.developer.family-controls\n• Frameworks: ManagedSettingsUI, DeviceActivity\n• Target: iOS 16.0+ ShieldConfigurationExtension",
                        color = TextMuted,
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Local Room Database & Storage Status
            GlassCard {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "LOCAL PERSISTENCE (ROOM DATABASE)",
                            color = TextSecondary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "Offline-First",
                            color = Color(0xFF22C55E),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "All study sessions, mistake journal entries, revision plans, and distraction attempt logs are persisted locally in SQLite via Room. Ready for cloud sync with Supabase PostgreSQL.",
                        color = TextMuted,
                        fontSize = 12.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedButton(
                        onClick = {
                            viewModel.clearDistractionLogs()
                            Toast.makeText(context, "Distraction logs cleared", Toast.LENGTH_SHORT).show()
                        },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = AlertRed),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Clear Distraction History")
                    }
                }
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }

    // Add Custom Blocked Package Dialog
    if (showAddAppDialog) {
        var appName by remember { mutableStateOf("") }
        var pkgName by remember { mutableStateOf("") }
        var category by remember { mutableStateOf("Social Media") }

        AlertDialog(
            onDismissRequest = { showAddAppDialog = false },
            title = { Text(text = "Add Application to Blocklist", color = Color.White, fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = appName,
                        onValueChange = { appName = it },
                        label = { Text("Display App Name (e.g. Candy Crush)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = pkgName,
                        onValueChange = { pkgName = it },
                        label = { Text("Android Package Name (e.g. com.king.candycrushsaga)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (appName.isNotBlank() && pkgName.isNotBlank()) {
                            viewModel.addCustomBlockedApp(pkgName.trim(), appName.trim(), category)
                            showAddAppDialog = false
                            Toast.makeText(context, "Added $appName to blocked list", Toast.LENGTH_SHORT).show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = FocusPurple)
                ) {
                    Text("Add App")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddAppDialog = false }) {
                    Text("Cancel", color = TextMuted)
                }
            },
            containerColor = SurfaceCard,
            shape = RoundedCornerShape(16.dp)
        )
    }
}
