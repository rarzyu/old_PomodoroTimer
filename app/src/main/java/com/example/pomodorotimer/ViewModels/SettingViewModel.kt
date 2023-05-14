package com.example.pomodorotimer.ViewModels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.pomodorotimer.Models.SettingDataModel

/**
 * 設定ビューのViewModel
 */
class SettingViewModel(private val settingDataModel: SettingDataModel) : ViewModel() {

    private val workTimeKey = settingDataModel.workTimeKey
    private val shortBreakTimeKey = settingDataModel.shortBreakTimeKey
    private val longBreakTimeKey = settingDataModel.longBreakTimeKey
    private val workBreakSetCountKey = settingDataModel.workBreakSetCountKey
    private val totalSetCountKey = settingDataModel.totalSetCountKey
    private val isTimerVibrationKey = settingDataModel.isTimerVibrationKey
    private val isTimerAlertKey = settingDataModel.isTimerAlertKey

    //各設定の値
    var workTime = settingDataModel.getWorkTime()
        set(value) {
            field = value
            settingDataModel.setSettingInt(workTimeKey, value)
        }
    var shortBreakTime = settingDataModel.getShortBreakTime()
        set(value) {
            field = value
            settingDataModel.setSettingInt(shortBreakTimeKey, value)
        }
    var longBreakTime = settingDataModel.getLongBreakTime()
        set(value) {
            field = value
            settingDataModel.setSettingInt(longBreakTimeKey, value)
        }
    var workBreakSetCount = settingDataModel.getWorkBreakSetCount()
        set(value) {
            field = value
            settingDataModel.setSettingInt(workBreakSetCountKey, value)
        }
    var totalSetCount = settingDataModel.getTotalSetCount()
        set(value) {
            field = value
            settingDataModel.setSettingInt(totalSetCountKey, value)
        }
    private var _isTimerVibration = mutableStateOf(settingDataModel.getTimerVibration())
    val isTimerVibration: Boolean
        get() = _isTimerVibration.value

    fun setIsTimerVibration(value: Boolean) {
        _isTimerVibration.value = value
        settingDataModel.setSettingBoolean(isTimerVibrationKey, value)
    }

    private var _isTimerAlert = mutableStateOf(settingDataModel.getTimerAlert())
    val isTimerAlert: Boolean
        get() = _isTimerAlert.value

    fun setIsTimerAlert(value: Boolean) {
        _isTimerAlert.value = value
        settingDataModel.setSettingBoolean(isTimerAlertKey, value)
    }

}