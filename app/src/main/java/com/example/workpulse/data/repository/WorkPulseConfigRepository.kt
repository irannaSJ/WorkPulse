package com.example.workpulse.data.repository

import android.util.Log
import androidx.room.withTransaction
import com.example.workpulse.data.local.dao.config.FeatureDao
import com.example.workpulse.data.local.dao.config.HomeSectionDao
import com.example.workpulse.data.local.dao.config.NavigationItemDao
import com.example.workpulse.data.local.dao.config.QuickActionDao
import com.example.workpulse.data.local.dao.config.WorkPulseConfigDao
import com.example.workpulse.data.local.dao.config.WorkPulseThemeDao
import com.example.workpulse.data.local.database.WorkPulseDatabase
import com.example.workpulse.data.remote.WorkPulseConfigApi
import com.example.workpulse.data.remote.dto.response.WorkPulseConfigDto
import com.example.workpulse.feature.config.domain.WorkPulseConfig
import com.example.workpulse.feature.config.mapper.toDomain
import com.example.workpulse.feature.config.mapper.toEntity
import javax.inject.Inject

class WorkPulseConfigRepository @Inject constructor(
    private val api: WorkPulseConfigApi,
    private val database: WorkPulseDatabase,
    private val configDao : WorkPulseConfigDao,
    private val themeDao: WorkPulseThemeDao,
    private val navigationDao: NavigationItemDao,
    private val quickActionDao: QuickActionDao,
    private val homeSectionDao: HomeSectionDao,
    private val featureDao: FeatureDao

    ) {
    suspend fun fetchConfiguration() : WorkPulseConfigDto{
        val response = api.getWorkPulseConfiguration()

        if (!response.message.success) {
            throw IllegalStateException(
                response.message.errorMessage
                    ?: "Failed to load WorkPulse configuration"
            )
        }

        return response.message.data
            ?: throw IllegalStateException(
                "WorkPulse configuration data is missing"
            )
    }

    suspend fun getCachedConfiguration(): WorkPulseConfig? {
        return database.withTransaction {

            val config = configDao.getConfig()
                ?: return@withTransaction null

            val theme = themeDao.getTheme()
                ?: return@withTransaction null

            val navigation = navigationDao.getAll()

            val quickActions = quickActionDao.getAll()

            val homeSections = homeSectionDao.getAll()

            val features = featureDao.getAll()

            config.toDomain(
                theme = theme,
                navigation = navigation,
                quickActions = quickActions,
                homeSections = homeSections,
                features = features
            )
        }
    }

    suspend fun saveConfiguration(
        configuration: WorkPulseConfigDto
    ){
        val config = configuration.toDomain()

        database.withTransaction {

            navigationDao.deleteAll()
            quickActionDao.deleteAll()
            homeSectionDao.deleteAll()
            featureDao.deleteAll()

            configDao.upsert(config.toEntity())
            themeDao.upsert(config.theme.toEntity())

            navigationDao.insertAll(
                config.navigation.map { it.toEntity() }
            )

            quickActionDao.insertAll(
                config.quickActions.map { it.toEntity() }
            )

            homeSectionDao.insertAll(
                config.home.sections.map { it.toEntity() }
            )

            featureDao.insertAll(
                config.features.map { it.toEntity() }
            )
        }
    }


    suspend fun syncConfiguration() : WorkPulseConfig?{
        val remoteConfig = fetchConfiguration()
        val cachedConfig =configDao.getConfig()
        val shouldUpdate =
            cachedConfig == null ||
                    cachedConfig.configurationName != remoteConfig.configurationName ||
                    remoteConfig.version > cachedConfig.version

        if(shouldUpdate){
            saveConfiguration(remoteConfig)
            Log.d(
                "WorkPulseConfig",
                "Configuration updated: ${remoteConfig.configurationName}, " +
                        "version=${remoteConfig.version}"
            )
        }
        else{
            Log.d(
                "WorkPulseConfig",
                "Configuration already up to date: " +
                        "${cachedConfig.configurationName}, " +
                        "version=${cachedConfig.version}"
            )
        }
        return getCachedConfiguration()
    }
}