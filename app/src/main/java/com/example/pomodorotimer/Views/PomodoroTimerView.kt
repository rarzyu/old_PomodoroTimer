package com.example.pomodorotimer.Views

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pomodorotimer.ViewModels.PomodoroTimerViewModel
import com.example.pomodorotimer.ViewModels.TimerState
import java.util.concurrent.TimeUnit

@Composable
fun PomodoroView(viewModel: PomodoroTimerViewModel) {
    val timeLeft by viewModel.timeLeft.collectAsState()
    val timerState by viewModel.timerState.collectAsState()
    var totalTime: Float = 0F //総時間（TimerProgress用）
    //ひとつ前のタイマーステータス
    var previousTimerState = timerState

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            //ステータス
            Text(
                text = timerState.toString(),
                fontSize = 50.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            //プログレスタイマー
            //総時間の決定
            totalTime = if (timerState == TimerState.TimerState.BREAK) {
                //一時停止の場合、ステータスにはひとつ前のステータスを渡す
                determineTotalTime(viewModel,previousTimerState)
            } else {
                determineTotalTime(viewModel,timerState)
            }
            TimerProgress(timeLeft.toFloat(), totalTime)

            Spacer(modifier = Modifier.height(20.dp))

            //時間
            Text(
                text = formatTime(timeLeft),
                fontSize = 50.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            //ボタン
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
                        Button(onClick = {
                            //ひとつ前のステータスを更新
                            previousTimerState = timerState
                            viewModel.pause()
                        }) {
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

/**
 * 時間をMM:SS表記に変換する処理
 * @param millis:時間（Long型）
 * 　└ここから分・秒を求める
 */
private fun formatTime(millis: Long): String {
    val minutes = TimeUnit.MILLISECONDS.toMinutes(millis)
    val seconds = TimeUnit.MILLISECONDS.toSeconds(millis) % 60
    return "%02d:%02d".format(minutes, seconds)
}

/**
 * TimerProgressの引数「totalTime」を決定する処理
 * パラメータはそのまま渡しているだけ
 * ※ただし、ステータスがBREAKの時は、ひとつ前のステータスを引数に渡す
 */
private fun determineTotalTime(viewModel: PomodoroTimerViewModel,timerState: TimerState.TimerState): Float {
    var result: Long = 0

    when (timerState) {
        //停止時・作業中の場合は作業時間
        TimerState.TimerState.STOPPED,TimerState.TimerState.WORKING -> {
            result = viewModel.workTime
        }
        TimerState.TimerState.BREAK -> {
            result = viewModel.shortBreakTime
        }
        TimerState.TimerState.LONG_BREAK -> {
            result = viewModel.longBreakTime
        }
    }

    return result.toFloat()
}

/**
 * タイマーのプログレスUI部分
 * @param remainingTime:現在時間
 * @param totalTime:総時間
 */
@Composable
fun TimerProgress(remainingTime: Float, totalTime: Float) {
    val progress by animateFloatAsState(
        targetValue = remainingTime / totalTime,
        animationSpec = tween(durationMillis = 500)
    )

    CircularProgressIndicator(
        progress = -progress,
        Modifier.background(Color.LightGray, CircleShape)
            .size(200.dp)
    )
}
