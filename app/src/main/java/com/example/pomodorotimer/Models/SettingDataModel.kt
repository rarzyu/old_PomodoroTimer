package com.example.pomodorotimer.Models

import android.content.Context

class SettingDataModel(private val context: Context) {
    private val sharedPreferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE)

    //作業時間
    fun getWorkTime(): Int = sharedPreferences.getInt("workTime", 25)

    //休憩時間
    fun getShortBreakTime(): Int = sharedPreferences.getInt("shortBreakTime",5)

    //休憩時間（長）
    fun getLongBreakTime():Int = sharedPreferences.getInt("longBreakTime",30)

    //作業と休憩のセット数
    fun getNumberOfWorkBreakSets():Int = sharedPreferences.getInt("numberOfWorkBreakSets",4)

    //全てのセット数
    fun getNumberOfAllSets():Int = sharedPreferences.getInt("numberOfAllSets",3)

    //タイマー終了時のバイブ
    fun getTimerVibration():Boolean = sharedPreferences.getBoolean("timerVibration",true)

    //タイマー終了時のアラート
    fun getTimerAlert():Boolean = sharedPreferences.getBoolean("timerAlert",true)

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