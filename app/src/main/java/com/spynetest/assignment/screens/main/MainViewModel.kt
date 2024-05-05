package com.spynetest.assignment.screens.main


import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.spynetest.assignment.model.database.ImagePicker.ImageModel
import com.spynetest.assignment.repository.ImageRepository
import com.spynetest.assignment.util.Converters
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(@ApplicationContext private val applicationContext: Context, private val repository: ImageRepository): ViewModel() {
    private val converters = Converters()
    init {
        Log.d("INITIALISE_VIEW_MODEL", "View Model initialised")
    }

    fun pickImages(multiphotoPickerLauncher: ManagedActivityResultLauncher<PickVisualMediaRequest, List<@JvmSuppressWildcards Uri>>) {
        Log.d("IMAGE_PICKER","IMAGE Picker triggered")
        multiphotoPickerLauncher.launch(
            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
        )
    }

    fun saveImages(uris: List<Uri>, method: String) {
        viewModelScope.launch {
            saveImagestoDB( uris, method)
        }
    }

    private suspend fun saveImagestoDB(uris: List<Uri>, method: String) {
        try {
            val availableImagesUris = repository.getImages().map { it.imageUri }
            uris.forEach { uri ->
                if (!availableImagesUris.contains(uri.toString())) {
                    try {
                        val newImage = ImageModel(uri.toString(), false)
                        repository.saveImage(newImage)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
            if (method=="server"){
                uploadImages()
            }
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    private suspend fun postFileToServer(file: File, imageModel: ImageModel){
        try {
            val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
            val part = MultipartBody.Part.createFormData("image",file.name, requestBody)
            val response = withContext(Dispatchers.IO) { repository.postImageToServer(part) }

            if(response.isSuccessful && response.body() !=null){
                val updatedImageModel = ImageModel(imageModel.imageUri, true)
                updateDB(updatedImageModel)
                withContext(Dispatchers.Main) {
                    Toast.makeText(applicationContext, "Image uploaded to server successfully! -- ${response.body()!!.image}", Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(applicationContext, "Image upload failed! -- ${response.body()!!.image}", Toast.LENGTH_SHORT).show()
            }
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    private suspend fun updateDB(imageModel: ImageModel) {
        repository.updateImage(imageModel)
    }


    private suspend fun uploadImages() {
        try {
            val imagesObjList = repository.getPendingImages()
            imagesObjList.forEach { imageModel ->
                val file = converters.createTempFileFromUri(applicationContext.contentResolver, imageModel.imageUri)
                if (file!=null){
                    postFileToServer(file, imageModel)
                }else{
                    Log.d("FILE_CREATION_Failure","File creation failed or retrieved file is null or corrupt")
                }
            }
        }catch (e : Exception){
            e.printStackTrace()
        }
    }
}