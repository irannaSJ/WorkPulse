package com.example.workpulse.core.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

/**
 * Reusable gradients for WorkPulse.
 *
 * These gradients are shared across the application to maintain
 * a consistent premium design language.
 */
object AppGradients {

    /**
     * Splash Screen Background (Light Theme)
     */
    val SplashLight = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFFFFFFF),
            Color(0xFFF4F8FF)
        )
    )

    /**
     * Splash Screen Background (Dark Theme)
     */
    val SplashDark = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF0B1220),
            Color(0xFF111827)
        )
    )

    /**
     * Primary Brand Gradient
     * Used for buttons and cards.
     */
    val Primary = Brush.horizontalGradient(
        colors = listOf(
            BrandBlue,
            BrandPurple
        )
    )

    /**
     * Accent Gradient
     * Used for highlights and progress indicators.
     */
    val Accent = Brush.horizontalGradient(
        colors = listOf(
            BrandPurple,
            BrandGold
        )
    )

    /**
     * Premium Logo Gradient
     * Used for premium banners or illustrations.
     */
    val Premium = Brush.linearGradient(
        colors = listOf(
            BrandBlue,
            BrandPurple,
            BrandGold
        )
    )

    /**
     * Success Gradient
     */
    val Success = Brush.horizontalGradient(
        colors = listOf(
            LightSuccess,
            Color(0xFF34D399)
        )
    )

    /**
     * Error Gradient
     */
    val Error = Brush.horizontalGradient(
        colors = listOf(
            LightError,
            Color(0xFFF87171)
        )
    )
}