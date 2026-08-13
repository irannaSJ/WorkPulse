package com.example.workpulse.core.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween

object NavTransitions {

    /*
     * ---------------------------------------------------------
     * BOTTOM NAVIGATION
     * ---------------------------------------------------------
     *
     * Smooth and calm.
     */

    val bottomEnter:
            AnimatedContentTransitionScope<*>.() -> EnterTransition = {

        fadeIn(
            animationSpec = tween(
                durationMillis = 520,
                easing = FastOutSlowInEasing
            )
        ) + slideInHorizontally(
            animationSpec = tween(
                durationMillis = 520,
                easing = FastOutSlowInEasing
            ),
            initialOffsetX = { it / 14 }
        )
    }

    val bottomExit:
            AnimatedContentTransitionScope<*>.() -> ExitTransition = {

        fadeOut(
            animationSpec = tween(
                durationMillis = 380,
                easing = FastOutSlowInEasing
            )
        )
    }


    /*
     * ---------------------------------------------------------
     * NORMAL FORWARD NAVIGATION
     * ---------------------------------------------------------
     */

    val enter:
            AnimatedContentTransitionScope<*>.() -> EnterTransition = {

        fadeIn(
            animationSpec = tween(
                durationMillis = 580,
                easing = FastOutSlowInEasing
            )
        ) + slideInHorizontally(
            animationSpec = tween(
                durationMillis = 580,
                easing = FastOutSlowInEasing
            ),
            initialOffsetX = { it / 10 }
        )
    }

    val exit:
            AnimatedContentTransitionScope<*>.() -> ExitTransition = {

        fadeOut(
            animationSpec = tween(
                durationMillis = 400,
                easing = FastOutSlowInEasing
            )
        ) + slideOutHorizontally(
            animationSpec = tween(
                durationMillis = 400,
                easing = FastOutSlowInEasing
            ),
            targetOffsetX = { -it / 24 }
        )
    }


    /*
     * ---------------------------------------------------------
     * BACK NAVIGATION
     * ---------------------------------------------------------
     */

    val popEnter:
            AnimatedContentTransitionScope<*>.() -> EnterTransition = {

        fadeIn(
            animationSpec = tween(
                durationMillis = 540,
                easing = FastOutSlowInEasing
            )
        ) + slideInHorizontally(
            animationSpec = tween(
                durationMillis = 540,
                easing = FastOutSlowInEasing
            ),
            initialOffsetX = { -it / 10 }
        )
    }

    val popExit:
            AnimatedContentTransitionScope<*>.() -> ExitTransition = {

        fadeOut(
            animationSpec = tween(
                durationMillis = 360,
                easing = FastOutSlowInEasing
            )
        ) + slideOutHorizontally(
            animationSpec = tween(
                durationMillis = 360,
                easing = FastOutSlowInEasing
            ),
            targetOffsetX = { it / 10 }
        )
    }


    /*
     * ---------------------------------------------------------
     * PREMIUM SCREEN
     * ---------------------------------------------------------
     */

    val premiumEnter:
            AnimatedContentTransitionScope<*>.() -> EnterTransition = {

        fadeIn(
            animationSpec = tween(
                durationMillis = 550,
                easing = FastOutSlowInEasing
            )
        ) + scaleIn(
            initialScale = 0.96f,
            animationSpec = tween(
                durationMillis = 550,
                easing = FastOutSlowInEasing
            )
        )
    }

    val premiumExit:
            AnimatedContentTransitionScope<*>.() -> ExitTransition = {

        fadeOut(
            animationSpec = tween(
                durationMillis = 350,
                easing = FastOutSlowInEasing
            )
        )
    }
}