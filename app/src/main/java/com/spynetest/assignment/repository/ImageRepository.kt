package com.spynetest.assignment.repository

import com.spynetest.assignment.api.ApiService
import com.spynetest.assignment.model.database.ImagePicker.ImageDao
import com.spynetest.assignment.model.database.ImagePicker.ImageDataBase
import com.spynetest.assignment.model.database.ImagePicker.ImageModel
import okhttp3.MultipartBody
import javax.inject.Inject

class ImageRepository @Inject constructor(
    private val db: ImageDataBase,
    private val dao: ImageDao,
    private val uploadServiceAPI: ApiService,
){
    //Save Image to RoomDB
    suspend fun saveImage(image: ImageModel) = dao.insertImage(image)

    suspend fun getImages() = dao.getAllImage()

    suspend fun getPendingImages() = dao.getPendingImages()

    suspend fun updateImage(image: ImageModel) = dao.markImageUploaded(image)

    suspend fun postImageToServer(part: MultipartBody.Part) = uploadServiceAPI.uploadImage(part)





}