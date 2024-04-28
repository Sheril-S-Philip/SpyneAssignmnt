package com.spynetest.assignment


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import com.spynetest.assignment.screens.MainViewModel
import com.spynetest.assignment.screens.MainScreen
import com.spynetest.assignment.ui.theme.AssignmentTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var factory: MainViewModel.Factory
    private val viewModel: MainViewModel by viewModels {
        MainViewModel.provideMainViewModelFactory(factory,applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AssignmentTheme {
                MainScreen(viewModel)
            }
        }
    }
}
