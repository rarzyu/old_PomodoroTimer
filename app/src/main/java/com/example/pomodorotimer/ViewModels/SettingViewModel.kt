package com.example.pomodorotimer.ViewModels

import androidx.lifecycle.ViewModel
import com.example.pomodorotimer.Models.SettingDataModel

class SettingViewModel(private val settingDataModel: SettingDataModel) : ViewModel(){

    //各設定のKey値
    val workTimeKey = "workTime"
    val shortBreakTimeKey = "shortBreakTime"
    val longBreakTimeKey = "longBreakTime"
    val numberOfWorkBreakSetsKey = "numberOfWorkBreakSets"
    val numberOfAllSetsKey = "numberOfAllSets"
    val timerVibrationKey = "timerVibration"
    val timerAlertKey = "timerAlert"

    //各設定の値
    var workTime = settingDataModel.getWorkTime()
    var shortBreakTime = settingDataModel.getShortBreakTime()
    var longBreakTime = settingDataModel.getLongBreakTime()
    var numberOfWorkBreakSets = settingDataModel.getNumberOfWorkBreakSets()
    var numberOfAllSets = settingDataModel.getNumberOfAllSets()
    var timerVibration = settingDataModel.getTimerVibration()
    var timerAlert = settingDataModel.getTimerAlert()

    //保存処理
    fun saveSetting(saveData:Map<String,Any>){
        saveData.forEach { (key, value) ->
            when(value::class.simpleName) {
                "Int" -> settingDataModel.setSettingInt(key,value as Int)
                "Boolean" -> settingDataModel.setSettingBoolean(key, value as Boolean)
            }
        }
    }
}