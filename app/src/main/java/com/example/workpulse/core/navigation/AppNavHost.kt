package com.example.workpulse.core.navigation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import com.example.workpulse.TestingScreen
import com.example.workpulse.core.ui.theme.AppElevation
import com.example.workpulse.core.ui.theme.AdaptiveLayout
import com.example.workpulse.core.ui.theme.Dimens
import com.example.workpulse.core.navigation.Screen.Splash
import com.example.workpulse.core.navigation.config.WorkPulseNavigationDestination
import com.example.workpulse.core.navigation.config.WorkPulseNavigationRegistry
import com.example.workpulse.feature.attendanceRequest.AttendanceRequestRoute
import com.example.workpulse.feature.attendanceRequestHistory.AttendanceRequestHistoryRoute
import com.example.workpulse.feature.compOffApplication.CompOffApplicationRoute
import com.example.workpulse.feature.compOffApplicationsHistory.CompOffApplicationHistoryRoute
import com.example.workpulse.feature.config.WorkPulseHomeSectionRegistry
import com.example.workpulse.feature.config.domain.HomeSection
import com.example.workpulse.feature.config.domain.NavigationItem
import com.example.workpulse.feature.config.domain.QuickAction
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
import com.example.workpulse.feature.config.domain.WorkPulseConfig

@Composable
fun AppNavHost(
    navController : NavHostController,
    configuration : WorkPulseConfig?,
    modifier: Modifier = Modifier
) {
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    val configuredBottomNavigation = configuration
        ?.navigation
        ?.filter {
            it.enabled &&
                    (
                            it.location.equals("Bottom Navigation", ignoreCase = true) ||
                                    it.location.equals("Both", ignoreCase = true)
                            )
        }
        ?.sortedBy { it.order }
        .orEmpty()

    val configuredDrawerNavigation = configuration
        ?.navigation
        ?.filter {
            it.enabled && (
                    it.location.equals("Drawer", ignoreCase = true) ||
                    it.location.equals("Both", ignoreCase = true)
                    )
        }
        ?.sortedBy { it.order }
        .orEmpty()

    val configurationHomeSections = configuration?.home?.sections.orEmpty()

    val configurationSupportedHomeSections =
        configurationHomeSections.filter { section ->
            WorkPulseHomeSectionRegistry.isSupported(section.sectionType)
        }

    val supportedHomeSections = configurationHomeSections
        ?.filter { section ->
            section.enabled &&
                    WorkPulseHomeSectionRegistry.isSupported(section.sectionType) &&
                    isHomeSectionFeatureEnabled(
                        configuration = configuration!!,
                        featureKey = section.featureKey
                    )
        }
        ?.sortedBy { it.order }
        .orEmpty()

    val effectiveHomeSections = when{
        configuration == null -> {
            defaultHomeSections()
        }
        configurationHomeSections.isEmpty() -> {
            defaultHomeSections()
        }
        configurationSupportedHomeSections.isEmpty() -> {
            defaultHomeSections()
        }
        else ->{
            supportedHomeSections
        }
    }

    val supportedQuickActions = configuration
        ?.quickActions
        ?.filter { action ->
            action.enabled &&
            WorkPulseNavigationRegistry.resolve(action.actionKey) != null &&
            isQuickActionFeatureEnabled(
                configuration = configuration,
                featureKey = action.featureKey
            )
        }
        ?.sortedBy { it.order }
        .orEmpty()



//    val effectiveQuickActions = if (supportedQuickActions.isNotEmpty()) {
//        supportedQuickActions
//    } else {
    val effectiveQuickActions = if(configuration != null){
        supportedQuickActions
    }else{
        listOf(
            QuickAction("ATTENDANCE_HISTORY", "Attendance History", null, true, 1, null, false),
            QuickAction("LEAVE_HISTORY", "Leave History", null, true, 2, null, false),
            QuickAction("COMPOFF_HISTORY", "Comp Off History", null, true, 3, null, false),
            QuickAction("LEAVE_APPLICATION", "Leave Application", null, true, 4, null, false),
            QuickAction("COMPOFF_APPLICATION", "Comp Off Application", null, true, 5, null, false)
        )
    }

    val resolvedBottomNavigation: List<Pair<String, WorkPulseNavigationDestination>> =
        configuredBottomNavigation.mapNotNull { item ->
            WorkPulseNavigationRegistry
                .resolve(item.navigationKey)
                ?.let { destination ->
                    item.label to destination
                }
        }

    val effectiveBottomNavigation:
            List<Pair<String, WorkPulseNavigationDestination>> =
        if (configuration == null) {
            listOf(
                "HOME",
                "ATTENDANCE_HISTORY",
                "LEAVE_HISTORY",
                "PROFILE",
                "ATTENDANCE_HISTORY",
                "LEAVE_HISTORY",
                "PROFILE"
            ).mapNotNull { key ->
                WorkPulseNavigationRegistry
                    .resolve(key)
                    ?.let { destination ->
                        destination.navigationKey to destination
                    }
            }
        } else {
            resolvedBottomNavigation
        }

    val showBottomNavigation =
        effectiveBottomNavigation.any {
            it.second.route == currentRoute
        }

    val WORKPULSE_CONFIG_TEST = "workpulse_config_test"

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            if (showBottomNavigation) {
                FloatingBottomNavigationBar(
                    items = effectiveBottomNavigation,
                    selectedDestination = currentBackStackEntry?.destination,
                    onDestinationSelected = navController::navigateToBottomDestination
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Splash.route,
//            startDestination = "workpulse_config_test",
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

//
            composable("workpulse_config_test") {
                TestingScreen()
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

                navigationItems = configuredDrawerNavigation,
                quickActions = effectiveQuickActions,
                onQuickActionClick = { actionKey ->
                    WorkPulseNavigationRegistry.resolve(actionKey)
                        ?.let(navController::navigateToWorkPulseDestination)
                },
                onNavigationItemClick =  {navigationKey ->
                    WorkPulseNavigationRegistry.resolve(navigationKey)
                        ?.let(navController::navigateToWorkPulseDestination)
                },
                homeSections = effectiveHomeSections,
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

private fun NavController.navigateToWorkPulseDestination(
    destination: WorkPulseNavigationDestination
) {
    if (destination.route in bottomNavigationRoutes) {
        navigateToBottomDestination(destination.route)
    } else {
        navigate(destination.route)
    }
}


//@Composable
//private fun FloatingBottomNavigationBar(
//    items: List<Pair<String, WorkPulseNavigationDestination>>,
//    selectedDestination: NavDestination?,
//    onDestinationSelected: (String) -> Unit
//) {
//    Box(
//        modifier = Modifier
//            .fillMaxWidth()
//            .navigationBarsPadding()
//            .padding(
//                start = Dimens.Space16,
//                end = Dimens.Space16,
//                bottom = Dimens.Space8
//            ),
//        contentAlignment = Alignment.Center
//    ) {
//
//        Surface(
//            modifier = Modifier
//                .fillMaxWidth()
//                .widthIn(
//                    max = AdaptiveLayout.BottomNavigationMaxWidth
//                )
//                .height(64.dp),
//            shape = RoundedCornerShape(12.dp),
//            color = MaterialTheme.colorScheme.surfaceContainer,
//            tonalElevation = 0.dp,
////            shadowElevation = 8.dp
//        ) {
//
//            Row(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .padding(horizontal = 12.dp),
//                horizontalArrangement = Arrangement.SpaceEvenly,
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//
//                items.forEach { (label, destination) ->
//
//                    FloatingNavigationItem(
//                        label = label,
//                        icon = destination.icon,
//                        route = destination.route,
//                        selectedDestination = selectedDestination,
//                        onDestinationSelected = onDestinationSelected
//                    )
//                }
//            }
//        }
//    }
//}
//@Composable
//private fun RowScope.FloatingNavigationItem(
//    label: String,
//    icon: ImageVector,
//    route: String,
//    selectedDestination: NavDestination?,
//    onDestinationSelected: (String) -> Unit
//) {
//    val selected =
//        selectedDestination?.hierarchy
//            ?.any { it.route == route } == true
//
//    Box(
//        modifier = Modifier
//            .weight(1f)
//            .fillMaxHeight()
//            .clickable {
//                onDestinationSelected(route)
//            },
//        contentAlignment = Alignment.Center
//    ) {
//
//        if (selected) {
//
//            Surface(
//                shape = RoundedCornerShape(12.dp),
//                color = MaterialTheme.colorScheme.primaryContainer
//            ) {
//
//                Row(
//                    modifier = Modifier
//                        .padding(
//                            horizontal = 18.dp,
//                            vertical = 8.dp
//                        ),
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//
//                    Icon(
//                        imageVector = icon,
//                        contentDescription = label,
//                        tint = MaterialTheme.colorScheme.primary
//                    )
//
//                    Spacer(modifier = Modifier.run { width(8.dp) })
//
//                    Text(
//                        text = label,
//                        style = MaterialTheme.typography.labelMedium,
//                        color = MaterialTheme.colorScheme.primary,
//                        maxLines = 1
//                    )
//                }
//            }
//
//        } else {
//
//            Icon(
//                imageVector = icon,
//                contentDescription = label,
//                tint = MaterialTheme.colorScheme.onSurfaceVariant
//            )
//        }
//    }
//}

@Composable
private fun FloatingBottomNavigationBar(
    items: List<Pair<String, WorkPulseNavigationDestination>>,
    selectedDestination: NavDestination?,
    onDestinationSelected: (String) -> Unit
) {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding(),
        contentAlignment = Alignment.Center
    ) {
        val isShortHeight =
            maxHeight < AdaptiveLayout.LandscapeNavigationBreakpoint

        val verticalPadding =
            if (isShortHeight) Dimens.Space4 else Dimens.Space8

        val bottomPadding =
            if (isShortHeight) Dimens.Space4 else Dimens.Space12

        Surface(
            modifier = Modifier
                .padding(
                    start = Dimens.Space16,
                    top = verticalPadding,
                    end = Dimens.Space16,
                    bottom = bottomPadding
                )
                .widthIn(max = AdaptiveLayout.BottomNavigationMaxWidth)
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

                items.forEach { (label, destination) ->

                    FloatingNavigationItem(
                        label = label,
                        icon = destination.icon,
                        route = destination.route,
                        selectedDestination = selectedDestination,
                        onDestinationSelected = onDestinationSelected,
                        alwaysShowLabel = !isShortHeight
                    )
                }
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
        onDestinationSelected: (String) -> Unit,
        alwaysShowLabel: Boolean
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
            alwaysShowLabel = alwaysShowLabel
        )
    }


private fun isQuickActionFeatureEnabled(
    configuration: WorkPulseConfig,
    featureKey : String?
): Boolean{
    if(featureKey.isNullOrBlank()){
        return true
    }
    return configuration.features.firstOrNull{it.featureKey == featureKey}?.enabled == true
}

private fun isHomeSectionFeatureEnabled(
    configuration: WorkPulseConfig,
    featureKey : String?
): Boolean{
    if (featureKey.isNullOrBlank()){
        return true
    }
    return configuration.features
        .firstOrNull{it.featureKey == featureKey}
        ?.enabled == true
}


private fun defaultHomeSections() : List<HomeSection>{
    return (
            listOf(
                HomeSection(
                    sectionKey = "DATE_TIME",
                    title = "Date & Time",
                    sectionType = WorkPulseHomeSectionRegistry.DATE_TIME,
                    enabled = true,
                    order = 1,
                    featureKey = null,
                    permissionRequired = false,
                    configuration = null
                ),
                HomeSection(
                    sectionKey = "ATTENDANCE",
                    title = "Attendance",
                    sectionType = WorkPulseHomeSectionRegistry.ATTENDANCE,
                    enabled = true,
                    order = 2,
                    featureKey = null,
                    permissionRequired = false,
                    configuration = null
                ),
                HomeSection(
                    sectionKey = "QUICK_ACTIONS",
                    title = "Quick Actions",
                    sectionType = WorkPulseHomeSectionRegistry.QUICK_ACTIONS,
                    enabled = true,
                    order = 3,
                    featureKey = null,
                    permissionRequired = false,
                    configuration = null
                ),
                HomeSection(
                    sectionKey = "LEAVE_SUMMARY",
                    title = "Leave Summary",
                    sectionType = WorkPulseHomeSectionRegistry.LEAVE_SUMMARY,
                    enabled = true,
                    order = 4,
                    featureKey = null,
                    permissionRequired = false,
                    configuration = null
                )
            )
            )
}
