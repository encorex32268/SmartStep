package com.lihan.smartstep.core.presentation.ui.theme

import android.app.Activity
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = ButtonPrimary,
    onPrimary = TextWhite,
    primaryContainer = ButtonSecondary,
    onPrimaryContainer = ButtonPrimary,

    secondary = ButtonSecondary,
    onSecondary = ButtonPrimary,

    background = BackgroundMain,
    onBackground = TextPrimary,

    surface = BackgroundSecondary,
    onSurface = TextPrimary,
    surfaceVariant = BackgroundTertiary,
    onSurfaceVariant = TextSecondary,

    outline = StrokeMain,
    outlineVariant = StrokeMain
)

@Composable
fun SmartStepTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}