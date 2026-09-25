package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.model.SessionStatus
import com.example.ui.MainViewModel
import com.example.ui.navigation.Screen
import com.example.ui.screens.CoachScreen
import com.example.ui.screens.FocusScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.PlannerScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.TrackerScreen
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.ElectricCyan
import com.example.ui.theme.FocusForgeTheme
import com.example.ui.theme.FocusPurple
import com.example.ui.theme.SpaceBlack
import com.example.ui.theme.SurfaceElevated
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextSecondary

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            FocusForgeTheme {
                val viewModel: MainViewModel = viewModel()
                var currentScreen by remember { mutableStateOf(Screen.HOME) }
                val sessionConfig by viewModel.focusSessionState.collectAsState()
                val isMonkMode by viewModel.isMonkMode.collectAsState()

                val isStrictFocusActive = isMonkMode && sessionConfig.status == SessionStatus.ACTIVE

                BackHandler(enabled = currentScreen != Screen.HOME && !isStrictFocusActive) {
                    currentScreen = Screen.HOME
                }

                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(SpaceBlack),
                    containerColor = SpaceBlack,
                    bottomBar = {
                        // Collapse navigation bar during Monk Mode active focus for distraction-free study
                        AnimatedVisibility(
                            visible = !isStrictFocusActive,
                            enter = fadeIn(),
                            exit = fadeOut()
                        ) {
                            FocusForgeBottomNav(
                                currentScreen = currentScreen,
                                onScreenSelected = { currentScreen = it }
                            )
                        }
                    }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        when (currentScreen) {
                            Screen.HOME -> HomeScreen(viewModel = viewModel, onNavigate = { currentScreen = it })
                            Screen.FOCUS -> FocusScreen(viewModel = viewModel)
                            Screen.TRACKER -> TrackerScreen(viewModel = viewModel)
                            Screen.PLANNER -> PlannerScreen(viewModel = viewModel)
                            Screen.COACH -> CoachScreen(viewModel = viewModel)
                            Screen.PROFILE -> ProfileScreen(viewModel = viewModel)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FocusForgeBottomNav(
    currentScreen: Screen,
    onScreenSelected: (Screen) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.navigationBars)
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(SurfaceElevated.copy(alpha = 0.95f))
            .border(1.dp, BorderSubtle, RoundedCornerShape(20.dp))
            .padding(horizontal = 4.dp, vertical = 6.dp)
            .testTag("focusforge_bottom_nav")
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Screen.entries.forEach { screen ->
                val isSelected = currentScreen == screen

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isSelected) FocusPurple.copy(alpha = 0.2f) else Color.Transparent)
                        .clickable { onScreenSelected(screen) }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                        .testTag("nav_item_${screen.route}")
                ) {
                    Icon(
                        imageVector = screen.icon,
                        contentDescription = screen.title,
                        tint = if (isSelected) ElectricCyan else TextMuted,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = screen.title,
                        color = if (isSelected) Color.White else TextMuted,
                        fontSize = 10.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }
        }
    }
}
