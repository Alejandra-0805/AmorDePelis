package com.alejandra.amordepelis.features.lists.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import android.graphics.Color as AndroidColor

@Composable
fun AddListFormCard(
    name: String,
    description: String,
    colorHex: String,
    isLoading: Boolean,
    onNameChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onColorChange: (String) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Agregar lista", style = MaterialTheme.typography.headlineSmall)

            OutlinedTextField(
                value = name,
                onValueChange = onNameChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Nombre") },
                enabled = !isLoading
            )

            OutlinedTextField(
                value = description,
                onValueChange = onDescriptionChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Descripcion") },
                enabled = !isLoading
            )

            Text(
                text = "Color",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 12.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val colorValue = try {
                    AndroidColor.parseColor(colorHex)
                } catch (e: Exception) {
                    AndroidColor.parseColor("#3B82F6")
                }

                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            color = Color(colorValue),
                            shape = RoundedCornerShape(8.dp)
                        )
                )

                OutlinedTextField(
                    value = colorHex,
                    onValueChange = onColorChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 12.dp),
                    label = { Text("#RRGGBB") },
                    enabled = !isLoading,
                    singleLine = true
                )
            }

            Button(
                onClick = onSaveClick,
                enabled = !isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
            ) {
                Text("Crear lista")
            }
        }
    }
}
