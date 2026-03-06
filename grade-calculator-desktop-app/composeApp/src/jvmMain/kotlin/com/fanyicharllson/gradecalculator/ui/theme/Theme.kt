package com.fanyicharllson.gradecalculator.ui.theme


import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// ── Colors ─────────────────────────────────────────────────────────────────
val BluePrimary = Color(0xFF4E9BFF)
val BlueDeep = Color(0xFF1A73E8)
val BlueLight = Color(0xFF1E2D45)
val DarkBg = Color(0xFF0F1117)
val DarkSurface = Color(0xFF1A1D27)
val DarkElevated = Color(0xFF222536)
val OffWhite = Color(0xFFE8EAED)
val GrayMid = Color(0xFF8A8FA8)
val GrayLight = Color(0xFF3A3D4A)
val AccentGreen = Color(0xFF00E676)
val AccentRed = Color(0xFFFF5252)
val White = Color(0xFFFFFFFF)

// Grade colors
val GradeA = Color(0xFF00E676)
val GradeB = Color(0xFF4E9BFF)
val GradeC = Color(0xFFFFD740)
val GradeD = Color(0xFFFF9100)
val GradeF = Color(0xFFFF5252)

private val AppColorScheme = darkColorScheme(
    primary = BluePrimary,
    onPrimary = DarkBg,
    background = DarkBg,
    onBackground = OffWhite,
    surface = DarkSurface,
    onSurface = OffWhite,
    surfaceVariant = DarkElevated,
    onSurfaceVariant = GrayMid,
    error = AccentRed,
    onError = White
)

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = AppColorScheme, content = content)
}