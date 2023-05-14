package com.example.pomodorotimer.ViewModels

import com.example.pomodorotimer.Common.NotificationController
import kotlinx.coroutines.*

/**
 * 時計の処理
 */
class TimerState(
    private val workTime: Long,
    private val shortBreakTime: Long,
    private val longBreakTime: Long,
    private val workBreakSetCount: Int,
    private val totalSetCount: Int,
    private val isTimerVibration: Boolean,
    private val isTimerAlert: Boolean,
    private val notificationController: NotificationController
) {
    //ローカル変数
    private var currentJob: Job? = null
    private val scope = CoroutineScope(Dispatchers.Main)
    private var currentSet = 0 // 現在のセット数
    private var currentTotalSet = 0 // 現在の「全セット数」
    private var timeLeft: Long = workTime   //残り時間

    //タイマーの状態
    enum class TimerState {
        WORKING,
        BREAK,
        LONG_BREAK,
        STOPPED,
        PAUSE
    }

    var state: TimerState = TimerState.STOPPED  //タイマーの状態
    var evacuationState: TimerState = TimerState.STOPPED //一時停止用の一時退避するタイマーの状態
    var onTick: ((Long) -> Unit)? = null
    var onStateChange: ((TimerState) -> Unit)? = null

    fun start() {
        if (state == TimerState.STOPPED) {
            state = TimerState.WORKING
            onStateChange?.invoke(state)
            startTimer(workTime)
        }
    }

    fun pause() {
        currentJob?.cancel()
        evacuationState = state
        state = TimerState.PAUSE
        onStateChange?.invoke(state)
    }

    fun resume() {
        if (state == TimerState.PAUSE) {
            state = evacuationState
            onStateChange?.invoke(state)
            startTimer(timeLeft)
        }
    }

    fun reset() {
        currentJob?.cancel()
        state = TimerState.STOPPED
        timeLeft = workTime
        onTick?.invoke(timeLeft)
        onStateChange?.invoke(state)
    }

    private fun startTimer(duration: Long) {
        currentJob = scope.launch {
            val startTime = System.currentTimeMillis()
            val endTime = startTime + duration

            //タイマー処理
            while (System.currentTimeMillis() < endTime) {
                timeLeft = endTime - System.currentTimeMillis()
                onTick?.invoke(timeLeft)
                delay(1000)
            }

            //タイマーが終了した際の処理
            //バイブ
            if (isTimerVibration) {
                notificationController.vibrate()
            }
            //アラート
            if (isTimerAlert) {
                notificationController.playNotificationSound()
            }

            //次のタイマーの設定
            when (state) {
                TimerState.WORKING -> {
                    currentSet++
                    if (currentSet < workBreakSetCount) {
                        //休憩
                        state = TimerState.BREAK
                        timeLeft = shortBreakTime
                    } else {
                        //長期休憩
                        currentTotalSet++
                        if (currentTotalSet < totalSetCount) {
                            state = TimerState.LONG_BREAK
                            timeLeft = longBreakTime
                            currentSet = 0
                        } else {
                            //全セット終了
                            currentJob?.cancel()
                            state = TimerState.STOPPED
                            timeLeft = workTime
                            onTick?.invoke(timeLeft)
                        }
                    }
                }
                TimerState.BREAK, TimerState.LONG_BREAK -> {
                    state = TimerState.WORKING
                    timeLeft = workTime
                }
                else -> return@launch
            }
            onStateChange?.invoke(state)
            if (state != TimerState.STOPPED) {
                startTimer(timeLeft) //再帰呼び出し
            }
        }
    }
}
