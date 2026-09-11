package com.example.workpulse.core.ui.theme

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.Dp

/**
 * Application spacing, sizes and corner radius.
 *
 * Use these values throughout the app instead of hardcoding dp values.
 */
object Dimens {

    /*--------------------------------------------------
     * Spacing
     *--------------------------------------------------*/

    val Space2 = 2.dp
    val Space4 = 4.dp
    val Space8 = 8.dp
    val Space12 = 12.dp
    val Space16 = 16.dp
    val Space20 = 20.dp
    val Space24 = 24.dp
    val Space32 = 32.dp
    val Space40 = 40.dp
    val Space48 = 48.dp
    val Space56 = 56.dp
    val Space64 = 64.dp

    /*--------------------------------------------------
     * Corner Radius
     *--------------------------------------------------*/

    val Radius8 = 8.dp
    val Radius12 = 12.dp
    val Radius16 = 16.dp
    val Radius20 = 20.dp
    val Radius24 = 24.dp
    val Radius28 = 28.dp
    val Radius32 = 32.dp

    /*--------------------------------------------------
     * Elevation
     *--------------------------------------------------*/

    val Elevation2 = 2.dp
    val Elevation4 = 4.dp
    val Elevation8 = 8.dp
    val Elevation12 = 12.dp

    /*--------------------------------------------------
     * Icon Sizes
     *--------------------------------------------------*/

    val Icon16 = 16.dp
    val Icon20 = 20.dp
    val Icon24 = 24.dp
    val Icon28 = 28.dp
    val Icon32 = 32.dp
    val Icon40 = 40.dp
    val Icon48 = 48.dp
    val Icon56 = 56.dp

    /*--------------------------------------------------
     * Button
     *--------------------------------------------------*/

    val ButtonHeight = 56.dp
    val SmallButtonHeight = 44.dp

    /*--------------------------------------------------
     * Text Fields
     *--------------------------------------------------*/

    val TextFieldHeight = 60.dp

    /*--------------------------------------------------
     * Logo Sizes
     *--------------------------------------------------*/

    val SplashLogo = 160.dp
    val LoginLogo = 120.dp
    val ToolbarLogo = 40.dp
    val ProfileAvatar = 72.dp
    val HomeLogoHeight = 42.dp

    /*--------------------------------------------------
     * Card Sizes
     *--------------------------------------------------*/

    val CardMinHeight = 140.dp
    val AttendanceCardHeight = 220.dp
    val cardCornerRadius = 28.dp
    val cardElevation = 8.dp

    /*--------------------------------------------------
     * Progress Indicators
     *--------------------------------------------------*/

    val ProgressHeight = 4.dp
    val ProgressWidth = 140.dp

    /*--------------------------------------------------
     * Dividers
     *--------------------------------------------------*/

    val DividerThickness = 1.dp
}

/** Window and content constraints used by adaptive layouts. */
object AdaptiveLayout {
    val MediumBreakpoint: Dp = 600.dp
    val ExpandedBreakpoint: Dp = 840.dp
    val DateTimeStackBreakpoint: Dp = 360.dp
    val LandscapeNavigationBreakpoint: Dp = 480.dp
    val HomeContentMaxWidth: Dp = 1120.dp
    val HomeColumnMaxWidth: Dp = 560.dp
    val DrawerWidth: Dp = 320.dp
    val BottomNavigationMaxWidth: Dp = 640.dp
    val HistoryContentMaxWidth: Dp = 960.dp
    val SplashFeatureMaxWidth: Dp = 360.dp
    val SplashFeatureIcon: Dp = 80.dp
    val SplashCircleLarge: Dp = 260.dp
    val SplashCircleMedium: Dp = 220.dp
    val SplashCircleSmall: Dp = 180.dp
}
