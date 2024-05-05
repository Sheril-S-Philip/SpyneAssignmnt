package com.spynetest.assignment.screens.main

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun MainScreen(viewModel: MainViewModel) {
    var selectedImageUris by remember {
        mutableStateOf<List<Uri>>(emptyList())
    }

    val multiPhotoPickerLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.PickMultipleVisualMedia(5),
        onResult = {uris ->  selectedImageUris = uris})

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            ActionButtons(
                onPickImages = { viewModel.pickImages(multiPhotoPickerLauncher) },
                onSaveToRoom = { viewModel.saveImages(selectedImageUris,"db") },
                onUploadToServer = { viewModel.saveImages(selectedImageUris,"server") }
            )
            Spacer(modifier = Modifier.height(16.dp)) // Spacer to create space between buttons and image grid
            ImageGrid(images = selectedImageUris)
        }
    }
}

@Composable
fun ActionButtons(
    onPickImages: () -> Unit,
    onSaveToRoom: () -> Unit,
    onUploadToServer: () -> Unit,
) {
    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        item {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Button(onClick = onUploadToServer, modifier = Modifier.width(175.dp)) {
                    Text(text = "Upload Image to Server")
                }
                Button(onClick = onSaveToRoom, modifier = Modifier.width(175.dp)) {
                    Text(text = "Save Image to Room")
                }
            }
        }
        item {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Button(
                    onClick = onPickImages, modifier = Modifier.width(175.dp)
                ) {
                    Text(text = "Pick Image(s) from Gallery")
                }
            }

        }
    }
}

@Composable
fun ImageGrid(images: List<Uri>) {
    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        items(images) { uri ->
            AsyncImage(model = uri, contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
        }
    }
}
