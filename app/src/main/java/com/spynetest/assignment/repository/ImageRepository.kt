package com.spynetest.assignment.repository

import android.content.ContentResolver
import android.net.Uri
import com.spynetest.assignment.api.UploadService
import com.spynetest.assignment.model.database.ImageDao
import com.spynetest.assignment.model.database.ImageDataBase
import com.spynetest.assignment.model.database.ImageModel
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.UUID
import javax.inject.Inject

class ImageRepository @Inject constructor(
    private val db: ImageDataBase,
    private val dao: ImageDao,
    private val uploadServiceAPI: UploadService,
){
    //Save Image to RoomDB
    suspend fun saveImage(image: ImageModel) = dao.insertImage(image)

    suspend fun getImages() = dao.getAllImage()

    suspend fun getPendingImages() = dao.getPendingImages()

    suspend fun updateImage(image: ImageModel) = dao.markImageUploaded(image)

     suspend fun postImageToServer(part: MultipartBody.Part) = uploadServiceAPI.uploadImage(part)



}