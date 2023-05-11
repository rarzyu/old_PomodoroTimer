package com.example.pomodorotimer.ViewModels

import kotlinx.coroutines.*

/**
 * 時計の処理
 */
class TimerState(
    private val workDuration: Long,
    private val breakDuration: Long,
    private val longBreakDuration: Long,
    private val workBreakSetCount: Int,
    private val totalSetCount: Int
) {
    private var currentJob: Job? = null
    private val scope = CoroutineScope(Dispatchers.Main)
    private var currentSet = 0 // 現在のセット数
    private var currentTotalSet = 0 // 現在の「全セット数」

    enum class TimerState { WORKING, BREAK, LONG_BREAK, STOPPED, PAUSE }

    var state: TimerState = TimerState.STOPPED  //タイマーの状態
    var evacuationState: TimerState = TimerState.STOPPED //一時停止用の一時退避するタイマーの状態
    var timeLeft: Long = workDuration   //残り時間
    var onTick: ((Long) -> Unit)? = null
    var onStateChange: ((TimerState) -> Unit)? = null

    fun start() {
        if (state == TimerState.STOPPED) {
            state = TimerState.WORKING
            onStateChange?.invoke(state)
            startTimer(workDuration)
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
        timeLeft = workDuration
        onTick?.invoke(timeLeft)
        onStateChange?.invoke(state)
    }

    private fun startTimer(duration: Long) {
        currentJob = scope.launch {
            val startTime = System.currentTimeMillis()
            val endTime = startTime + duration

            while (System.currentTimeMillis() < endTime) {
                timeLeft = endTime - System.currentTimeMillis()
                onTick?.invoke(timeLeft)
                delay(1000)
            }

            when (state) {
                TimerState.WORKING -> {
                    currentSet++
                    if (currentSet < workBreakSetCount) {
                        //休憩
                        state = TimerState.BREAK
                        timeLeft = breakDuration
                    } else {
                        //長期休憩
                        currentTotalSet++
                        if (currentTotalSet < totalSetCount) {
                            state = TimerState.LONG_BREAK
                            timeLeft = longBreakDuration
                            currentSet = 0
                        } else {
                            //全セット終了
                            currentJob?.cancel()
                            state = TimerState.STOPPED
                            timeLeft = workDuration
                            onTick?.invoke(timeLeft)
                        }
                    }
                }
                TimerState.BREAK, TimerState.LONG_BREAK -> {
                    state = TimerState.WORKING
                    timeLeft = workDuration
                }
                else -> return@launch
            }
            onStateChange?.invoke(state)
            if (state != TimerState.STOPPED) {
                startTimer(timeLeft) //再帰呼び出し
            }
        }
    }

//    fun resetToNextState() {
//        state = when (state) {
//            TimerState.WORKING -> TimerState.BREAK
//            TimerState.BREAK -> TimerState.WORKING
//            TimerState.LONG_BREAK -> TimerState.WORKING
//            else -> TimerState.STOPPED
//        }
//        timeLeft = when (state) {
//            TimerState.WORKING -> workDuration
//            TimerState.BREAK -> breakDuration
//            TimerState.LONG_BREAK -> longBreakDuration
//            else -> workDuration
//        }
//        onStateChange?.invoke(state)
//    }
//
//    fun resetToLongBreakTime() {
//        state = TimerState.STOPPED
//        timeLeft = longBreakDuration
//        onStateChange?.invoke(state)
//    }
}
