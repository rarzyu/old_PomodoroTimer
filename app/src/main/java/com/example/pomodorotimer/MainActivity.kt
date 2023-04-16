package com.example.pomodorotimer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.pomodorotimer.Models.SettingDataModel
import com.example.pomodorotimer.ViewModels.HelpViewModel
import com.example.pomodorotimer.ViewModels.PomodoroViewModel
import com.example.pomodorotimer.ViewModels.SettingViewModel
import com.example.pomodorotimer.ViewModels.ViewModelFactory
import com.example.pomodorotimer.Views.*



class MainActivity : ComponentActivity() {
    private val settingDataModel by lazy { SettingDataModel(applicationContext) }
    private val settingViewModel by viewModels<SettingViewModel> { ViewModelFactory(settingDataModel) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PomodoroApp(settingViewModel) {
                SettingView(settingViewModel)
            }
        }
    }
}

@Composable
fun PomodoroApp(settingViewModel: SettingViewModel,content: @Composable () -> Unit) {
    val (selectedTab, setSelectedTab) = remember { mutableStateOf(0) }
    Column(Modifier.fillMaxSize()) {
        //ヘッダー
        val title = when (selectedTab) {
            0 -> "タイマー"
            1 -> "設定"
            2 -> "ヘルプ"
            else -> ""
        }
        HeaderView(title)

        //メイン
        Box(Modifier.weight(1f)) {
            when (selectedTab) {
                0 -> PomodoroView(viewModel = PomodoroViewModel())
                1 -> SettingView(viewModel = settingViewModel)
                2 -> HelpView(viewModel = HelpViewModel())
            }
        }

        //フッター
        FooterView(selectedTab, setSelectedTab)

    }
}

@Preview(showBackground = true)
@Composable
fun MainActivityPreview() {
    val context = LocalContext.current
    // ダミーのSettingDataModelとSettingViewModelを作成
    val dummySettingDataModel = remember { SettingDataModel(context) }
    val dummySettingViewModel = remember { SettingViewModel(dummySettingDataModel) }

    // ダミーのSettingViewModelを渡す
    PomodoroApp(settingViewModel = dummySettingViewModel) {

    }
}