package com.lihan.smartstep.core.presentation.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

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