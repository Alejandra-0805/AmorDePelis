package com.alejandra.amordepelis.features.user.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun UserInfoCard(
    username: String,
    email: String,
    passwordMasked: String,
    isEditing: Boolean = false,
    onUsernameChange: (String) -> Unit = {},
    onEditClick: () -> Unit = {},
    onSaveClick: () -> Unit = {},
    onDeleteClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Tus datos",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                if (isEditing) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        TextButton(onClick = onEditClick) { Text("Cancelar") }
                        Button(onClick = onSaveClick) { Text("Guardar") }
                    }
                } else {
                    TextButton(onClick = onEditClick) { Text("Editar") }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "NOMBRE DE USUARIO", style = MaterialTheme.typography.labelLarge)
            if (isEditing) {
                OutlinedTextField(
                    value = username,
                    onValueChange = onUsernameChange,
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            } else {
                Text(text = username, style = MaterialTheme.typography.titleLarge)  
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = "CORREO", style = MaterialTheme.typography.labelLarge)  
            Text(text = email, style = MaterialTheme.typography.titleLarge)     
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = "CONTRASEÑA", style = MaterialTheme.typography.labelLarge)
            Text(text = passwordMasked, style = MaterialTheme.typography.titleLarge)
            
            Spacer(modifier = Modifier.height(24.dp))
            OutlinedButton(
                onClick = onDeleteClick,
                colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Eliminar perfil")
            }
        }
    }
}
