package com.example.lamborghiniandroid

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.lamborghiniandroid.todo.data.Car
import com.example.lamborghiniandroid.todo.data.local.CarDao
import com.example.lamborghiniandroid.utils.LocalDateConverters

@Database(entities = [Car::class], version = 2, exportSchema = false)
@TypeConverters(LocalDateConverters::class)
abstract class MyAppDatabase : RoomDatabase() {
    abstract fun carDao(): CarDao

    companion object {
        @Volatile
        private var INSTANCE: MyAppDatabase? = null

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE migrationTest (_id TEXT NOT NULL, text TEXT NOT NULL, PRIMARY KEY(_id))")
            }
        }

        fun getDatabase(context: Context): MyAppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MyAppDatabase::class.java,
                    "cars_database"
                )
                    .addMigrations(MIGRATION_1_2)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}


