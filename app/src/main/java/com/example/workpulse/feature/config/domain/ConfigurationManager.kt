package com.example.workpulse.feature.config.domain

import com.example.workpulse.data.repository.WorkPulseConfigRepository
import com.example.workpulse.feature.config.WorkPulseFeatureRegistry
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ConfigurationManager @Inject constructor(
    private val repository: WorkPulseConfigRepository
) {

    private val _configuration =
        MutableStateFlow<WorkPulseConfig?>(null)

    val configuration: StateFlow<WorkPulseConfig?> =
        _configuration.asStateFlow()

    suspend fun loadFromCache() {
        _configuration.value =
            repository.getCachedConfiguration()
    }

    suspend fun sync() {
        _configuration.value =
            repository.syncConfiguration()
    }

    fun getNavigationItems(): List<NavigationItem> {
        return _configuration.value
            ?.navigation
            ?.filter { it.enabled }
            .orEmpty()
    }

    fun getQuickActions(): List<QuickAction> {
        return _configuration.value
            ?.quickActions
            ?.filter { it.enabled }
            .orEmpty()
    }

    fun getHomeSections(): List<HomeSection> {
        return _configuration.value
            ?.home
            ?.sections
            ?.filter { it.enabled }
            .orEmpty()
    }

    fun getFeatures(): List<Feature> {
        return _configuration.value
            ?.features
            .orEmpty()
    }

    fun isFeatureEnabled(featureKey: String): Boolean {
        return getFeature(featureKey)?.enabled == true
    }

    fun isFeatureVisible(featureKey : String?): Boolean
    {
        if(featureKey.isNullOrBlank()){
            return true
        }
        return isFeatureEnabled(featureKey)
    }
    fun getFeature(featureKey: String): Feature? {
        if (!WorkPulseFeatureRegistry.isSupported(featureKey)){
            return null
            }
            return _configuration.value
                ?.features
                ?.firstOrNull {
                    it.featureKey.equals(featureKey, ignoreCase = true)
                }
    }
}