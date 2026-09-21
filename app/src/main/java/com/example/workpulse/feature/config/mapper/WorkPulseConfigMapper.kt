package com.example.workpulse.feature.config.mapper

import com.example.workpulse.data.local.entity.configEntity.FeatureEntity
import com.example.workpulse.data.local.entity.configEntity.HomeSectionEntity
import com.example.workpulse.data.local.entity.configEntity.NavigationItemEntity
import com.example.workpulse.data.local.entity.configEntity.QuickActionEntity
import com.example.workpulse.data.local.entity.configEntity.WorkPulseConfigEntity
import com.example.workpulse.data.local.entity.configEntity.WorkPulseThemeEntity
import com.example.workpulse.data.remote.dto.response.FeatureDto
import com.example.workpulse.data.remote.dto.response.HomeConfigDto
import com.example.workpulse.data.remote.dto.response.HomeSectionDto
import com.example.workpulse.data.remote.dto.response.NavigationItemDto
import com.example.workpulse.data.remote.dto.response.QuickActionDto
import com.example.workpulse.data.remote.dto.response.WorkPulseConfigDto
import com.example.workpulse.data.remote.dto.response.WorkPulseThemeDto
import com.example.workpulse.feature.config.domain.Feature
import com.example.workpulse.feature.config.domain.HomeConfig
import com.example.workpulse.feature.config.domain.HomeSection
import com.example.workpulse.feature.config.domain.NavigationItem
import com.example.workpulse.feature.config.domain.QuickAction
import com.example.workpulse.feature.config.domain.WorkPulseConfig
import com.example.workpulse.feature.config.domain.WorkPulseTheme

fun WorkPulseConfigDto.toDomain(): WorkPulseConfig {
    return WorkPulseConfig(
        configurationName = configurationName,
        version = version,
        theme = theme.toDomain(),
        navigation = navigation
            .sortedBy { it.order }
            .map { it.toDomain() },
        quickActions = quickActions
            .sortedBy { it.order }
            .map { it.toDomain() },
        home = home.toDomain(),
        features = features
            .sortedBy { it.order }
            .map { it.toDomain() }
    )
}

private fun WorkPulseThemeDto.toDomain(): WorkPulseTheme {
    return WorkPulseTheme(
        themeMode = themeMode ?: "System",
        primaryColor = primaryColor,
        secondaryColor = secondaryColor,
        accentColor = accentColor,
        backgroundColor = backgroundColor,
        surfaceColor = surfaceColor,
        primaryTextColor = primaryTextColor,
        secondaryTextColor = secondaryTextColor,
        darkBackgroundColor = darkBackgroundColor,
        darkSurfaceColor = darkSurfaceColor,
        darkPrimaryTextColor = darkPrimaryTextColor,
        darkSecondaryTextColor = darkSecondaryTextColor,
        cornerRadius = cornerRadius ?: 0.0
    )
}

private fun NavigationItemDto.toDomain(): NavigationItem {
    return NavigationItem(
        navigationKey = navigationKey,
        label = label,
        icon = icon,
        location = location,
        enabled = enabled,
        order = order,
        featureKey = featureKey,
        permissionRequired = permissionRequired
    )
}

private fun QuickActionDto.toDomain(): QuickAction {
    return QuickAction(
        actionKey = actionKey,
        label = label,
        icon = icon,
        enabled = enabled,
        order = order,
        featureKey = featureKey,
        permissionRequired = permissionRequired
    )
}

private fun HomeConfigDto.toDomain(): HomeConfig {
    return HomeConfig(
        sections = sections
            .sortedBy { it.order }
            .map { it.toDomain() }
    )
}

private fun HomeSectionDto.toDomain(): HomeSection {
    return HomeSection(
        sectionKey = sectionKey,
        title = title,
        sectionType = sectionType,
        enabled = enabled,
        order = order,
        featureKey = featureKey,
        permissionRequired = permissionRequired,
        configuration = configuration
    )
}

private fun FeatureDto.toDomain(): Feature {
    return Feature(
        featureKey = featureKey,
        displayName = displayName,
        enabled = enabled,
        order = order,
        configuration = configuration,
        permissionRequired = permissionRequired
    )
}

fun WorkPulseConfig.toEntity(): WorkPulseConfigEntity {
    return WorkPulseConfigEntity(
        id = 1,
        configurationName = configurationName,
        version = version
    )
}

fun WorkPulseTheme.toEntity(): WorkPulseThemeEntity {
    return WorkPulseThemeEntity(
        configId = 1,
        themeMode = themeMode,
        primaryColor = primaryColor,
        secondaryColor = secondaryColor,
        accentColor = accentColor,
        backgroundColor = backgroundColor,
        surfaceColor = surfaceColor,
        primaryTextColor = primaryTextColor,
        secondaryTextColor = secondaryTextColor,
        darkBackgroundColor = darkBackgroundColor,
        darkSurfaceColor = darkSurfaceColor,
        darkPrimaryTextColor = darkPrimaryTextColor,
        darkSecondaryTextColor = darkSecondaryTextColor,
        cornerRadius = cornerRadius
    )
}

fun NavigationItem.toEntity(): NavigationItemEntity {
    return NavigationItemEntity(
        configId = 1,
        navigationKey = navigationKey,
        label = label,
        icon = icon,
        location = location,
        enabled = enabled,
        order = order,
        featureKey = featureKey,
        permissionRequired = permissionRequired
    )
}

fun QuickAction.toEntity(): QuickActionEntity {
    return QuickActionEntity(
        configId = 1,
        actionKey = actionKey,
        label = label,
        icon = icon,
        enabled = enabled,
        order = order,
        featureKey = featureKey,
        permissionRequired = permissionRequired
    )
}

fun HomeSection.toEntity(): HomeSectionEntity {
    return HomeSectionEntity(
        configId = 1,
        sectionKey = sectionKey,
        title = title,
        sectionType = sectionType,
        enabled = enabled,
        order = order,
        featureKey = featureKey,
        permissionRequired = permissionRequired,
        configuration = configuration
    )
}

fun Feature.toEntity(): FeatureEntity {
    return FeatureEntity(
        configId = 1,
        featureKey = featureKey,
        displayName = displayName,
        enabled = enabled,
        order = order,
        configuration = configuration,
        permissionRequired = permissionRequired
    )
}

fun WorkPulseConfigEntity.toDomain(
    theme: WorkPulseThemeEntity,
    navigation :List<NavigationItemEntity>,
    quickActions : List<QuickActionEntity>,
    homeSections : List<HomeSectionEntity>,
    features : List<FeatureEntity>
): WorkPulseConfig {
    return WorkPulseConfig(
        configurationName = configurationName,
        version = version,
        theme = theme.toDomain(),
        navigation = navigation
            .sortedBy { it.order }
            .map { it.toDomain() },
        quickActions = quickActions
            .sortedBy { it.order }
            .map { it.toDomain() },
        home = HomeConfig(
            sections = homeSections
                .sortedBy { it.order }
                .map { it.toDomain() }
        ),
        features = features
            .sortedBy { it.order }
            .map { it.toDomain() }
    )
}

private fun WorkPulseThemeEntity.toDomain(): WorkPulseTheme {
    return WorkPulseTheme(
        themeMode = themeMode,
        primaryColor = primaryColor,
        secondaryColor = secondaryColor,
        accentColor = accentColor,
        backgroundColor = backgroundColor,
        surfaceColor = surfaceColor,
        primaryTextColor = primaryTextColor,
        secondaryTextColor = secondaryTextColor,
        darkBackgroundColor = darkBackgroundColor,
        darkSurfaceColor = darkSurfaceColor,
        darkPrimaryTextColor = darkPrimaryTextColor,
        darkSecondaryTextColor = darkSecondaryTextColor,
        cornerRadius = cornerRadius
    )
}

private fun NavigationItemEntity.toDomain(): NavigationItem {
    return NavigationItem(
        navigationKey = navigationKey,
        label = label,
        icon = icon,
        location = location,
        enabled = enabled,
        order = order,
        featureKey = featureKey,
        permissionRequired = permissionRequired
    )
}

private fun QuickActionEntity.toDomain() : QuickAction {
    return QuickAction(
        actionKey = actionKey,
        label = label,
        icon = icon,
        enabled = enabled,
        order = order,
        featureKey = featureKey,
        permissionRequired = permissionRequired
    )
}

private fun HomeSectionEntity.toDomain(): HomeSection{
    return HomeSection(
        sectionKey = sectionKey,
        title = title,
        sectionType = sectionType,
        enabled = enabled,
        order = order,
        featureKey = featureKey,
        permissionRequired = permissionRequired,
        configuration = configuration
    )
}

private fun FeatureEntity.toDomain() : Feature {
    return Feature(
        featureKey = featureKey,
        displayName = displayName,
        enabled= enabled,
        order = order,
        configuration= configuration,
        permissionRequired = permissionRequired
    )
}