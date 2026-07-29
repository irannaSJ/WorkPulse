package com.example.workpulse.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.NavHostController
import com.example.workpulse.core.navigation.Screen.Splash
import com.example.workpulse.feature.home.presentation.HomeRoute
import com.example.workpulse.feature.home.presentation.history.AttendanceHistoryRoute
import com.example.workpulse.feature.leave.LeaveSummaryRoute
import com.example.workpulse.feature.leaveApplication.LeaveApplicationRoute
//import com.example.workpulse.feature.home.presentation.HomeRoute
import com.example.workpulse.feature.login.LoginRoute
import com.example.workpulse.feature.profile.ProfileRoute
//import com.example.workpulse.feature.profile.ProfileRoute
import com.example.workpulse.feature.splash.SplashRoute

@Composable
fun AppNavHost(
    navController : NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route,
        modifier = modifier
    )
    {
        composable(Screen.Splash.route) {
            SplashRoute(

                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Splash.route) {
                            inclusive = true
                        }
                    }
                },
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Splash.route) {
                            inclusive = true
                        }

                    }
                }
            )

        }

        composable(Screen.Login.route) {
            LoginRoute(
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

        composable(Screen.Home.route) {
            HomeRoute(
                onNavigateToProfile = {
                    navController.navigate(Screen.Profile.route)
                },
                onViewAllClick = {
                    navController.navigate(Screen.LeaveSummary.route)
                }
            )
        }

        composable(Screen.AttendanceHistory.route) {
            AttendanceHistoryRoute(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.LeaveApplication.route){
            LeaveApplicationRoute(
                onBackClick ={
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.Profile.route) {
            ProfileRoute(
                onBackClick = {
                    navController.popBackStack()
                },

                onLogoutSuccess = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(navController.graph.id) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                },
                onLeaveClick = {
                    navController.navigate(Screen.LeaveApplication.route)
                },
                onAttendanceHistoryClick = {
                    navController.navigate(Screen.AttendanceHistory.route)
                }
            )
        }

        composable (Screen.LeaveSummary.route) {
            LeaveSummaryRoute(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }


    }
}