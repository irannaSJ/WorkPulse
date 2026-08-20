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