package com.example.workpulse.core.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes

/**
 * Shapes used throughout the WorkPulse application.
 *
 * Always use these predefined shapes instead of creating
 * RoundedCornerShape() directly in composables.
 */
val WorkPulseShapes = Shapes(

    /**
     * Small components
     * Chips, Badges, Small Buttons
     */
    small = RoundedCornerShape(Dimens.Radius12),

    /**
     * Medium components
     * TextFields, Buttons, Cards
     */
    medium = RoundedCornerShape(Dimens.Radius16),

    /**
     * Large components
     * Dialogs, Bottom Sheets, Large Cards
     */
    large = RoundedCornerShape(Dimens.Radius24),

    /**
     * Extra Large components
     * Full-screen dialogs
     */
    extraLarge = RoundedCornerShape(Dimens.Radius32)
)