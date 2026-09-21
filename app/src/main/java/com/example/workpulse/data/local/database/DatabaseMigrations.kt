package com.example.workpulse.data.local.database

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_23_24 = object : Migration(23, 24) {

    override fun migrate(database: SupportSQLiteDatabase) {

        database.execSQL(
            """
            ALTER TABLE compOff_application
            ADD COLUMN syncErrorMessage TEXT
            """.trimIndent()
        )
    }
}

// Version 25 changed no schema, but the explicit bridge preserves upgrades from v24.
val MIGRATION_24_25 = object : Migration(24, 25) {
    override fun migrate(database: SupportSQLiteDatabase) = Unit
}

/** Adds local biometric templates without changing existing employee or attendance data. */
val MIGRATION_25_26 = object : Migration(25, 26) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            """
            CREATE TABLE IF NOT EXISTS face_embedding (
                employeeId TEXT NOT NULL,
                embedding TEXT NOT NULL,
                createdAt INTEGER NOT NULL,
                updatedAt INTEGER NOT NULL,
                PRIMARY KEY(employeeId)
            )
            """.trimIndent()
        )
    }
}

val MIGRATION_26_27 = object : Migration(26, 27) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            "ALTER TABLE face_embedding ADD COLUMN modelVersion INTEGER NOT NULL DEFAULT 1"
        )
    }
}

val MIGRATION_27_28 = object : Migration(27, 28) {

    override fun migrate(database: SupportSQLiteDatabase) {

        database.execSQL(
            """
            CREATE TABLE IF NOT EXISTS workpulse_config (
                id INTEGER NOT NULL,
                configurationName TEXT NOT NULL,
                version INTEGER NOT NULL,
                syncedAt INTEGER NOT NULL,
                PRIMARY KEY(id)
            )
            """.trimIndent()
        )

        database.execSQL(
            """
            CREATE TABLE IF NOT EXISTS workpulse_theme (
                configId INTEGER NOT NULL,
                themeMode TEXT NOT NULL,
                primaryColor TEXT,
                secondaryColor TEXT,
                accentColor TEXT,
                backgroundColor TEXT,
                surfaceColor TEXT,
                primaryTextColor TEXT,
                secondaryTextColor TEXT,
                darkBackgroundColor TEXT,
                darkSurfaceColor TEXT,
                darkPrimaryTextColor TEXT,
                darkSecondaryTextColor TEXT,
                cornerRadius REAL NOT NULL,
                PRIMARY KEY(configId)
            )
            """.trimIndent()
        )

        database.execSQL(
            """
            CREATE TABLE IF NOT EXISTS workpulse_navigation (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                configId INTEGER NOT NULL,
                navigationKey TEXT NOT NULL,
                label TEXT NOT NULL,
                icon TEXT,
                location TEXT NOT NULL,
                enabled INTEGER NOT NULL,
                `order` INTEGER NOT NULL,
                featureKey TEXT,
                permissionRequired INTEGER NOT NULL
            )
            """.trimIndent()
        )

        database.execSQL(
            """
            CREATE INDEX IF NOT EXISTS index_workpulse_navigation_configId
            ON workpulse_navigation(configId)
            """.trimIndent()
        )

        database.execSQL(
            """
            CREATE INDEX IF NOT EXISTS index_workpulse_navigation_navigationKey
            ON workpulse_navigation(navigationKey)
            """.trimIndent()
        )

        database.execSQL(
            """
            CREATE TABLE IF NOT EXISTS workpulse_quick_actions (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                configId INTEGER NOT NULL,
                actionKey TEXT NOT NULL,
                label TEXT NOT NULL,
                icon TEXT,
                enabled INTEGER NOT NULL,
                `order` INTEGER NOT NULL,
                featureKey TEXT,
                permissionRequired INTEGER NOT NULL
            )
            """.trimIndent()
        )

        database.execSQL(
            """
            CREATE INDEX IF NOT EXISTS index_workpulse_quick_actions_configId
            ON workpulse_quick_actions(configId)
            """.trimIndent()
        )

        database.execSQL(
            """
            CREATE INDEX IF NOT EXISTS index_workpulse_quick_actions_actionKey
            ON workpulse_quick_actions(actionKey)
            """.trimIndent()
        )

        database.execSQL(
            """
            CREATE TABLE IF NOT EXISTS workpulse_home_sections (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                configId INTEGER NOT NULL,
                sectionKey TEXT NOT NULL,
                title TEXT NOT NULL,
                sectionType TEXT NOT NULL,
                enabled INTEGER NOT NULL,
                `order` INTEGER NOT NULL,
                featureKey TEXT,
                permissionRequired INTEGER NOT NULL,
                configuration TEXT
            )
            """.trimIndent()
        )

        database.execSQL(
            """
            CREATE INDEX IF NOT EXISTS index_workpulse_home_sections_configId
            ON workpulse_home_sections(configId)
            """.trimIndent()
        )

        database.execSQL(
            """
            CREATE INDEX IF NOT EXISTS index_workpulse_home_sections_sectionKey
            ON workpulse_home_sections(sectionKey)
            """.trimIndent()
        )

        database.execSQL(
            """
            CREATE TABLE IF NOT EXISTS workpulse_features (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                configId INTEGER NOT NULL,
                featureKey TEXT NOT NULL,
                displayName TEXT NOT NULL,
                enabled INTEGER NOT NULL,
                `order` INTEGER NOT NULL,
                configuration TEXT,
                permissionRequired INTEGER NOT NULL
            )
            """.trimIndent()
        )

        database.execSQL(
            """
            CREATE INDEX IF NOT EXISTS index_workpulse_features_configId
            ON workpulse_features(configId)
            """.trimIndent()
        )

        database.execSQL(
            """
            CREATE INDEX IF NOT EXISTS index_workpulse_features_featureKey
            ON workpulse_features(featureKey)
            """.trimIndent()
        )
    }
}
