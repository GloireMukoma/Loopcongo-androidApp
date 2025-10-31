package com.example.loopcongo.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// Inclure maintenant User ET Customer
@Database(entities = [User::class, Customer::class], version = 3, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun customerDao(): CustomerDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "user_db"
                )
                    .fallbackToDestructiveMigration() // recrée la DB si changement de version
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

