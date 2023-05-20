package com.example.pomodorotimer.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pomodorotimer.Common.NotificationController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PomodoroTimerViewModel(private val settingViewModel:SettingViewModel,notificationController: NotificationController) : ViewModel() {

    //ViewModelを変数に
    private val _workTime = settingViewModel.workTime * 60 * 1000L
    val workTime: Long
        get() = _workTime
    private val _shortBreakTime = settingViewModel.shortBreakTime * 60 * 1000L
    val shortBreakTime: Long
        get() = _shortBreakTime
    private val _longBreakTime = settingViewModel.longBreakTime * 60 * 1000L
    val longBreakTime: Long
        get() = _longBreakTime
    private val workBreakSetCount = settingViewModel.workBreakSetCount
    private val totalSetCount = settingViewModel.totalSetCount
    private val isTimerVibration = settingViewModel.isTimerVibration
    private val isTimerAlert = settingViewModel.isTimerAlert

    //タイマークラスの設定
    private val timer = TimerJobs(
        _workTime,
        _shortBreakTime,
        _longBreakTime,
        workBreakSetCount,
        totalSetCount,
        isTimerVibration,
        isTimerAlert,
        notificationController
    )

    //監視する変数たち
    private val _timeLeft = MutableStateFlow(_workTime)
    val timeLeft: StateFlow<Long> = _timeLeft

    private val _timerState = MutableStateFlow(TimerJobs.TimerState.STOPPED)
    val timerState: StateFlow<TimerJobs.TimerState> = _timerState

    private val _previousTimerState = MutableStateFlow(TimerJobs.TimerState.STOPPED)
    val previousTimerState: StateFlow<TimerJobs.TimerState> = _previousTimerState

    private val _currentSet = MutableStateFlow(1)
    val currentSet: StateFlow<Int> = _currentSet

    private val _currentTotalSet = MutableStateFlow(1)
    val currentTotalSet: StateFlow<Int> = _currentTotalSet

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
        viewModelScope.launch {
            timer.currentSet.collect { _currentSet.value = it }
            timer.currentTotalSet.collect { _currentTotalSet.value = it }
        }
    }

    fun start() = timer.start()
    fun pause() = timer.pause()
    fun resume() = timer.resume()
    fun reset() = timer.reset()
}
