package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val FocusForgeColorScheme = darkColorScheme(
    primary = FocusPurple,
    onPrimary = Color.White,
    primaryContainer = SurfaceElevated,
    onPrimaryContainer = ElectricCyan,
    secondary = PhysicsCyan,
    onSecondary = Color.Black,
    secondaryContainer = SurfaceCard,
    onSecondaryContainer = Color.White,
    tertiary = MathsAmber,
    onTertiary = Color.Black,
    background = SpaceBlack,
    onBackground = TextPrimary,
    surface = SurfaceCard,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceElevated,
    onSurfaceVariant = TextSecondary,
    error = AlertRed,
    onError = Color.White
)

@Composable
fun FocusForgeTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = FocusForgeColorScheme,
        typography = Typography,
        content = content
    )
}

// Backward compatibility alias
@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    FocusForgeTheme(darkTheme = true, content = content)
}
