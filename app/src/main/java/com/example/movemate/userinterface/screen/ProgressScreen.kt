package com.example.movemate.userinterface.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.movemate.R
import com.example.movemate.viewmodel.WorkoutViewModel
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import kotlin.math.min


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProgressScreen(
    viewModel: WorkoutViewModel,
    onBack: () -> Unit
) {
    val state by viewModel.workoutsUiState.collectAsState()
    val totalMinutes = state.workouts.sumOf { it.durationMinutes }
    val totalCalories = state.workouts.sumOf { it.caloriesKcal }

    val weeklyGoalMinutes = 150f
    val progressRaw = (totalMinutes.toFloat() / weeklyGoalMinutes)
    val clampedProgress = min(progressRaw, 1f)
    val animatedProgress by animateFloatAsState(
        targetValue = clampedProgress,
        label = "weeklyProgress"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.progress_title)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(text = stringResource(id = R.string.progress_summary))

            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = stringResource(
                            id = R.string.progress_total_minutes,
                            totalMinutes
                        )
                    )
                    Text(
                        text = stringResource(
                            id = R.string.progress_total_calories,
                            totalCalories
                        )
                    )
                }
            }

            Text(
                text = stringResource(
                    id = R.string.progress_goal_label,
                    weeklyGoalMinutes.toInt()
                ),
                style = MaterialTheme.typography.titleMedium
            )

            LinearProgressIndicator(
                progress = { animatedProgress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(12.dp)
            )

            Text(
                text = stringResource(
                    id = R.string.progress_goal_value,
                    (animatedProgress * 100).toInt()
                ),
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
