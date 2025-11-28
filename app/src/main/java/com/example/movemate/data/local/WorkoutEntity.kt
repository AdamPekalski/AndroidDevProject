package com.example.movemate.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workouts")
data class WorkoutEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val dateEpochMs: Long,
    val type: String,
    val durationMinutes: Int,
    val distanceKm: Double?,
    val effort: Int,
    val caloriesKcal: Int,
    val notes: String?
)
