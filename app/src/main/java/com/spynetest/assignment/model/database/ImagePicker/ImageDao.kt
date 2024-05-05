package com.spynetest.assignment.model.database.ImagePicker

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface ImageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertImage(imageModel: ImageModel)

    @Query("SELECT * FROM AssignmentImageDB")
    suspend fun getAllImage(): MutableList<ImageModel>

    @Query("SELECT * FROM AssignmentImageDB WHERE uploadStatus = 0")
    suspend fun getPendingImages(): MutableList<ImageModel>

    @Update
    suspend fun markImageUploaded(image: ImageModel)
}