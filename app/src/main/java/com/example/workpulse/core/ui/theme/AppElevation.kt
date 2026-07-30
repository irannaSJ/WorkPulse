package com.example.workpulse.core.ui.theme

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Semantic elevations used throughout WorkPulse.
 *
 * Avoid hardcoding elevation values.
 * Always use these predefined elevations.
 */
object AppElevation {

    /**
     * No elevation
     */
    val None: Dp = 0.dp

    /**
     * Small cards and chips
     */
    val Small: Dp = 2.dp

    /**
     * Standard Cards
     */
    val Card: Dp = 4.dp

    /**
     * Elevated Cards
     */
    val Medium: Dp = 8.dp

    /**
     * Dialogs
     */
    val Dialog: Dp = 12.dp

    /**
     * Floating Action Button
     */
    val Fab: Dp = 6.dp

    /**
     * Navigation Drawer
     */
    val Drawer: Dp = 16.dp

    /**
     * Bottom Sheet
     */
    val BottomSheet: Dp = 16.dp
}