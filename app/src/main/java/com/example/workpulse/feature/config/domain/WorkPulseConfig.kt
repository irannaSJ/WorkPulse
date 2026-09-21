package com.example.workpulse.feature.config.domain

data class WorkPulseConfig(
    val configurationName: String,
    val version: Int,
    val theme: WorkPulseTheme,
    val navigation: List<NavigationItem>,
    val quickActions: List<QuickAction>,
    val home: HomeConfig,
    val features: List<Feature>
)

data class WorkPulseTheme(
    val themeMode: String,
    val primaryColor: String?,
    val secondaryColor: String?,
    val accentColor: String?,
    val backgroundColor: String?,
    val surfaceColor: String?,
    val primaryTextColor: String?,
    val secondaryTextColor: String?,
    val darkBackgroundColor: String?,
    val darkSurfaceColor: String?,
    val darkPrimaryTextColor: String?,
    val darkSecondaryTextColor: String?,
    val cornerRadius: Double
)

data class NavigationItem(
    val navigationKey: String,
    val label: String,
    val icon: String?,
    val location: String,
    val enabled: Boolean,
    val order: Int,
    val featureKey: String?,
    val permissionRequired: Boolean
)

data class QuickAction(
    val actionKey: String,
    val label: String,
    val icon: String?,
    val enabled: Boolean,
    val order: Int,
    val featureKey: String?,
    val permissionRequired: Boolean
)

data class HomeConfig(
    val sections: List<HomeSection>
)

data class HomeSection(
    val sectionKey: String,
    val title: String,
    val sectionType: String,
    val enabled: Boolean,
    val order: Int,
    val featureKey: String?,
    val permissionRequired: Boolean,
    val configuration: String?
)

data class Feature(
    val featureKey: String,
    val displayName: String,
    val enabled: Boolean,
    val order: Int,
    val configuration: String?,
    val permissionRequired: Boolean
)