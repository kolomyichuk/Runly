package kolomyichuk.runly.data.local.room

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS `runs_2` (
            `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
            `timestamp` INTEGER NOT NULL,
            `durationInMillis` INTEGER NOT NULL,
            `distanceInMeters` REAL NOT NULL,
            `routePoints` TEXT NOT NULL
            )
        """.trimIndent()
        )

        db.execSQL(
            """
            INSERT INTO runs_2 (id, timestamp, durationInMillis, distanceInMeters, routePoints)
            SELECT id, timestamp, durationInMillis, distanceInMeters, routePoints FROM runs
        """.trimIndent()
        )

        db.execSQL(
            """
            DROP TABLE runs
        """.trimIndent()
        )

        db.execSQL(
            """
            ALTER TABLE runs_2 RENAME TO runs
        """.trimIndent()
        )
    }
}
