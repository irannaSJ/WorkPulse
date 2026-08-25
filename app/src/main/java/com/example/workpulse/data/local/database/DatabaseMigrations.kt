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
