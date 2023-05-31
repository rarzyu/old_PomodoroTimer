package com.rarzyu.pomodorotimer.Views

import androidx.compose.foundation.background
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rarzyu.pomodorotimer.ViewModels.PomodoroTimerViewModel
import com.rarzyu.pomodorotimer.ViewModels.TimerJobs
import java.util.concurrent.TimeUnit

@Composable
fun PomodoroView(viewModel: PomodoroTimerViewModel) {
    val timeLeft by viewModel.timeLeft.collectAsState()
    val timerState by viewModel.timerState.collectAsState()
    val previousTimerState by viewModel.previousTimerState.collectAsState() //ひとつ前のステータス
    val currentSet by viewModel.currentSet.collectAsState()
    val currentTotalSet by viewModel.currentTotalSet.collectAsState()
    var totalTime = 0L //総時間（TimerProgress用）

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            //ステータスやセット数
            val currentSetText = "作業と休憩のセット：" + currentSet + "セット目"
            val currentTotalSetText = "全セット：" + currentTotalSet + "セット目"

            Text(
                text = determineTimerStateString(timerState) ,
                textAlign = TextAlign.Center,
                fontSize = 40.sp
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = currentSetText,
                textAlign = TextAlign.Center,
                fontSize = 20.sp
            )
            Text(
                text = currentTotalSetText ,
                textAlign = TextAlign.Center,
                fontSize = 20.sp
            )

            Spacer(modifier = Modifier.height(30.dp))

            //プログレスタイマー
            //総時間の決定
            totalTime = if (timerState == TimerJobs.TimerState.PAUSE) {
                //一時停止の場合、ステータスにはひとつ前のステータスを渡す
                determineTotalTime(viewModel,previousTimerState)
            } else {
                determineTotalTime(viewModel,timerState)
            }
            TimerProgress(timeLeft, totalTime)

            Spacer(modifier = Modifier.height(30.dp))

            //ボタン
            Row {
                when (timerState) {
                    //停止状態
                    TimerJobs.TimerState.STOPPED -> {
                        Button(onClick = { viewModel.start() }) {
                            Text("スタート")
                        }
                    }
                    //一時停止状態
                    TimerJobs.TimerState.PAUSE -> {
                        Button(onClick = { viewModel.resume() }) {
                            Text("再開")
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Button(onClick = { viewModel.reset() }) {
                            Text("リセット")
                        }
                    }
                    //動作中（作業・休憩問わず）
                    TimerJobs.TimerState.WORKING, TimerJobs.TimerState.BREAK, TimerJobs.TimerState.LONG_BREAK -> {
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
private fun determineTotalTime(viewModel: PomodoroTimerViewModel,timerState: TimerJobs.TimerState): Long {
    var result: Long = 0

    when (timerState) {
        //停止時・作業中の場合は作業時間
        TimerJobs.TimerState.STOPPED,TimerJobs.TimerState.WORKING -> {
            result = viewModel.workTime
        }
        TimerJobs.TimerState.BREAK -> {
            result = viewModel.shortBreakTime
        }
        TimerJobs.TimerState.LONG_BREAK -> {
            result = viewModel.longBreakTime
        }
    }

    return result
}

/**
 * ステータスから表示する文字を求める
 * @param
 */
private fun determineTimerStateString(timerState: TimerJobs.TimerState): String{
    var result = ""

    when(timerState){
        TimerJobs.TimerState.STOPPED -> {
            result = "さぁ、はじめよう"
        }
        TimerJobs.TimerState.WORKING -> {
            result = "作業中"
        }
        TimerJobs.TimerState.BREAK -> {
            result = "休憩中"
        }
        TimerJobs.TimerState.LONG_BREAK -> {
            result = "休憩中（長期）"
        }
        TimerJobs.TimerState.PAUSE -> {
            result = "一時停止"
        }
    }

    return result
}

/**
 * タイマーのプログレスUI部分
 * @param timeLeft:現在時間
 * @param totalTime:総時間
 */
@Composable
fun TimerProgress(timeLeft: Long, totalTime: Long) {
    var progress = timeLeft.toFloat() / totalTime.toFloat()

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(300.dp)
    ) {

        CircularProgressIndicator(
            progress = -progress, //時計回り
            strokeWidth = 200.dp,
            modifier = Modifier
                .fillMaxSize()
                .background(Color.LightGray, CircleShape),
            color = Color.Blue
        )
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(150.dp)
        ){
            androidx.compose.foundation.Canvas(
                modifier = Modifier.fillMaxSize(),
                onDraw = {
                    drawCircle(
                        Color.White
                    )
                }
            )
            Text(
                text = formatTime(timeLeft),
                fontSize = 50.sp
            )
        }
    }
}
