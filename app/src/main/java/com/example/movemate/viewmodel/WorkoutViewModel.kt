package com.example.movemate.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movemate.data.WorkoutRepository
import com.example.movemate.data.local.WorkoutEntity
import com.example.movemate.data.sensor.StepCounterDataSource
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

data class WorkoutsUiState(
    val workouts: List<WorkoutEntity> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

data class EditWorkoutUiState(
    val id: Int? = null,
    val type: String = "",
    val durationMinutes: String = "",
    val distanceKm: String = "",
    val effort: Int = 3,
    val notes: String = "",
    val dateEpochMs: Long = System.currentTimeMillis()
)

class WorkoutViewModel(
    private val repository: WorkoutRepository,
    private val stepCounterDataSource: StepCounterDataSource? = null
) : ViewModel() {

    private val _workoutsUiState = MutableStateFlow(WorkoutsUiState(isLoading = true))
    val workoutsUiState: StateFlow<WorkoutsUiState> = _workoutsUiState.asStateFlow()

    private val _editUiState = MutableStateFlow(EditWorkoutUiState())
    val editUiState: StateFlow<EditWorkoutUiState> = _editUiState.asStateFlow()

    // Sensor steps
    private val _steps = MutableStateFlow(0)
    val steps: StateFlow<Int> = _steps.asStateFlow()

    init {
        observeWorkouts()
        observeSteps()
    }

    private fun observeWorkouts() {
        viewModelScope.launch {
            repository.getAllWorkouts()
                .onEach { list ->
                    _workoutsUiState.value = WorkoutsUiState(workouts = list)
                }
                .catch { e ->
                    _workoutsUiState.value = WorkoutsUiState(
                        workouts = emptyList(),
                        errorMessage = e.message
                    )
                }
                .collect()
        }
    }

    private fun observeSteps() {
        val source = stepCounterDataSource ?: return
        source.start()
        viewModelScope.launch {
            source.steps.collect { value ->
                _steps.value = value
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        stepCounterDataSource?.stop()
    }

    fun startNewWorkout() {
        _editUiState.value = EditWorkoutUiState()
    }

    fun loadWorkoutForEdit(id: Int) {
        viewModelScope.launch {
            val workout = repository.getWorkoutById(id)
            if (workout != null) {
                _editUiState.value = EditWorkoutUiState(
                    id = workout.id,
                    type = workout.type,
                    durationMinutes = workout.durationMinutes.toString(),
                    distanceKm = workout.distanceKm?.toString().orEmpty(),
                    effort = workout.effort,
                    notes = workout.notes.orEmpty(),
                    dateEpochMs = workout.dateEpochMs
                )
            }
        }
    }

    fun updateType(newType: String) {
        _editUiState.update { it.copy(type = newType) }
    }

    fun updateDuration(newDuration: String) {
        _editUiState.update { it.copy(durationMinutes = newDuration) }
    }

    fun updateDistance(newDistance: String) {
        _editUiState.update { it.copy(distanceKm = newDistance) }
    }

    fun updateEffort(newEffort: Int) {
        _editUiState.update { it.copy(effort = newEffort) }
    }

    fun updateNotes(newNotes: String) {
        _editUiState.update { it.copy(notes = newNotes) }
    }

    fun saveCurrentWorkout(onFinished: () -> Unit) {
        viewModelScope.launch {
            val current = _editUiState.value

            val duration = current.durationMinutes.toIntOrNull() ?: 0
            val distance = current.distanceKm.toDoubleOrNull()
            val calories = estimateCalories(
                type = current.type,
                durationMinutes = duration
            )

            val workout = WorkoutEntity(
                id = current.id ?: 0,
                dateEpochMs = current.dateEpochMs,
                type = current.type.ifBlank { "Unknown" },
                durationMinutes = duration,
                distanceKm = distance,
                effort = current.effort,
                caloriesKcal = calories,
                notes = current.notes.ifBlank { null }
            )

            if (current.id == null) {
                repository.insertWorkout(workout)
            } else {
                repository.updateWorkout(workout)
            }

            onFinished()
        }
    }

    fun deleteWorkout(workout: WorkoutEntity) {
        viewModelScope.launch {
            repository.deleteWorkout(workout)
        }
    }

    fun restoreWorkout(workout: WorkoutEntity) {
        viewModelScope.launch {
            repository.insertWorkout(workout)
        }
    }

    // calories estimation
    private fun estimateCalories(type: String, durationMinutes: Int): Int {
        val met = when (type.lowercase()) {
            "walk" -> 3.0
            "run" -> 7.0
            "cycle" -> 6.0
            else -> 4.0
        }
        val calories = met * durationMinutes
        return calories.roundToInt()
    }


    //Use current step count to estimate a walking duration (~60 steps per minute)
    fun applyStepsToDurationEstimate() {
        val stepCount = steps.value
        if (stepCount <= 0) return

        val estimatedMinutes = (stepCount / 60.0)
            .roundToInt()
            .coerceAtLeast(1)

        _editUiState.update { current ->
            current.copy(
                type = if (current.type.isBlank()) "Walk" else current.type,
                durationMinutes = estimatedMinutes.toString()
            )
        }
    }


    // Helper for displaying date
    fun formatDate(epochMs: Long): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        return Instant.ofEpochMilli(epochMs)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
            .format(formatter)
    }
}
