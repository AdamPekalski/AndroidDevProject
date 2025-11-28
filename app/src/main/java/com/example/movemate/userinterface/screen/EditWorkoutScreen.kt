package com.example.movemate.userinterface.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.movemate.R
import com.example.movemate.viewmodel.WorkoutViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditWorkoutScreen(
    viewModel: WorkoutViewModel,
    onDone: () -> Unit
) {
    val uiState by viewModel.editUiState.collectAsState()
    val steps by viewModel.steps.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (uiState.id == null)
                            stringResource(id = R.string.add_workout)
                        else
                            stringResource(id = R.string.edit_workout)
                    )
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = uiState.type,
                onValueChange = viewModel::updateType,
                label = { Text(stringResource(id = R.string.field_type)) },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = uiState.durationMinutes,
                onValueChange = viewModel::updateDuration,
                label = { Text(stringResource(id = R.string.field_duration)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = uiState.distanceKm,
                onValueChange = viewModel::updateDistance,
                label = { Text(stringResource(id = R.string.field_distance)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Text(text = stringResource(id = R.string.field_effort, uiState.effort))
            Slider(
                value = uiState.effort.toFloat(),
                onValueChange = { viewModel.updateEffort(it.toInt()) },
                valueRange = 1f..5f,
                steps = 3
            )

            OutlinedTextField(
                value = uiState.notes,
                onValueChange = viewModel::updateNotes,
                label = { Text(stringResource(id = R.string.field_notes)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 80.dp)
            )

            Divider() //i have no idea why this is crossed out :(

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = stringResource(
                        id = R.string.sensor_steps_label,
                        steps
                    ),
                    style = MaterialTheme.typography.bodySmall
                )

                TextButton(
                    onClick = { viewModel.applyStepsToDurationEstimate() },
                    enabled = steps > 0,
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text(text = stringResource(id = R.string.sensor_use_steps_button))
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    modifier = Modifier.weight(1f),
                    onClick = onDone
                ) {
                    Text(text = stringResource(id = R.string.action_cancel))
                }
                Button(
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.saveCurrentWorkout(onDone) }
                ) {
                    Text(text = stringResource(id = R.string.action_save))
                }
            }
        }
    }
}
