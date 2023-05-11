package com.example.pomodorotimer.Views

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pomodorotimer.ViewModels.PomodoroTimerViewModel
import com.example.pomodorotimer.ViewModels.TimerState
import java.util.concurrent.TimeUnit

@Composable
fun PomodoroView(viewModel: PomodoroTimerViewModel) {
    val timeLeft by viewModel.timeLeft.collectAsState()
    val timerState by viewModel.timerState.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = timerState.toString(),
                fontSize = 50.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = formatTime(timeLeft),
                fontSize = 48.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row {
                when (timerState) {
                    //停止状態
                    TimerState.TimerState.STOPPED -> {
                        Button(onClick = { viewModel.start() }) {
                            Text("スタート")
                        }
                    }
                    //一時停止状態
                    TimerState.TimerState.PAUSE -> {
                        Button(onClick = { viewModel.resume() }) {
                            Text("再開")
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Button(onClick = { viewModel.reset() }) {
                            Text("リセット")
                        }
                    }
                    //動作中（作業・休憩問わず）
                    TimerState.TimerState.WORKING, TimerState.TimerState.BREAK, TimerState.TimerState.LONG_BREAK -> {
                        Button(onClick = { viewModel.pause() }) {
                            Text("一時停止")
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Button(onClick = { viewModel.reset() }) {
                            Text("リセット")
                        }
                    }
                }
            }
        }
    }
}
private fun formatTime(millis: Long): String {
    val minutes = TimeUnit.MILLISECONDS.toMinutes(millis)
    val seconds = TimeUnit.MILLISECONDS.toSeconds(millis) % 60
    return "%02d:%02d".format(minutes, seconds)
}
