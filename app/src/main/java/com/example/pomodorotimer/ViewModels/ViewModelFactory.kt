package com.example.pomodorotimer.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pomodorotimer.Models.SettingDataModel

class ViewModelFactory(private val settingDataModel: SettingDataModel) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(SettingViewModel::class.java) -> {
                SettingViewModel(settingDataModel) as T
            }
            else -> throw IllegalArgumentException("ViewModelClassが見つかりません")
        }
    }
}
