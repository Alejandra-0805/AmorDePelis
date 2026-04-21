package com.alejandra.amordepelis.features.user.presentation.components

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun PartnerSectionCard(
    hasPartner: Boolean,
    ownInviteCode: String,
    roomName: String,
    partnerUsername: String,
    inviteCodeInput: String,
    onInviteCodeChange: (String) -> Unit,
    onJoinRoomClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Log.d("PartnerSectionCard", "hasPartner: $hasPartner, roomName: $roomName, partnerUsername: $partnerUsername")
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier.padding(20.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // ESTADO VINCULADO (COUPLED)
            if (hasPartner) {
                Icon(
                    imageVector = Icons.Outlined.Favorite,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(48.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
                
                Text(
                    text = "Vinculado con tu pareja",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = partnerUsername.ifBlank { "Tu Pareja" },
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.height(8.dp))
                
                // Mostrar solo el nombre de la sala. Oculta el código de invitación
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Text(
                        text = roomName.ifBlank { "Sala Compartida" },
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            } 
            // ESTADO DISPONIBLE (SINGLE)
            else {
                Text(
                    text = "Aún no tienes pareja vinculada",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(16.dp))
                
                // Mostrar datos propios de la sala y código de invitación
                Text(text = "Tu nombre de sala: ${roomName.ifBlank { "Sin Asignar" }}")
                Text(
                    text = "Tu código: $ownInviteCode",
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Black),
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(16.dp))
                
                // Campo TextField y botón Unirse
                OutlinedTextField(
                    value = inviteCodeInput,
                    onValueChange = onInviteCodeChange,
                    label = { Text("Código de tu pareja") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = onJoinRoomClick,
                    enabled = inviteCodeInput.isNotBlank(), 
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "Unirse a la sala",
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
            }
        }
    }
}

