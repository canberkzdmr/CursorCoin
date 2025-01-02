package com.cursorcoin.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [],  // Add your entities here
    version = 1,
    exportSchema = true
)
@TypeConverters() // Add your type converters here
abstract class AppDatabase : RoomDatabase() {
    // Add your DAOs here
} 