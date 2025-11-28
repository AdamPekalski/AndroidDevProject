package com.example.movemate.userinterface

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.movemate.navigation.NavRoutes
import com.example.movemate.userinterface.screen.EditWorkoutScreen
import com.example.movemate.userinterface.screen.ProgressScreen
import com.example.movemate.userinterface.screen.SettingsScreen
import com.example.movemate.userinterface.screen.WorkoutsScreen
import com.example.movemate.viewmodel.WorkoutViewModel

@Composable
fun MovemateApp(
    navController: NavHostController,
    viewModel: WorkoutViewModel
) {
    MaterialTheme {
        Surface {
            NavHost(
                navController = navController,
                startDestination = NavRoutes.WORKOUTS
            ) {
                composable(NavRoutes.WORKOUTS) {
                    WorkoutsScreen(
                        viewModel = viewModel,
                        onAddClick = {
                            viewModel.startNewWorkout()
                            navController.navigate(NavRoutes.EDIT_WORKOUT)
                        },
                        onWorkoutClick = { workoutId ->
                            viewModel.loadWorkoutForEdit(workoutId)
                            navController.navigate(NavRoutes.EDIT_WORKOUT)
                        },
                        onProgressClick = {
                            navController.navigate(NavRoutes.PROGRESS)
                        },
                        onSettingsClick = {
                            navController.navigate(NavRoutes.SETTINGS)
                        }
                    )
                }
                composable(NavRoutes.EDIT_WORKOUT) {
                    EditWorkoutScreen(
                        viewModel = viewModel,
                        onDone = { navController.popBackStack() }
                    )
                }
                composable(NavRoutes.PROGRESS) {
                    ProgressScreen(
                        viewModel = viewModel,
                        onBack = { navController.popBackStack() }
                    )
                }
                composable(NavRoutes.SETTINGS) {
                    SettingsScreen(
                        onBack = { navController.popBackStack() }
                    )
                }
            }
        }
    }
}
