package com.example.workpulse.core.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.tween

object NavTransitions {

    val tabEnter: AnimatedContentTransitionScope<*>.() -> EnterTransition = {
        fadeIn(animationSpec = tween(200))
    }

    val tabExit: AnimatedContentTransitionScope<*>.() -> ExitTransition = {
        fadeOut(animationSpec = tween(160))
    }

    val enter: AnimatedContentTransitionScope<*>.() -> EnterTransition = {

        fadeIn(
            animationSpec = tween(400)
        ) + slideInVertically(
            animationSpec = tween(400),
            initialOffsetY = { it / 10 }
        )

    }

    val exit: AnimatedContentTransitionScope<*>.() -> ExitTransition = {

        fadeOut(
            animationSpec = tween(250)
        )

    }

}
