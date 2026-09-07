package com.dareewss.myapplication.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val FitColorScheme = darkColorScheme(
    primary = FitPrimary,
    onPrimary = FitOnPrimary,
    secondary = FitSecondary,
    tertiary = FitTertiary,
    background = FitBackground,
    onBackground = FitText,
    surface = FitSurface,
    onSurface = FitText,
    surfaceVariant = FitSurfaceAlt,
    onSurfaceVariant = FitMuted,
    outline = FitBorder,
    error = FitDanger
)

@Composable
fun FitTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = FitColorScheme.copy(surfaceTint = Color.Transparent),
        typography = FitTypography,
        content = content
    )
}
