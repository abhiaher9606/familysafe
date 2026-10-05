package com.familysafe.app

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat

data class Member(val name: String, val status: String, val sharing: Boolean)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { FamilySafeApp() }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FamilySafeApp() {
    var sharing by remember { mutableStateOf(false) }
    var showPrivacy by remember { mutableStateOf(false) }
    var showInvite by remember { mutableStateOf(false) }
    val members = remember {
        mutableStateListOf(
            Member("You", "This phone", sharing),
            Member("Family member", "Waiting for invitation", false)
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
        val granted = result[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                result[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        sharing = granted
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("FamilySafe") },
                actions = {
                    IconButton(onClick = { showPrivacy = true }) {
                        Icon(Icons.Default.Lock, "Privacy")
                    }
                }
            )
        }
    ) { pad ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(pad).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Card {
                    Column(Modifier.padding(18.dp)) {
                        Text("Private family location", style = MaterialTheme.typography.headlineSmall)
                        Spacer(Modifier.height(6.dp))
                        Text("Only people you invite can see your location. Every member controls their own sharing.")
                        Spacer(Modifier.height(14.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Switch(
                                checked = sharing,
                                onCheckedChange = { enabled ->
                                    if (enabled) {
                                        permissionLauncher.launch(arrayOf(
                                            Manifest.permission.ACCESS_FINE_LOCATION,
                                            Manifest.permission.ACCESS_COARSE_LOCATION
                                        ))
                                    } else sharing = false
                                }
                            )
                            Spacer(Modifier.width(10.dp))
                            Text(if (sharing) "Location sharing ON" else "Location sharing OFF")
                        }
                    }
                }
            }

            item {
                Button(
                    onClick = { showInvite = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.PersonAdd, null)
                    Spacer(Modifier.width(8.dp))
                    Text("Invite family member")
                }
            }

            item {
                Text("Family", style = MaterialTheme.typography.titleLarge)
            }

            items(members) { member ->
                ListItem(
                    leadingContent = {
                        Icon(
                            if (member.sharing) Icons.Default.LocationOn else Icons.Default.LocationOff,
                            null
                        )
                    },
                    headlineContent = { Text(member.name) },
                    supportingContent = { Text(member.status) },
                    trailingContent = {
                        if (member.sharing) Text("Sharing") else Text("Private")
                    }
                )
                HorizontalDivider()
            }

            item {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(Modifier.padding(18.dp)) {
                        Text("Emergency", style = MaterialTheme.typography.titleMedium)
                        Text("The SOS feature will notify your chosen family contacts after you enable it.")
                        Spacer(Modifier.height(10.dp))
                        OutlinedButton(onClick = { }) {
                            Icon(Icons.Default.Sos, null)
                            Spacer(Modifier.width(8.dp))
                            Text("SOS")
                        }
                    }
                }
            }
        }
    }

    if (showPrivacy) {
        AlertDialog(
            onDismissRequest = { showPrivacy = false },
            confirmButton = { TextButton(onClick = { showPrivacy = false }) { Text("OK") } },
            title = { Text("Privacy first") },
            text = { Text("FamilySafe is designed for consent-based location sharing. No member can secretly enable another person's GPS. Production location data will be protected by authenticated access rules and encrypted transport.") }
        )
    }

    if (showInvite) {
        AlertDialog(
            onDismissRequest = { showInvite = false },
            confirmButton = { TextButton(onClick = { showInvite = false }) { Text("Close") } },
            title = { Text("Invite family member") },
            text = { Text("The production version will generate a one-time invitation code/link. The invited person must accept and grant location permission before their location is shared.") }
        )
    }
}
