package com.example.pomodorotimer.Views

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pomodorotimer.ViewModels.PomodoroViewModel
import com.example.pomodorotimer.ViewModels.TimerState
import java.util.concurrent.TimeUnit


class PomodoroTimerView : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val context = LocalContext.current
            val viewModel = ViewModelProvider(this).get(PomodoroViewModel::class.java)
            PomodoroScreen(viewModel)
        }
    }
}

@Composable
fun PomodoroScreen(viewModel: PomodoroViewModel) {
    val timeLeft by viewModel.timeLeft.collectAsState()
    val timerState by viewModel.timerState.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = formatTime(timeLeft),
                fontSize = 48.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row {
                when (timerState) {
                    TimerState.TimerState.STOPPED -> {
                        Button(onClick = { viewModel.start() }) {
                            Text("Start")
                        }
                    }
                    TimerState.TimerState.WORKING, TimerState.TimerState.BREAK -> {
                        Button(onClick = { viewModel.pause() }) {
                            Text("Pause")
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Button(onClick = { viewModel.reset() }) {
                            Text("Reset")
                        }
                    }
                }
            }
        }
        val navController = rememberNavController()

        NavHost(navController = navController, startDestination = "timer") {
            composable("timer") {
                TimerView(viewModel)
            }
            composable("settings") {
                SettingsView(viewModel)
            }
        }

        FooterView(navController = navController, modifier = Modifier.align(Alignment.BottomCenter))
    }
}

@Composable
fun TimerView(viewModel: PomodoroViewModel) {
    PomodoroScreen(viewModel)
}

@Composable
fun SettingsView(viewModel: PomodoroViewModel) {
    Text(text = "Settings")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    val dummyViewModel = PomodoroViewModel()
    PomodoroScreen(viewModel = dummyViewModel)
}


private fun formatTime(millis: Long): String {
    val minutes = TimeUnit.MILLISECONDS.toMinutes(millis)
    val seconds = TimeUnit.MILLISECONDS.toSeconds(millis) % 60
    return "%02d:%02d".format(minutes, seconds)
}
