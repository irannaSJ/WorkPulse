package com.example.workpulse.data.remote.dto.response

import com.google.gson.annotations.SerializedName

data class WorkPulseConfigResponseDto (
    @SerializedName("message")
    val message : WorkPulseConfigMessageDto
)

data class WorkPulseConfigMessageDto(
    @SerializedName("success")
    val success : Boolean,

    @SerializedName("data")
    val data: WorkPulseConfigDto? = null,

    @SerializedName("message")
    val errorMessage: String? = null
)

data class WorkPulseConfigDto(
    @SerializedName("configuration_name")
    val configurationName: String,

    @SerializedName("version")
    val version: Int,

    @SerializedName("theme")
    val theme: WorkPulseThemeDto,

    @SerializedName("navigation")
    val navigation: List<NavigationItemDto>,

    @SerializedName("quick_actions")
    val quickActions: List<QuickActionDto>,

    @SerializedName("home")
    val home: HomeConfigDto,

    @SerializedName("features")
    val features: List<FeatureDto>
)

data class WorkPulseThemeDto(
    @SerializedName("theme_mode")
    val themeMode: String?,

    @SerializedName("primary_color")
    val primaryColor: String?,

    @SerializedName("secondary_color")
    val secondaryColor: String?,

    @SerializedName("accent_color")
    val accentColor: String?,

    @SerializedName("background_color")
    val backgroundColor: String?,

    @SerializedName("surface_color")
    val surfaceColor: String?,

    @SerializedName("primary_text_color")
    val primaryTextColor: String?,

    @SerializedName("secondary_text_color")
    val secondaryTextColor: String?,

    @SerializedName("dark_background_color")
    val darkBackgroundColor: String?,

    @SerializedName("dark_surface_color")
    val darkSurfaceColor: String?,

    @SerializedName("dark_primary_text_color")
    val darkPrimaryTextColor: String?,

    @SerializedName("dark_secondary_text_color")
    val darkSecondaryTextColor: String?,

    @SerializedName("corner_radius")
    val cornerRadius: Double?
)

data class NavigationItemDto(
    @SerializedName("navigation_key")
    val navigationKey: String,

    @SerializedName("label")
    val label: String,

    @SerializedName("icon")
    val icon: String?,

    @SerializedName("location")
    val location: String,

    @SerializedName("enabled")
    val enabled: Boolean,

    @SerializedName("order")
    val order: Int,

    @SerializedName("feature_key")
    val featureKey: String?,

    @SerializedName("permission_required")
    val permissionRequired: Boolean
)

data class QuickActionDto(
    @SerializedName("action_key")
    val actionKey: String,

    @SerializedName("label")
    val label: String,

    @SerializedName("icon")
    val icon: String?,

    @SerializedName("enabled")
    val enabled: Boolean,

    @SerializedName("order")
    val order: Int,

    @SerializedName("feature_key")
    val featureKey: String?,

    @SerializedName("permission_required")
    val permissionRequired: Boolean
)

data class HomeConfigDto(
    @SerializedName("sections")
    val sections : List<HomeSectionDto>
)

data class HomeSectionDto(
    @SerializedName("section_key")
    val sectionKey: String,

    @SerializedName("title")
    val title: String,

    @SerializedName("section_type")
    val sectionType: String,

    @SerializedName("enabled")
    val enabled: Boolean,

    @SerializedName("order")
    val order: Int,

    @SerializedName("feature_key")
    val featureKey: String?,

    @SerializedName("permission_required")
    val permissionRequired: Boolean,

    @SerializedName("configuration")
    val configuration: String?
)

data class FeatureDto(
    @SerializedName("feature_key")
    val featureKey: String,

    @SerializedName("display_name")
    val displayName: String,

    @SerializedName("enabled")
    val enabled: Boolean,

    @SerializedName("order")
    val order: Int,

    @SerializedName("configuration")
    val configuration: String?,

    @SerializedName("permission_required")
    val permissionRequired: Boolean
)
