package com.rarzyu.pomodorotimer.Models

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * 設定のデータ管理
 * SharedPreferencesとやり取りする
 */
class SettingDataModel(private val context: Context) {
    private val sharedPreferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE)

    //各設定のKey値
    val workTimeKey = "workTime"
    val shortBreakTimeKey = "shortBreakTime"
    val longBreakTimeKey = "longBreakTime"
    val workBreakSetCountKey = "workBreakSetCount"
    val totalSetCountKey = "totalSetCount"
    val isTimerVibrationKey = "isTimerVibration"
    val isTimerAlertKey = "isTimerAlert"

    //監視可能な値
    private val _workTime = MutableStateFlow(getWorkTime())
    val workTime: StateFlow<Int> = _workTime

    private val _shortBreakTime = MutableStateFlow(getShortBreakTime())
    val shortBreakTime: StateFlow<Int> = _shortBreakTime

    private val _longBreakTime = MutableStateFlow(getLongBreakTime())
    val longBreakTime: StateFlow<Int> = _longBreakTime

    private val _workBreakSetCount = MutableStateFlow(getWorkBreakSetCount())
    val workBreakSetCount: StateFlow<Int> = _workBreakSetCount

    private val _totalSetCount = MutableStateFlow(getTotalSetCount())
    val totalSetCount: StateFlow<Int> = _totalSetCount

    private val _isTimerVibration = MutableStateFlow(getTimerVibration())
    val isTimerVibration: StateFlow<Boolean> = _isTimerVibration

    private val _isTimerAlert = MutableStateFlow(getTimerAlert())
    val isTimerAlert: StateFlow<Boolean> = _isTimerAlert

    //アプリ内に保存したデータから取得する
    fun getWorkTime():Int {
      return sharedPreferences.getInt(workTimeKey, 25)
    }

    fun getShortBreakTime():Int {
        return sharedPreferences.getInt(shortBreakTimeKey,5)
    }

    fun getLongBreakTime():Int {
        return sharedPreferences.getInt(longBreakTimeKey,30)
    }

    fun getWorkBreakSetCount():Int {
        return sharedPreferences.getInt(workBreakSetCountKey,4)
    }

    fun getTotalSetCount():Int {
        return sharedPreferences.getInt(totalSetCountKey,3)
    }

    fun getTimerVibration():Boolean {
        return sharedPreferences.getBoolean(isTimerVibrationKey,true)
    }

    fun getTimerAlert():Boolean {
        return sharedPreferences.getBoolean(isTimerAlertKey,true)
    }

    /**
     * SharedPreferencesに保存する処理：Int型
     * @param key：保存先のKey値
     * @param value：保存する値
     */
    fun setSettingInt(key:String, value: Int){
        sharedPreferences.edit().putInt(key,value).apply()
    }

    /**
     * SharedPreferencesに保存する処理：Boolean型
     * @param key：保存先のKey値
     * @param value：保存する値
     */
    fun setSettingBoolean(key:String,value:Boolean){
        sharedPreferences.edit().putBoolean(key,value).apply()
    }
}