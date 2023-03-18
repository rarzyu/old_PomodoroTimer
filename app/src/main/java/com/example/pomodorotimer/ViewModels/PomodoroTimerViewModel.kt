package com.example.pomodorotimer.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PomodoroViewModel : ViewModel() {
    private val workDuration = 25 * 60 * 1000L // 25分
    private val breakDuration = 5 * 60 * 1000L // 5分
    private val timer = TimerState(workDuration, breakDuration)

    private val _timerState = MutableStateFlow(TimerState.TimerState.STOPPED)
    val timerState: StateFlow<TimerState.TimerState> = _timerState

    private val _timeLeft = MutableStateFlow(workDuration)
    val timeLeft: StateFlow<Long> = _timeLeft

    init {
        timer.onTick = { remainingTime ->
            viewModelScope.launch {
                _timeLeft.emit(remainingTime)
            }
        }

        timer.onStateChange = { newState ->
            viewModelScope.launch {
                _timerState.emit(newState)
            }
        }
    }

    fun start() = timer.start()
    fun pause() = timer.pause()
    fun resume() = timer.resume()
    fun reset() = timer.reset()
}
