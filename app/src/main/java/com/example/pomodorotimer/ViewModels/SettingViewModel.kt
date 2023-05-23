package com.example.pomodorotimer.ViewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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
    var workTime by mutableStateOf(settingDataModel.getWorkTime())
    var shortBreakTime by mutableStateOf(settingDataModel.getShortBreakTime())
    var longBreakTime by mutableStateOf(settingDataModel.getLongBreakTime())
    var workBreakSetCount by mutableStateOf(settingDataModel.getWorkBreakSetCount())
    var totalSetCount by mutableStateOf(settingDataModel.getTotalSetCount())
    var isTimerVibration by mutableStateOf(settingDataModel.getTimerVibration())
    var isTimerAlert by mutableStateOf(settingDataModel.getTimerAlert())

    //更新処理：set変数名はKotlinが自動生成するので使用不可
    fun updateWorkTime(value: Int) {
        workTime = value
        settingDataModel.setSettingInt(workTimeKey,value)
    }
    fun updateShortBreakTime(value: Int) {
        shortBreakTime = value
        settingDataModel.setSettingInt(shortBreakTimeKey,value)
    }
    fun updateLongBreakTime(value: Int) {
        longBreakTime = value
        settingDataModel.setSettingInt(longBreakTimeKey,value)
    }
    fun updateWorkBreakSetCount(value: Int) {
        workBreakSetCount = value
        settingDataModel.setSettingInt(workBreakSetCountKey,value)
    }
    fun updateTotalSetCount(value: Int) {
        totalSetCount = value
        settingDataModel.setSettingInt(totalSetCountKey,value)
    }
    fun updateIsTimerVibration(value: Boolean) {
        isTimerVibration = value
        settingDataModel.setSettingBoolean(isTimerVibrationKey,value)
    }
    fun updateIsTimerAlert(value: Boolean) {
        isTimerAlert = value
        settingDataModel.setSettingBoolean(isTimerAlertKey,value)
    }


}