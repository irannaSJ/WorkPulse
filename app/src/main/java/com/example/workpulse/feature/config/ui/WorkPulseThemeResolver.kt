package com.example.workpulse.feature.config.ui

import androidx.compose.ui.graphics.Color
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import com.example.workpulse.core.ui.theme.DarkBackground
import com.example.workpulse.core.ui.theme.DarkError
import com.example.workpulse.core.ui.theme.DarkOnBackground
import com.example.workpulse.core.ui.theme.DarkOnPrimary
import com.example.workpulse.core.ui.theme.DarkOnSecondary
import com.example.workpulse.core.ui.theme.DarkOnSurface
import com.example.workpulse.core.ui.theme.DarkPrimary
import com.example.workpulse.core.ui.theme.DarkSecondary
import com.example.workpulse.core.ui.theme.DarkSurface
import com.example.workpulse.core.ui.theme.DarkTertiary
import com.example.workpulse.core.ui.theme.LightBackground
import com.example.workpulse.core.ui.theme.LightError
import com.example.workpulse.core.ui.theme.LightOnBackground
import com.example.workpulse.core.ui.theme.LightOnPrimary
import com.example.workpulse.core.ui.theme.LightOnSecondary
import com.example.workpulse.core.ui.theme.LightOnSurface
import com.example.workpulse.core.ui.theme.LightPrimary
import com.example.workpulse.core.ui.theme.LightSecondary
import com.example.workpulse.core.ui.theme.LightSurface
import com.example.workpulse.core.ui.theme.LightTertiary
import com.example.workpulse.feature.config.domain.WorkPulseTheme

fun WorkPulseTheme.toLightColorScheme(): ColorScheme {

    return lightColorScheme(
        primary = primaryColor.toColorOrFallback(LightPrimary),
        onPrimary = LightOnPrimary,

        secondary = secondaryColor.toColorOrFallback(LightSecondary),
        onSecondary = LightOnSecondary,

        tertiary = accentColor.toColorOrFallback(LightTertiary),

        background = backgroundColor.toColorOrFallback(LightBackground),
        onBackground = primaryTextColor.toColorOrFallback(LightOnBackground),

        surface = surfaceColor.toColorOrFallback(LightSurface),
        onSurface = primaryTextColor.toColorOrFallback(LightOnSurface),

        error = LightError
    )
}

fun WorkPulseTheme.toDarkColorScheme(): ColorScheme {

    return darkColorScheme(
        primary = primaryColor.toColorOrFallback(DarkPrimary),
        onPrimary = DarkOnPrimary,

        secondary = secondaryColor.toColorOrFallback(DarkSecondary),
        onSecondary = DarkOnSecondary,

        tertiary = accentColor.toColorOrFallback(DarkTertiary),

        background = darkBackgroundColor.toColorOrFallback(DarkBackground),
        onBackground = darkPrimaryTextColor.toColorOrFallback(DarkOnBackground),

        surface = darkSurfaceColor.toColorOrFallback(DarkSurface),
        onSurface = darkPrimaryTextColor.toColorOrFallback(DarkOnSurface),

        error = DarkError
    )
}

private fun String?.toColorOrFallback(
    fallback: Color
): Color {
    return try {
        if (this.isNullOrBlank()) {
            fallback
        } else {
            Color(android.graphics.Color.parseColor(this))
        }
    } catch (_: IllegalArgumentException) {
        fallback
    }
}