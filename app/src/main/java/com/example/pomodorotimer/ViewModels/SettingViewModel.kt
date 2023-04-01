package com.example.pomodorotimer.ViewModels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class SettingViewModel: ViewModel() {
    val workTime = mutableStateOf(25)
    val shortBreakTime = mutableStateOf(5)
    val longBreakTime = mutableStateOf(30)
    val numberOfWorkBreakSets = mutableStateOf(4)
    val numberOfAllSets = mutableStateOf(3)
    val timerVibration = mutableStateOf(true)
    val timerAlert = mutableStateOf(true)
}