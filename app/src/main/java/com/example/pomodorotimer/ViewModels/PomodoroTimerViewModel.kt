package com.example.pomodorotimer.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pomodorotimer.Common.NotificationController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PomodoroTimerViewModel(private val settingViewModel:SettingViewModel,notificationController: NotificationController) : ViewModel() {

    //ViewModelを変数に
    private val workTime = settingViewModel.workTime * 60 * 1000L
    private val shortBreakTime = settingViewModel.shortBreakTime * 60 * 1000L
    private val longBreakTime = settingViewModel.longBreakTime * 60 * 1000L
    private val workBreakSetCount = settingViewModel.workBreakSetCount
    private val totalSetCount = settingViewModel.totalSetCount
    private val isTimerVibration = settingViewModel.isTimerVibration
    private val isTimerAlert = settingViewModel.isTimerAlert

    //タイマークラスの設定
    private val timer = TimerState(
        workTime,
        shortBreakTime,
        longBreakTime,
        workBreakSetCount,
        totalSetCount,
        isTimerVibration,
        isTimerAlert,
        notificationController
    )

    private val _timerState = MutableStateFlow(TimerState.TimerState.STOPPED)
    val timerState: StateFlow<TimerState.TimerState> = _timerState

    private val _timeLeft = MutableStateFlow(workTime)
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
