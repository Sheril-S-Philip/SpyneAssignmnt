package com.spynetest.assignment.di

import android.content.Context
import androidx.room.Room
import com.spynetest.assignment.model.database.ImagePicker.ImageDataBase
import com.spynetest.assignment.model.database.ImagePicker.ImageModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Provides
    @Singleton
    fun provideImageDb(@ApplicationContext context: Context) = Room.databaseBuilder(context,
        ImageDataBase::class.java,"AssignmentImageDB")
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun provideDao(db: ImageDataBase) = db.imageDao()

    @Provides
    fun providesEntity() = ImageModel()


}