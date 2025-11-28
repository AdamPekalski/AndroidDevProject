package com.example.movemate.userinterface.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.movemate.R
import com.example.movemate.userinterface.components.WorkoutListItem
import com.example.movemate.viewmodel.WorkoutViewModel
import kotlinx.coroutines.launch
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutsScreen(
    viewModel: WorkoutViewModel,
    onAddClick: () -> Unit,
    onWorkoutClick: (Int) -> Unit,
    onProgressClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    val state by viewModel.workoutsUiState.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()


    val deletedMsg = stringResource(id = R.string.snackbar_workout_deleted)
    val undoLabel = stringResource(id = R.string.action_undo)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.app_name)) },
                actions = {
                    IconButton(onClick = onProgressClick) {
                        Icon(Icons.Default.Check, contentDescription = "Progress")
                    }
                    IconButton(onClick = onSettingsClick) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddClick) {
                Icon(Icons.Default.Add, contentDescription = "Add workout")
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->

        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            if (state.workouts.isEmpty()) {
                Text(
                    text = stringResource(id = R.string.no_workouts_yet),
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp)
                ) {
                    items(
                        items = state.workouts,
                        key = { it.id }
                    ) { workout ->

                        val dismissState = rememberSwipeToDismissBoxState(
                            confirmValueChange = { value ->
                                if (value == SwipeToDismissBoxValue.EndToStart ||
                                    value == SwipeToDismissBoxValue.StartToEnd
                                ) {
                                    scope.launch {
                                        viewModel.deleteWorkout(workout)

                                        val result = snackbarHostState.showSnackbar(
                                            message = deletedMsg,
                                            actionLabel = undoLabel,
                                            withDismissAction = true
                                        )

                                        if (result == SnackbarResult.ActionPerformed) {
                                            viewModel.restoreWorkout(workout)
                                        }
                                    }
                                    // We handle removal via state, not the dismiss state
                                    false
                                } else {
                                    false
                                }
                            }
                        )

                        SwipeToDismissBox(
                            state = dismissState,
                            enableDismissFromStartToEnd = true,
                            enableDismissFromEndToStart = true,
                            backgroundContent = {}, // optional: add red delete bg later
                            content = {
                                WorkoutListItem(
                                    workout = workout,
                                    dateText = viewModel.formatDate(workout.dateEpochMs),
                                    onClick = { onWorkoutClick(workout.id) }
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}
