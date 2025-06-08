package com.example.studentmanagerfull

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [StudentEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun studentDao(): StudentDao
}