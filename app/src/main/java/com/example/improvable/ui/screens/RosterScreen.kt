package com.example.improvable.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.improvable.data.RosterInfo
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.sp

// updating with viewModel addition 3/29
@Composable
fun RosterScreen(onNavigateBack: () -> Unit, // same thing as gameScreen
                 viewModel: RosterViewModel = viewModel(
                     factory = RosterViewModel.Factory(LocalContext.current)
                 )
) {
    val roster by viewModel.roster.collectAsState() // yank roster from the viewModel we made

    // 4/21 adding popup dialog
    var showAddDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Header("Roster")

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                items(roster) { person ->
                    RosterMemberItem(person = person)
                    HorizontalDivider()
                }
            }

            // keep navigation back
            Column(modifier = Modifier
                .padding(top = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally) {
                // 4/21 adding member as a button
                Button(
                    onClick = { showAddDialog = true },
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .size(height = 40.dp, width = 200.dp),
                    shape = RectangleShape,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary
                    )
                ) {
                    Text("Add New Member",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.SansSerif)
                }
                // Back Button
                Button(
                    onClick = onNavigateBack,
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .size(height = 40.dp, width = 200.dp),
                    shape = RectangleShape,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary
                    )) {
                    Text(text = "Back",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.SansSerif)
                }
            }
        }

        // Actually show the dialog when state is true
        if (showAddDialog) {
            AddMemberDialog(
                onDismiss = { showAddDialog = false },
                onConfirm = { firstName, lastName, returning, year, isCore ->
                    viewModel.addMember(firstName, lastName, returning, year, isCore)
                    showAddDialog = false
                }
            )
        }
    }
}

// 4/21 ADDED AN ALERT LIKE THE PLAYER SELECT IN GAME SELECT
// basically copy pasted and modified from there
@Composable
fun AddMemberDialog(
    onDismiss: () -> Unit,
    onConfirm: (firstName: String, lastName: String, returning: Boolean, year: Int, coreCast: Boolean) -> Unit
) {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var year by remember { mutableStateOf("") }
    var isCore by remember { mutableStateOf(false) }
    var isReturning by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add New Member") },
        text = {
            Column {
                TextField(value = firstName, onValueChange = { firstName = it }, label = { Text("First Name") })
                TextField(value = lastName, onValueChange = { lastName = it }, label = { Text("Last Name") })
                // Text field for what year they are input
                TextField(
                    value = year,
                    onValueChange = { if (it.all { theirYear -> theirYear.isDigit() }) year = it },
                    label = { Text("Year (1-4)") }
                )
                // Asking for returning --> if they have been here before it's gonna be no
                // original implementation for returning was for attendance but we scrapped that
                // WILL delete if I have time
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Returning")
                    Checkbox(checked = isReturning, onCheckedChange = { isReturning = it })
                }
                // Core cast verification
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Core Cast")
                    Checkbox(checked = isCore, onCheckedChange = { isCore = it })
                }
            }
        },
        confirmButton = {
            // PUTTING GUARDS UP --> make sure no empty values for name
            Button(onClick = {
                if (firstName.isNotBlank() && lastName.isNotBlank()) {
                    onConfirm(firstName, lastName, isReturning, year.toIntOrNull() ?: 0, isCore)
                }
            }, modifier = Modifier
                .padding(top = 4.dp)
                .size(height = 40.dp, width = 175.dp),
                shape = RectangleShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                )) {
                Text("Add",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.SansSerif)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Back",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.SansSerif)
            }
        }
    )
}

// Used accordian ref from games screen
@Composable
fun RosterMemberItem(
    person: RosterInfo,
) {
  //  var checked by remember { mutableStateOf(false) } // if not clicked --> false
    var expanded = remember { mutableStateOf(false) }
    Column( // 4/21 switched from row to Column
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable{expanded.value = expanded.value.not()}
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${person.firstName} ${person.lastName}",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.weight(1f)
            )
        }

        AnimatedVisibility(
            visible = expanded.value,
            enter = expandVertically(
                spring(
                    stiffness = Spring.StiffnessMediumLow,
                    visibilityThreshold = IntSize.VisibilityThreshold
                )
            ),
            exit = shrinkVertically()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text(text = "Year: ${person.year} ${if (person.coreCast) "-- Core Cast" else ""}")
                //Text(text = "Returning: ${if (person.returning) "Yes" else "No"}")

                // FUTURE IMPLEMENTATION:
                // Maybe we have specifically tagged notes for people
                // Maybe not

//                if (person.notes.isNotEmpty()) {
//                    Text(
//                        text = "Notes: ${person.notes.joinToString(", ")}",
//                        style = MaterialTheme.typography.bodyMedium,
//                        color = MaterialTheme.colorScheme.primary
//                    )
//                }
            }
        }
        // 4/20 STEALING THIS FOR GAME SELECT
//        Checkbox( // can be clicked --> when clicked, then present
//            checked = checked,
//            onCheckedChange = {
//                checked = it // it keyword
//                if (it) {
//                    //box clicked --> bool flipped --> default false --> true
//                   // onAttendanceToggled(true)
//                }
//            }
//        )
        }
    }