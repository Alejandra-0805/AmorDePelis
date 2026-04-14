package com.alejandra.amordepelis.features.user.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun PartnerSectionCard(
    hasPartner: Boolean,
    partnerUsername: String,
    partnerEmail: String,
    onAddPartnerClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Pareja",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(12.dp))

            if (hasPartner) {
                Text(text = "NOMBRE DE USUARIO", style = MaterialTheme.typography.labelLarge)
                Text(text = partnerUsername, style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(10.dp))
                Text(text = "CORREO", style = MaterialTheme.typography.labelLarge)
                Text(text = partnerEmail, style = MaterialTheme.typography.titleLarge)
            } else {
                Text(
                    text = "Aun no tienes una pareja. Envia una invitacion por username para comenzar a registrar peliculas juntos.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(14.dp))
                Button(onClick = onAddPartnerClick, modifier = Modifier.fillMaxWidth()) {
                    Text(text = "Agregar pareja")
                }
            }
        }
    }
}
