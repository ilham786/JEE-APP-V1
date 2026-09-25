package com.example.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Timer
import androidx.compose.ui.graphics.vector.ImageVector

enum class Screen(val route: String, val title: String, val icon: ImageVector) {
    HOME("home", "Dashboard", Icons.Default.Home),
    FOCUS("focus", "Focus", Icons.Default.Timer),
    TRACKER("tracker", "Tracker", Icons.Default.Book),
    PLANNER("planner", "Planner", Icons.Default.DateRange),
    COACH("coach", "ForgeCoach", Icons.Default.AutoAwesome),
    PROFILE("profile", "Profile", Icons.Default.Person)
}
