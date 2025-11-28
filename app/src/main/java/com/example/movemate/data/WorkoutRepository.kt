package com.example.movemate.data

import com.example.movemate.data.local.WorkoutDao
import com.example.movemate.data.local.WorkoutEntity
import kotlinx.coroutines.flow.Flow

class WorkoutRepository(
    private val workoutDao: WorkoutDao
) {

    fun getAllWorkouts(): Flow<List<WorkoutEntity>> = workoutDao.getAllWorkouts()

    suspend fun getWorkoutById(id: Int): WorkoutEntity? = workoutDao.getWorkoutById(id)

    suspend fun insertWorkout(workout: WorkoutEntity) = workoutDao.insertWorkout(workout)

    suspend fun updateWorkout(workout: WorkoutEntity) = workoutDao.updateWorkout(workout)

    suspend fun deleteWorkout(workout: WorkoutEntity) = workoutDao.deleteWorkout(workout)
}
