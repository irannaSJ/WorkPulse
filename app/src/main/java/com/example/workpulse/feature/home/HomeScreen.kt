package com.example.workpulse.feature.home.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.widthIn
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.launch
import kotlin.math.floor
import java.util.Locale

import com.example.workpulse.feature.attendance.AttendanceCard
import com.example.workpulse.feature.attendance.DateTimeCard
import com.example.workpulse.feature.config.WorkPulseHomeSectionRegistry
import com.example.workpulse.feature.config.domain.HomeSection
import com.example.workpulse.feature.config.domain.NavigationItem
import com.example.workpulse.feature.config.domain.QuickAction
import com.example.workpulse.feature.home.HomeViewModel
import com.example.workpulse.feature.home.presentation.components.HomeTopBar
import com.example.workpulse.feature.home.presentation.components.QuickActionCard
import com.example.workpulse.feature.home.presentation.components.drawerRelated.LogoutDialog
import com.example.workpulse.feature.home.presentation.components.drawerRelated.NavigationDrawerContent
import com.example.workpulse.feature.home.presentation.components.leaveRelated.LeaveSummaryCard
import com.example.workpulse.feature.leave.LeaveSummaryUiState
import com.example.workpulse.core.ui.theme.AdaptiveLayout
import com.example.workpulse.core.ui.theme.Dimens


enum class AttendanceState {
    NOT_PUNCHED_IN,
    PUNCHED_IN,
    PUNCHED_OUT
}


@Composable
fun HomeScreen(
    uiState: HomeUiState,
    onAttendanceClick: () -> Unit,
    navigationItems: List<NavigationItem>,
    onNavigationItemClick: (String) -> Unit,
    onLogoutClick: () -> Unit,
    leaveUiState: LeaveSummaryUiState,
    quickActions: List<QuickAction>,
    onQuickActionClick: (String) -> Unit,
    homeSections: List<HomeSection>
) {

    val drawerState = rememberDrawerState(
        initialValue = DrawerValue.Closed
    )

    var showLogoutDialog by rememberSaveable {
        mutableStateOf(false)
    }

    val scope = rememberCoroutineScope()


    ModalNavigationDrawer(

        drawerState = drawerState,

        drawerContent = {

            NavigationDrawerContent(
                employeeName = uiState.employeeName,
                designation = uiState.designation,
                company = uiState.company,
                navigationItems = navigationItems,
                selectedRoute = "HOME",

                onNavigationItemClick = { navigationKey ->
                    scope.launch {
                        drawerState.close()
                        onNavigationItemClick(navigationKey)
                    }
                },

                onLogoutClick = {
                    scope.launch {
                        drawerState.close()
                        showLogoutDialog = true
                    }
                }
            )
        }

    ) {

        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {

            val isWide =
                maxWidth >= AdaptiveLayout.MediumBreakpoint

            val horizontalPadding =
                if (isWide) {
                    Dimens.Space24
                } else {
                    Dimens.Space16
                }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = horizontalPadding,
                        vertical = Dimens.Space16
                    )
                    .widthIn(
                        max = AdaptiveLayout.HomeContentMaxWidth
                    )
                    .verticalScroll(
                        rememberScrollState()
                    )
            ) {

                // --------------------------------------------------
                // TOP BAR
                // --------------------------------------------------

                HomeTopBar(
                    uiState = uiState,

                    onMenuClick = {
                        scope.launch {
                            drawerState.open()
                        }
                    }
                )

                Spacer(
                    modifier = Modifier.height(Dimens.Space20)
                )


                // --------------------------------------------------
                // DYNAMIC HOME SECTIONS
                // --------------------------------------------------

                if (isWide) {

                    DynamicHomeSectionsWide(
                        sections = homeSections,
                        uiState = uiState,
                        leaveUiState = leaveUiState,
                        quickActions = quickActions,
                        onAttendanceClick = onAttendanceClick,
                        onQuickActionClick = onQuickActionClick
                    )

                } else {

                    DynamicHomeSectionsPhone(
                        sections = homeSections,
                        uiState = uiState,
                        leaveUiState = leaveUiState,
                        quickActions = quickActions,
                        onAttendanceClick = onAttendanceClick,
                        onQuickActionClick = onQuickActionClick
                    )
                }
            }



            // ------------------------------------------------------
            // LOGOUT DIALOG
            // ------------------------------------------------------

            if (showLogoutDialog) {

                LogoutDialog(

                    onDismiss = {
                        showLogoutDialog = false
                    },

                    onConfirm = {
                        showLogoutDialog = false
                        onLogoutClick()
                    }
                )
            }
        }
    }
}


// ================================================================
// PHONE HOME SECTION LAYOUT
// ================================================================

@Composable
private fun DynamicHomeSectionsPhone(
    sections: List<HomeSection>,
    uiState: HomeUiState,
    leaveUiState: LeaveSummaryUiState,
    quickActions: List<QuickAction>,
    onAttendanceClick: () -> Unit,
    onQuickActionClick: (String) -> Unit
) {

    sections.forEachIndexed { index, section ->

        HomeSectionContent(
            section = section,
            uiState = uiState,
            leaveUiState = leaveUiState,
            quickActions = quickActions,
            onAttendanceClick = onAttendanceClick,
            onQuickActionClick = onQuickActionClick
        )

        if (index < sections.lastIndex) {

            Spacer(
                modifier = Modifier.height(
                    Dimens.Space20
                )
            )
        }

    }
}


// ================================================================
// WIDE HOME SECTION LAYOUT
// ================================================================

@Composable
private fun DynamicHomeSectionsWide(
    sections: List<HomeSection>,
    uiState: HomeUiState,
    leaveUiState: LeaveSummaryUiState,
    quickActions: List<QuickAction>,
    onAttendanceClick: () -> Unit,
    onQuickActionClick: (String) -> Unit
) {

    val rows = sections.chunked(2)

    rows.forEachIndexed { rowIndex, rowSections ->

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(
                Dimens.Space20
            ),
            verticalAlignment = Alignment.Top
        ) {

            rowSections.forEach { section ->

                HomeSectionContent(
                    section = section,
                    uiState = uiState,
                    leaveUiState = leaveUiState,
                    quickActions = quickActions,
                    onAttendanceClick = onAttendanceClick,
                    onQuickActionClick = onQuickActionClick,
                    modifier = Modifier.weight(1f)
                )
            }


            // --------------------------------------------------
            // CENTER SINGLE SECTION IN LAST ROW
            // --------------------------------------------------

            if (rowSections.size == 1) {

                Spacer(
                    modifier = Modifier.weight(1f)
                )
            }
        }


        if (rowIndex < rows.lastIndex) {

            Spacer(
                modifier = Modifier.height(
                    Dimens.Space20
                )
            )
        }
    }
}


// ================================================================
// NATIVE HOME SECTION RENDERER
// ================================================================

@Composable
private fun HomeSectionContent(
    section: HomeSection,
    uiState: HomeUiState,
    leaveUiState: LeaveSummaryUiState,
    quickActions: List<QuickAction>,
    onAttendanceClick: () -> Unit,
    onQuickActionClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {

    when (
        section.sectionType
            .trim()
            .uppercase(Locale.ROOT)
    ) {

        // --------------------------------------------------------
        // DATE TIME
        // --------------------------------------------------------

        WorkPulseHomeSectionRegistry.DATE_TIME -> {

            DateTimeCard(
                modifier = modifier
            )
        }


        // --------------------------------------------------------
        // ATTENDANCE
        // --------------------------------------------------------

        WorkPulseHomeSectionRegistry.ATTENDANCE -> {

            AttendanceCard(
                modifier = modifier,

                attendanceState =
                    uiState.attendanceState,

                workingSeconds =
                    uiState.workingSeconds,

                punchInTime =
                    uiState.punchInTime,

                punchOutTime =
                    uiState.punchOutTime,

                onAttendanceClick =
                    onAttendanceClick
            )
        }


        // --------------------------------------------------------
        // QUICK ACTIONS
        // --------------------------------------------------------

        WorkPulseHomeSectionRegistry.QUICK_ACTIONS -> {

            if (quickActions.isNotEmpty()) {

                QuickActionsSection(
                    modifier = modifier,
                    title = section.title.ifBlank {
                        "Quick Actions"
                    },
                    actions = quickActions,
                    onActionClick = onQuickActionClick
                )
            }
        }


        // --------------------------------------------------------
        // LEAVE SUMMARY
        // --------------------------------------------------------

        WorkPulseHomeSectionRegistry.LEAVE_SUMMARY -> {

            LeaveSummaryCard(
                modifier = modifier,

                remainingLeaves =
                    uiState.remainingLeaves,

                leaveUiState =
                    leaveUiState
            )
        }


        // --------------------------------------------------------
        // UNKNOWN SECTION
        // --------------------------------------------------------

        else -> {
            // Unsupported section is ignored safely.
        }
    }
}


// ================================================================
// QUICK ACTIONS SECTION
// ================================================================

@Composable
private fun QuickActionsSection(
    modifier: Modifier = Modifier,
    title: String,
    actions: List<QuickAction>,
    onActionClick: (String) -> Unit
) {

    BoxWithConstraints(
        modifier = modifier.fillMaxWidth()
    ) {

        val spacing = Dimens.Space12

        val columnCount = floor(
            (maxWidth.value + spacing.value) /
                    (
                            Dimens.QuickActionMinWidth.value +
                                    spacing.value
                            )
        )
            .toInt()
            .coerceIn(1, 3)


        val cardWidth = if (actions.size == 1) {

            maxWidth

        } else {

            (
                    maxWidth -
                            spacing * (columnCount - 1)
                    ) / columnCount
        }


        Column(
            modifier = Modifier.fillMaxWidth()
        ) {

            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground
            )


            Spacer(
                modifier = Modifier.height(spacing)
            )


            val rows = actions.chunked(columnCount)

            rows.forEachIndexed { index, rowActions ->

                Row(
                    modifier = Modifier.fillMaxWidth(),

                    horizontalArrangement =
                        Arrangement.spacedBy(
                            spacing,
                            Alignment.CenterHorizontally
                        )
                ) {

                    rowActions.forEach { action ->

                        Box(
                            modifier = Modifier.width(
                                cardWidth
                            )
                        ) {

                            QuickActionCard(
                                action = action,

                                supportingText =
                                    actionSupportingText(
                                        action.actionKey
                                    ),

                                onClick = {
                                    onActionClick(
                                        action.actionKey
                                    )
                                }
                            )
                        }
                    }


                    // --------------------------------------------------
                    // CENTER SINGLE ITEM
                    // --------------------------------------------------

                    if (rowActions.size == 1 &&
                        actions.size > 1
                    ) {

                        Spacer(
                            modifier = Modifier.width(
                                cardWidth
                            )
                        )
                    }
                }


                if (index < rows.lastIndex) {

                    Spacer(
                        modifier = Modifier.height(spacing)
                    )
                }
            }
        }
    }
}


// ================================================================
// QUICK ACTION SUPPORTING TEXT
// ================================================================

private fun actionSupportingText(
    actionKey: String
): String? {

    return when (actionKey) {

        "ATTENDANCE_HISTORY" ->
            "View your records"

        "LEAVE_HISTORY" ->
            "Review leave activity"

        "COMPOFF_HISTORY" ->
            "Track comp off requests"

        "LEAVE_APPLICATION" ->
            "Submit a new request"

        else ->
            null
    }
}


// ================================================================
// WORKING TIME FORMAT
// ================================================================

fun formatWorkingTime(
    seconds: Long
): String {

    val hrs = seconds / 3600
    val mins = (seconds % 3600) / 60
    val sec = seconds % 60

    return String.format(
        "%02d : %02d : %02d",
        hrs,
        mins,
        sec
    )
}