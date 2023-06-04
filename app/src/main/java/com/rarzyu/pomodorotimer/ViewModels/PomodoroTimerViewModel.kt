package com.rarzyu.pomodorotimer.ViewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rarzyu.pomodorotimer.Common.NotificationController
import com.rarzyu.pomodorotimer.Models.SettingDataModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PomodoroTimerViewModel(
    private val settingDataModel: SettingDataModel,
    notificationController: NotificationController
) : ViewModel() {

    //ViewModelを変数に
    var workTime by mutableStateOf(settingDataModel.getWorkTime() * 60 * 1000L)
    var shortBreakTime by mutableStateOf(settingDataModel.getShortBreakTime() * 60 * 1000L)
    var longBreakTime by mutableStateOf(settingDataModel.getLongBreakTime() * 60 * 1000L)
    var workBreakSetCount by mutableStateOf(settingDataModel.getWorkBreakSetCount())
    var totalSetCount by mutableStateOf(settingDataModel.getTotalSetCount())
    var isTimerVibration by mutableStateOf(settingDataModel.getTimerVibration())
    var isTimerAlert by mutableStateOf(settingDataModel.getTimerAlert())

    //タイマークラスの設定
    private var timer = TimerJobs(
        workTime,
        shortBreakTime,
        longBreakTime,
        workBreakSetCount,
        totalSetCount,
        isTimerVibration,
        isTimerAlert,
        notificationController
    )

    //監視する変数たち
    private val _timeLeft = MutableStateFlow(workTime)
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
        }
        viewModelScope.launch {
            timer.currentTotalSet.collect { _currentTotalSet.value = it }
        }
    }

    fun start() {
        //スタート前に更新する
        updateSettings()
        timer.start()
    }
    fun pause() = timer.pause()
    fun resume() = timer.resume()
    fun reset() = timer.reset()

    /**
     *設定情報の更新時に行う処理
     */
    private fun updateSettings() {
        workTime = settingDataModel.getWorkTime() * 60 * 1000L
        shortBreakTime = settingDataModel.getShortBreakTime() * 60 * 1000L
        longBreakTime = settingDataModel.getLongBreakTime() * 60 * 1000L
        workBreakSetCount = settingDataModel.getWorkBreakSetCount()
        totalSetCount = settingDataModel.getTotalSetCount()
        isTimerVibration = settingDataModel.getTimerVibration()
        isTimerAlert = settingDataModel.getTimerAlert()
    }

}
