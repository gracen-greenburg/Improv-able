package com.example.improvable.ui.screens

import android.util.Range
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.improvable.data.SessionInfo
import java.util.Date

@Composable
fun SessionsScreen(
    onNavigateBack: () -> Unit, // same thing as gameScreen
    onNavigateToScenes: () -> Unit,
    viewModel: SessionsListViewModel = viewModel(
        factory = SessionsListViewModel.Factory(LocalContext.current)
    )
) {
    val allSessions by viewModel.displayedSessions.collectAsState()
    val displayedSessions = allSessions.sortedByDescending { it.date }
    Column(modifier = Modifier.fillMaxSize()) {

        Header("Sessions")

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            //NOTE: make this button add a new Session and take the user to the SessionScreen for that session.
            Button(onClick = {
                val newSesh = SessionInfo(emptyList(), System.currentTimeMillis()/1000, "")
                viewModel.setCurSesh(newSesh)
                onNavigateToScenes()
            },  modifier = Modifier
                .padding(top = 4.dp)
                .size(height = 40.dp, width = 175.dp),
                shape = RectangleShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                ))  {
                Text("Add New",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.SansSerif)
            }
            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider()
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                items(displayedSessions) { session ->
                    SessionItem(session, viewModel, onNavigateToScenes)
                    HorizontalDivider()
                }
            }

            Button(onClick = onNavigateBack,  modifier = Modifier
                .padding(top = 16.dp)
                .size(height = 40.dp, width = 175.dp),
                shape = RectangleShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                ))  {
                Text("Back",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.SansSerif)
            }
        }
    }

}

@Composable
fun SessionItem(session : SessionInfo,
                viewModel : SessionsListViewModel,
                onNavigateToScenes: () -> Unit){
    var expanded = remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable {
                expanded.value = expanded.value.not()
            }
    ) {
        val date = Date(session.date * 1000)
        val parsedDate : List<String> = date.toString().split(" ")
        // 0->dayOfWeek(Mon), 1->month(Jan), 2->day(DD) 3->time(HH:MM:SS), 4->timezone(EDT), 5->year(YYYY)
        val text = (toFullMonth(parsedDate[1]) + " "
                  + toOrdinal(parsedDate[2].toInt()) + ", "
                  + parsedDate[5] + " ("
                  + toFullDay(parsedDate[0]) + ")")
        Text(text = text, style = MaterialTheme.typography.titleLarge)
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
                .padding(vertical = 4.dp)
        ) {
            if (session.scenes.isNotEmpty()) {
                var text = "Played " + session.scenes[0].game!!.title
                for (i in 1..<session.scenes.size) {
                    if (session.scenes.size > 2) text += ","
                    text += " "
                    if (i == session.scenes.size-1) text += "and "
                    text += session.scenes[i].game!!.title
                }
                Text(text)
            }

            Button(
                onClick = {
                    viewModel.setCurSesh(session)
                    onNavigateToScenes() },
                //modifier = Modifier.padding(top = 4.dp, bottom = 8.dp))
                 modifier = Modifier
                                .padding(top = 4.dp, bottom = 8.dp)
                                .size(height = 40.dp, width = 220.dp),
                                shape = RectangleShape,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.secondary
                                )){
                Text("View Session Details",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.SansSerif)
            }

            Text(session.notes, fontStyle = FontStyle.Italic)
        }
    }
}

// Converts a number to a string that shows that number's place in a list (i.e. its ordinal form)
// 1 -> 1st, 2 -> 2nd, 3 -> 3rd, 4 -> 4th, etc.
fun toOrdinal(value: Int) : String {
    if ((value / 10) % 10 == 1) return value.toString() + "th"
    if (value % 10 == 1) return value.toString() + "st"
    if (value % 10 == 2) return value.toString() + "nd"
    if (value % 10 == 3) return value.toString() + "rd"
    return value.toString() + "th"
}

// Converts an abbreviated date to its full form.
fun toFullDay(day: String) : String {
    when (day.toLowerCase(Locale.current).substring(0, 2)) {
        "mo" -> return "Monday"
        "tu" -> return "Tuesday"
        "we" -> return "Wednesday"
        "th" -> return "Thursday"
        "fr" -> return "Friday"
        "sa" -> return "Saturday"
        "su" -> return "Sunday"
    }
    return "Not a day."
}

// Converts an abbreviated month to its full form.
fun toFullMonth(month: String) : String {
    when (month.toLowerCase(Locale.current).substring(0, 3)) {
        "jan" -> return "January"
        "feb" -> return "February"
        "mar" -> return "March"
        "apr" -> return "April"
        "may" -> return "May"
        "jun" -> return "June"
        "jul" -> return "July"
        "aug" -> return "August"
        "sep" -> return "September"
        "oct" -> return "October"
        "nov" -> return "November"
        "dec" -> return "December"
    }
    return "Not a month."
}