package com.example.movemate

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.compose.rememberNavController
import com.example.movemate.data.WorkoutRepository
import com.example.movemate.data.local.AppDatabase
import com.example.movemate.data.sensor.StepCounterDataSource
import com.example.movemate.userinterface.MovemateApp
import com.example.movemate.viewmodel.WorkoutViewModel

class MainActivity : ComponentActivity() {

    private val viewModelFactory = viewModelFactory {
        initializer {
            val appContext = applicationContext

            val db = AppDatabase.getDatabase(appContext)
            val repository = WorkoutRepository(db.workoutDao())
            val stepSource = StepCounterDataSource(appContext)

            WorkoutViewModel(
                repository = repository,
                stepCounterDataSource = stepSource
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()
            val viewModel: WorkoutViewModel =
                androidx.lifecycle.viewmodel.compose.viewModel(factory = viewModelFactory)

            MovemateApp(
                navController = navController,
                viewModel = viewModel
            )
        }
    }
}
