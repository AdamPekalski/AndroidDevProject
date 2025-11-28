package com.example.movemate.userinterface.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.movemate.data.local.WorkoutEntity

@Composable
fun WorkoutListItem(
    workout: WorkoutEntity,
    dateText: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 4.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(text = dateText, style = MaterialTheme.typography.labelSmall)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "${workout.type} – ${workout.durationMinutes} min")
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "${workout.caloriesKcal} kcal",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
