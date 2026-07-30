package com.example.workpulse.core.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

/**
 * -----------------------------
 * Light Color Scheme
 * -----------------------------
 */

private val LightColors = lightColorScheme(

    primary = LightPrimary,
    onPrimary = LightOnPrimary,

    secondary = LightSecondary,
    onSecondary = LightOnSecondary,

    tertiary = LightTertiary,

    background = LightBackground,
    onBackground = LightOnBackground,

    surface = LightSurface,
    onSurface = LightOnSurface,

    error = LightError,
)

/**
 * -----------------------------
 * Dark Color Scheme
 * -----------------------------
 */

private val DarkColors = darkColorScheme(

    primary = DarkPrimary,
    onPrimary = DarkOnPrimary,

    secondary = DarkSecondary,
    onSecondary = DarkOnSecondary,

    tertiary = DarkTertiary,

    background = DarkBackground,
    onBackground = DarkOnBackground,

    surface = DarkSurface,
    onSurface = DarkOnSurface,

    error = DarkError,
)

/**
 * -----------------------------
 * WorkPulse Theme
 * -----------------------------
 */

@Composable
fun WorkPulseTheme(

    darkTheme: Boolean = isSystemInDarkTheme(),

    content: @Composable () -> Unit

) {

    val colorScheme = if (darkTheme) {
        DarkColors
    } else {
        LightColors
    }

    MaterialTheme(

        colorScheme = colorScheme,

        typography = WorkPulseTypography,

        shapes = WorkPulseShapes,

        content = content

    )
}