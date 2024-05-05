package com.spynetest.assignment.model.database.ImagePicker

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "AssignmentImageDB")
data class ImageModel(
    @PrimaryKey(autoGenerate = false)
    val imageUri : String = "",
    val uploadStatus: Boolean = false
)
