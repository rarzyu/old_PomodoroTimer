package com.rarzyu.pomodorotimer.ViewModels

import android.os.Build
import com.rarzyu.pomodorotimer.Common.NotificationController
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * 時計の処理
 */
class TimerJobs(
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
    private var timeLeft: Long = workTime   //残り時間

    //タイマーの状態
    enum class TimerState {
        WORKING,
        BREAK,
        LONG_BREAK,
        STOPPED,
        PAUSE
    }

    private val _currentSet = MutableStateFlow(1) // 現在のセット数
    val currentSet: StateFlow<Int> = _currentSet

    private val _currentTotalSet = MutableStateFlow(1) // 現在の「全セット数」
    val currentTotalSet: StateFlow<Int> = _currentTotalSet

    var state: TimerState = TimerState.STOPPED  //タイマーの状態
    var previousState: TimerState = TimerState.STOPPED //一時停止用の一時退避するタイマーの状態
    var onTick: ((Long) -> Unit)? = null
    var onStateChange: ((TimerState) -> Unit)? = null

    fun start() {
        if (state == TimerState.STOPPED) {
            currentJob = CoroutineScope(Dispatchers.Main).launch{
                state = TimerState.WORKING
                onStateChange?.invoke(state)
                startTimer(workTime)
            }
        }
    }

    fun pause() {
        currentJob?.cancel()
        previousState = state
        state = TimerState.PAUSE
        onStateChange?.invoke(state)
    }

    fun resume() {
        if (state == TimerState.PAUSE) {
            state = previousState
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
                delay(5) //更新頻度（ミリ秒）
            }

            //タイマーが終了した際の処理
            //バイブ
            if (isTimerVibration) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    notificationController.vibrate()
                }
            }
            //アラート
            if (isTimerAlert) {
                notificationController.playNotificationSound()
            }

            //次のタイマーの設定
            when (state) {
                TimerState.WORKING -> {
                    if (_currentSet.value < workBreakSetCount) {
                        //休憩
                        state = TimerState.BREAK
                        timeLeft = shortBreakTime
                    } else {
                        if (_currentTotalSet.value < totalSetCount) {
                            //長期休憩
                            state = TimerState.LONG_BREAK
                            timeLeft = longBreakTime
                        } else {
                            //全セット終了
                            _currentSet.value = 1
                            _currentTotalSet.value = 1
                            currentJob?.cancel()
                            state = TimerState.STOPPED
                            timeLeft = workTime
                            onTick?.invoke(timeLeft)
                        }
                    }
                }
                TimerState.BREAK -> {
                    _currentSet.value++
                    state = TimerState.WORKING
                    timeLeft = workTime
                }
                TimerState.LONG_BREAK -> {
                    _currentSet.value = 1 //リセット
                    _currentTotalSet.value++
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
