package com.example.workpulse.core.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.EventNote
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.NavHostController
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import com.example.workpulse.core.ui.theme.AppElevation
import com.example.workpulse.core.ui.theme.Dimens
import com.example.workpulse.core.navigation.Screen.Splash
import com.example.workpulse.feature.attendanceRequest.AttendanceRequestRoute
import com.example.workpulse.feature.attendanceRequestHistory.AttendanceRequestHistoryRoute
import com.example.workpulse.feature.compOffApplication.CompOffApplicationRoute
import com.example.workpulse.feature.compOffApplicationsHistory.CompOffApplicationHistoryRoute
import com.example.workpulse.feature.faceRecognition.model.FaceRecognitionMode
import com.example.workpulse.feature.faceRecognition.presentation.FaceRecognitionScreen
import com.example.workpulse.feature.home.presentation.HomeRoute
import com.example.workpulse.feature.home.presentation.history.AttendanceHistoryRoute
import com.example.workpulse.feature.leaveApplication.LeaveApplicationRoute
import com.example.workpulse.feature.leaveHistory.LeaveHistoryRoute
//import com.example.workpulse.feature.home.presentation.HomeRoute
import com.example.workpulse.feature.login.presentation.LoginRoute
import com.example.workpulse.feature.profile.presentation.ProfileRoute
//import com.example.workpulse.feature.profile.presentation.ProfileRoute
import com.example.workpulse.feature.splash.presentation.SplashRoute

@Composable
fun AppNavHost(
    navController : NavHostController,
    modifier: Modifier = Modifier
) {
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route
    val showBottomNavigation = currentRoute in bottomNavigationRoutes

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            if (showBottomNavigation) {
                FloatingBottomNavigationBar(
                    selectedDestination = currentBackStackEntry?.destination,
                    onDestinationSelected = navController::navigateToBottomDestination
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Splash.route,
//            startDestination = "face_camera_test",
            modifier = modifier
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding),


            enterTransition = NavTransitions.enter,
            exitTransition = NavTransitions.exit,

            popEnterTransition = NavTransitions.popEnter,
            popExitTransition = NavTransitions.popExit
        ) {
        composable(Screen.Splash.route) {
            SplashRoute(
                onNavigateToFaceRegistration = {
                    navController.navigate(Screen.FaceRegistration.route) { popUpTo(Splash.route) { inclusive = true } }
                },

                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Splash.route) {
                            inclusive = true
                        }
                    }
                },
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Splash.route) {
                            inclusive = true
                        }

                    }
                }
            )

        }

        composable(
            route = Screen.Login.route,
            enterTransition = NavTransitions.premiumEnter,
            exitTransition = NavTransitions.premiumExit,
            popEnterTransition = NavTransitions.popEnter,
            popExitTransition = NavTransitions.popExit
            ) {
            LoginRoute(
                onNavigateToFaceRegistration = {
                    navController.navigate(Screen.FaceRegistration.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(Screen.FaceRegistration.route) {
            FaceRecognitionScreen(
                mode = FaceRecognitionMode.Registration,
                viewModel = hiltViewModel(),
                onRegistrationComplete = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.FaceRegistration.route) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onBack = { navController.navigate(Screen.Login.route) { popUpTo(Screen.FaceRegistration.route) { inclusive = true } } }
            )
        }

        composable(Screen.FaceVerification.route) {
            FaceRecognitionScreen(
                mode = FaceRecognitionMode.Verification,
                viewModel = hiltViewModel(),
                onVerified = {
                    navController.previousBackStackEntry?.savedStateHandle?.set("attendance_face_verified", true)
                    navController.popBackStack()
                },
                onBack = { navController.popBackStack() }
            )
        }


        //screen upgrading from here we changed the profile navigation here
        composable(
            route = Screen.Home.route,
            enterTransition = NavTransitions.bottomEnter,
            exitTransition = NavTransitions.bottomExit,
            popEnterTransition = NavTransitions.popEnter,
            popExitTransition = NavTransitions.popExit
        ) {
            val faceVerified = navController.currentBackStackEntry
                ?.savedStateHandle?.get<Boolean>("attendance_face_verified") == true
            HomeRoute(
                faceVerificationGranted = faceVerified,
                onFaceVerificationConsumed = {
                    navController.currentBackStackEntry?.savedStateHandle?.set("attendance_face_verified", false)
                },
                onFaceVerificationRequired = { navController.navigate(Screen.FaceVerification.route) },
                onFaceRegistrationRequired = {
                    navController.navigate(Screen.FaceRegistration.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onProfileClick = {
                    navController.navigateToBottomDestination(Screen.Profile.route)
                },
                onLeaveClick = {
                    navController.navigate(Screen.LeaveApplication.route)
                },
                onAttendanceHistoryClick = {
                    navController.navigateToBottomDestination(Screen.AttendanceHistory.route)
                },
                onAttendanceRequestClick = {
                    navController.navigate(Screen.AttendanceRequest.route)
                },
                onLeaveHistoryClick = {
                    navController.navigateToBottomDestination(Screen.LeaveHistory.route)
                },
                onAttendanceRequestHistoryClick = {
                    navController.navigateToBottomDestination(Screen.AttendanceRequestHistory.route )
                },

                onCompOffApplicationClick = {
                    navController.navigateToBottomDestination(Screen.CompOffApplication.route)
                },

                onLogoutSuccess = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(navController.graph.id) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            )
        }


        composable(
            route = Screen.AttendanceHistory.route,
            enterTransition = NavTransitions.bottomEnter,
            exitTransition = NavTransitions.bottomExit,
            popEnterTransition = NavTransitions.bottomEnter,
            popExitTransition = NavTransitions.bottomExit
        ) {
            AttendanceHistoryRoute(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

            composable(
                route = Screen.CompOffApplicationHistory.route,
                enterTransition = NavTransitions.bottomEnter,
                exitTransition = NavTransitions.bottomExit,
                popEnterTransition = NavTransitions.bottomEnter,
                popExitTransition = NavTransitions.bottomExit
            ){
                CompOffApplicationHistoryRoute(
                    onBackClick = {
                        navController.popBackStack()
                    }
                )

            }

        composable(
            route  = Screen.AttendanceRequest.route,
            enterTransition = NavTransitions.enter,
            exitTransition = NavTransitions.exit,
            popEnterTransition = NavTransitions.popEnter,
            popExitTransition = NavTransitions.popExit
            ) {
            AttendanceRequestRoute(
                onBackClick = {
                    navController.popBackStack()
                },
                onSaved = {
                    navController.navigate(Screen.AttendanceRequestHistory.route) {
                        popUpTo(Screen.AttendanceRequest.route) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(
            route = Screen.LeaveHistory.route,
            enterTransition = NavTransitions.bottomEnter,
            exitTransition = NavTransitions.bottomExit,
            popEnterTransition = NavTransitions.bottomEnter,
            popExitTransition = NavTransitions.bottomExit
        ) {
            LeaveHistoryRoute(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

            composable(
                route = Screen.AttendanceRequestHistory.route,
                enterTransition = NavTransitions.enter,
                exitTransition = NavTransitions.exit,
                popEnterTransition = NavTransitions.popEnter,
                popExitTransition = NavTransitions.popExit
            ) {
                AttendanceRequestHistoryRoute(
                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            }

        composable(
            route = Screen.LeaveApplication.route,
            enterTransition = NavTransitions.enter,
            exitTransition = NavTransitions.exit,
            popEnterTransition = NavTransitions.popEnter,
            popExitTransition = NavTransitions.popExit
        ){
            LeaveApplicationRoute(
                onBackClick ={
                    navController.popBackStack()
                },
                onSaved = {
                    navController.navigate(Screen.LeaveHistory.route) {
                        popUpTo(Screen.LeaveApplication.route) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }
            composable(
                route = Screen.CompOffApplication.route,
                enterTransition = NavTransitions.enter,
                exitTransition = NavTransitions.exit,
                popEnterTransition = NavTransitions.popEnter,
                popExitTransition = NavTransitions.popExit
            ) {
                CompOffApplicationRoute(
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onButtonClick = {
                        navController.navigate(Screen.CompOffApplicationHistory.route)
                    },
                    onSaved = {
                        navController.navigate(Screen.CompOffApplicationHistory.route) {
                            popUpTo(Screen.CompOffApplication.route) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                )
            }

        composable(
            route = Screen.Profile.route,
            enterTransition = NavTransitions.bottomEnter,
            exitTransition = NavTransitions.bottomExit,
            popEnterTransition = NavTransitions.bottomEnter,
            popExitTransition = NavTransitions.bottomExit
        ) {
            ProfileRoute(
                onBackClick = {
                    navController.popBackStack()
                },
                onLogoutSuccess = {
                    navController.navigate(Screen.Login.route){
                        popUpTo(navController.graph.id){
                            inclusive= true
                        }
                        launchSingleTop = true
                    }
                }
            )
        }

    }
    }
}

private val bottomNavigationRoutes = setOf(
    Screen.Home.route,
    Screen.AttendanceHistory.route,
    Screen.LeaveHistory.route,
    Screen.Profile.route
)

private fun NavController.navigateToBottomDestination(route: String) {
    if (currentDestination?.route == route) return

    navigate(route) {
        popUpTo(Screen.Home.route) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}

@Composable
private fun FloatingBottomNavigationBar(
    selectedDestination: NavDestination?,
    onDestinationSelected: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(start = Dimens.Space16, top = Dimens.Space8, end = Dimens.Space16, bottom = Dimens.Space12),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier
                .widthIn(max = 640.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(Dimens.Radius28),
            color = MaterialTheme.colorScheme.surfaceContainer,
            tonalElevation = AppElevation.Small,
            shadowElevation = AppElevation.Fab
        ) {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surfaceContainer,
                tonalElevation = AppElevation.None,
                windowInsets = WindowInsets(0, 0, 0, 0)
            ) {
                FloatingNavigationItem(
                    label = "Home",
                    icon = Icons.Outlined.Home,
                    route = Screen.Home.route,
                    selectedDestination = selectedDestination,
                    onDestinationSelected = onDestinationSelected
                )
                FloatingNavigationItem(
                    label = "Attendance",
                    icon = Icons.Outlined.History,
                    route = Screen.AttendanceHistory.route,
                    selectedDestination = selectedDestination,
                    onDestinationSelected = onDestinationSelected
                )
                FloatingNavigationItem(
                    label = "Leave",
                    icon = Icons.AutoMirrored.Outlined.EventNote,
                    route = Screen.LeaveHistory.route,
                    selectedDestination = selectedDestination,
                    onDestinationSelected = onDestinationSelected
                )
                FloatingNavigationItem(
                    label = "Profile",
                    icon = Icons.Outlined.Person,
                    route = Screen.Profile.route,
                    selectedDestination = selectedDestination,
                    onDestinationSelected = onDestinationSelected
                )
            }
        }
    }
}

@Composable
private fun RowScope.FloatingNavigationItem(
    label: String,
    icon: ImageVector,
    route: String,
    selectedDestination: NavDestination?,
    onDestinationSelected: (String) -> Unit
) {
    NavigationBarItem(
        selected = selectedDestination?.hierarchy?.any { it.route == route } == true,
        onClick = { onDestinationSelected(route) },
        icon = {
            Icon(
                imageVector = icon,
                contentDescription = label
            )
        },
        label = {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                maxLines = 1
            )
        },
        alwaysShowLabel = true
    )
}
