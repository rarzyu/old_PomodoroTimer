package com.rarzyu.pomodorotimer.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rarzyu.pomodorotimer.Models.SettingDataModel

/**
 * シングルトンクラス
 * SettingDataModelとSettingViewModelの架け橋
 */
class ViewModelFactory private constructor(private val settingDataModel: SettingDataModel) :
    ViewModelProvider.Factory {

    companion object {
        private var instance: ViewModelFactory? = null

        fun getInstance(settingDataModel: SettingDataModel): ViewModelFactory {
            if (instance == null) {
                instance = ViewModelFactory(settingDataModel)
            }
            return instance!!
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(SettingViewModel::class.java) -> {
                SettingViewModel(settingDataModel) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}



