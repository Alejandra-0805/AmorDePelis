package com.alejandra.amordepelis.features.movies.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MovieFormCard(
    title: String,
    synopsis: String,
    genre: String,
    duration: String,
    isFavorite: Boolean,
    isLoading: Boolean,
    onTitleChange: (String) -> Unit,
    onSynopsisChange: (String) -> Unit,
    onGenreChange: (String) -> Unit,
    onDurationChange: (String) -> Unit,
    onFavoriteChange: (Boolean) -> Unit,
    onSubmit: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(text = "Agregar pelicula", style = MaterialTheme.typography.headlineSmall)
            OutlinedTextField(
                value = title,
                onValueChange = onTitleChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Titulo") },
                enabled = !isLoading
            )
            OutlinedTextField(
                value = synopsis,
                onValueChange = onSynopsisChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Sinopsis") },
                enabled = !isLoading
            )
            OutlinedTextField(
                value = genre,
                onValueChange = onGenreChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Genero") },
                enabled = !isLoading
            )
            OutlinedTextField(
                value = duration,
                onValueChange = onDurationChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Duracion (min)") },
                enabled = !isLoading
            )

            Row {
                Checkbox(
                    checked = isFavorite,
                    onCheckedChange = onFavoriteChange,
                    enabled = !isLoading
                )
                Text(text = "Marcar como favorita", modifier = Modifier.padding(top = 12.dp))
            }

            Spacer(modifier = Modifier.height(6.dp))
            Button(
                onClick = onSubmit,
                enabled = !isLoading,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Agregar pelicula")
            }
        }
    }
}
