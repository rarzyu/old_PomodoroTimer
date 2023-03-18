package com.example.pomodorotimer.ViewModels

import kotlinx.coroutines.*

class TimerState(private val workDuration: Long, private val breakDuration: Long) {
    private var currentJob: Job? = null
    private val scope = CoroutineScope(Dispatchers.Main)

    enum class TimerState { WORKING, BREAK, STOPPED }

    var state: TimerState = TimerState.STOPPED
    var timeLeft: Long = workDuration
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
        state = TimerState.STOPPED
        onStateChange?.invoke(state)
    }

    fun resume() {
        if (state == TimerState.STOPPED) {
            state = if (timeLeft < breakDuration) TimerState.BREAK else TimerState.WORKING
            onStateChange?.invoke(state)
            startTimer(timeLeft)
        }
    }

    fun reset() {
        pause()
        timeLeft = workDuration
        onTick?.invoke(timeLeft)
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
                    state = TimerState.BREAK
                    timeLeft = breakDuration
                }
                TimerState.BREAK -> {
                    state = TimerState.WORKING
                    timeLeft = workDuration
                }
                else -> return@launch
            }

            onStateChange?.invoke(state)
            startTimer(timeLeft)
        }
    }
}
