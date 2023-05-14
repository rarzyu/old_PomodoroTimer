package com.example.pomodorotimer.Models

import android.content.Context

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

    //作業時間
    fun getWorkTime():Int {
      return sharedPreferences.getInt(workTimeKey, 25)
    }

    //休憩時間
    fun getShortBreakTime():Int {
        return sharedPreferences.getInt(shortBreakTimeKey,5)
    }

    //休憩時間（長）
    fun getLongBreakTime():Int {
        return sharedPreferences.getInt(longBreakTimeKey,30)
    }

    //作業と休憩のセット数
    fun getWorkBreakSetCount():Int {
        return sharedPreferences.getInt(workBreakSetCountKey,4)
    }

    //全てのセット数
    fun getTotalSetCount():Int {
        return sharedPreferences.getInt(totalSetCountKey,3)
    }

    //タイマー終了時のバイブ
    fun getTimerVibration():Boolean {
        return sharedPreferences.getBoolean(isTimerVibrationKey,true)
    }

    //タイマー終了時のアラート
    fun getTimerAlert():Boolean {
        return sharedPreferences.getBoolean(isTimerAlertKey,true)
    }

    /**
     * SharedPreferencesに保存する処理：Int型
     * @param key：保存先のKey値
     * @param value：保存する値
     */
    fun setSettingInt(key:String,value:Int){
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