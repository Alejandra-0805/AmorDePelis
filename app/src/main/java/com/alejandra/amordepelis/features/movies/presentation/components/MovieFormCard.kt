package com.alejandra.amordepelis.features.movies.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alejandra.amordepelis.core.ui.theme.AppTheme

@Composable
fun MovieFormCard(
    title: String,
    synopsis: String,
    genre: String,
    duration: String,
    rating: Int,
    isFavorite: Boolean,
    isLoading: Boolean,
    error: String?,
    onTitleChange: (String) -> Unit,
    onSynopsisChange: (String) -> Unit,
    onGenreChange: (String) -> Unit,
    onDurationChange: (String) -> Unit,
    onRatingChange: (Int) -> Unit,
    onFavoriteChange: (Boolean) -> Unit,
    onSubmit: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Información de la película",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )

            OutlinedTextField(
                value = title,
                onValueChange = onTitleChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Título *") },
                placeholder = { Text("Ej: Titanic") },
                enabled = !isLoading,
                singleLine = true,
                isError = error != null && title.isBlank(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    focusedLabelColor = MaterialTheme.colorScheme.primary
                )
            )

            OutlinedTextField(
                value = synopsis,
                onValueChange = onSynopsisChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                label = { Text("Sinopsis") },
                placeholder = { Text("Escribe una breve descripción de la película...") },
                enabled = !isLoading,
                maxLines = 4,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    focusedLabelColor = MaterialTheme.colorScheme.primary
                )
            )

            OutlinedTextField(
                value = genre,
                onValueChange = onGenreChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Género") },
                placeholder = { Text("Ej: Romance, Acción, Comedia") },
                enabled = !isLoading,
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    focusedLabelColor = MaterialTheme.colorScheme.primary
                )
            )

            OutlinedTextField(
                value = duration,
                onValueChange = { value ->
                    if (value.isEmpty() || value.all { it.isDigit() }) {
                        onDurationChange(value)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Duración (minutos)") },
                placeholder = { Text("Ej: 120") },
                enabled = !isLoading,
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    focusedLabelColor = MaterialTheme.colorScheme.primary
                )
            )

            Column {
                Text(
                    text = "Calificación",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))
                RatingBar(
                    rating = rating,
                    onRatingChange = onRatingChange,
                    enabled = !isLoading
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable(enabled = !isLoading) {
                    onFavoriteChange(!isFavorite)
                }
            ) {
                Checkbox(
                    checked = isFavorite,
                    onCheckedChange = onFavoriteChange,
                    enabled = !isLoading,
                    colors = CheckboxDefaults.colors(
                        checkedColor = MaterialTheme.colorScheme.primary
                    )
                )
                Text(
                    text = "Marcar como favorita",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = onSubmit,
                enabled = !isLoading && title.isNotBlank(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = "Agregar película",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Composable
private fun RatingBar(
    rating: Int,
    onRatingChange: (Int) -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        repeat(5) { index ->
            val starRating = index + 1
            Icon(
                imageVector = if (starRating <= rating) Icons.Filled.Star else Icons.Outlined.Star,
                contentDescription = "Estrella $starRating",
                tint = if (starRating <= rating) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.outline
                },
                modifier = Modifier
                    .size(32.dp)
                    .clickable(enabled = enabled) {
                        onRatingChange(if (rating == starRating) 0 else starRating)
                    }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MovieFormCardPreview() {
    AppTheme(dynamicColor = false) {
        MovieFormCard(
            title = "Titanic",
            synopsis = "Una historia de amor épica...",
            genre = "Romance",
            duration = "195",
            rating = 4,
            isFavorite = true,
            isLoading = false,
            error = null,
            onTitleChange = {},
            onSynopsisChange = {},
            onGenreChange = {},
            onDurationChange = {},
            onRatingChange = {},
            onFavoriteChange = {},
            onSubmit = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MovieFormCardEmptyPreview() {
    AppTheme(dynamicColor = false) {
        MovieFormCard(
            title = "",
            synopsis = "",
            genre = "",
            duration = "",
            rating = 0,
            isFavorite = false,
            isLoading = false,
            error = null,
            onTitleChange = {},
            onSynopsisChange = {},
            onGenreChange = {},
            onDurationChange = {},
            onRatingChange = {},
            onFavoriteChange = {},
            onSubmit = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}
