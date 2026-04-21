package com.example.improvable.ui.screens

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
import androidx.compose.material3.Checkbox
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

// updating with viewModel addition 3/29
@Composable
fun RosterScreen(onNavigateBack: () -> Unit, // same thing as gameScreen
                 viewModel: RosterViewModel = viewModel(
                     factory = RosterViewModel.Factory(LocalContext.current)
                 )
) {
    val roster by viewModel.roster.collectAsState() // yank roster from the viewModel we made

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // make the text look prettier later, figure this out now
        Text(text = "Roster Attendance", style = MaterialTheme.typography.headlineMedium)
        // keep navigation back
        Column(modifier = Modifier.padding(top = 16.dp)) {
            Button(onClick = onNavigateBack) {
                Text(text = "Back")
            }
        }
    }
}

@Composable
fun RosterMemberItem(
    person: RosterInfo,
) {
    var checked by remember { mutableStateOf(false) } // if not clicked --> false
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = "${person.firstName} ${person.lastName}", style = MaterialTheme.typography.titleMedium)
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