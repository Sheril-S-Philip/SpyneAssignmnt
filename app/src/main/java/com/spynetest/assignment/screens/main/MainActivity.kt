package com.spynetest.assignment.screens.main


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModelProvider
import com.spynetest.assignment.screens.main.MainViewModel
import com.spynetest.assignment.screens.main.MainScreen
import com.spynetest.assignment.ui.theme.AssignmentTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    lateinit var mainViewModel: MainViewModel

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AssignmentTheme {
                Scaffold(
                    topBar = {
                        TopAppBar(colors = TopAppBarColors(containerColor = Color.Black,
                            titleContentColor = Color.White, scrolledContainerColor = Color.White, navigationIconContentColor = Color.White, actionIconContentColor = Color.White), title = {
                            Text(text = "Spyne Image Picker")
                        })
                    }
                ) {
                    mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]

                    Box(modifier = Modifier.padding(it)) {
                        MainScreen(mainViewModel)
                    }
                }
            }
        }
    }
}
