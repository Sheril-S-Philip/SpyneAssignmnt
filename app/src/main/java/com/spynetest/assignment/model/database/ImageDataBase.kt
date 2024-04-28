package com.spynetest.assignment.model.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [ImageModel::class],
    version = 1
)
abstract class ImageDataBase: RoomDatabase() {
    abstract fun imageDao(): ImageDao
}