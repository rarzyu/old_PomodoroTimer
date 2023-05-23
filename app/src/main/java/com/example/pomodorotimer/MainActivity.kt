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
import com.example.pomodorotimer.Common.DummyNotificationController
import com.example.pomodorotimer.Common.NotificationController
import com.example.pomodorotimer.Models.SettingDataModel
import com.example.pomodorotimer.ViewModels.PomodoroTimerViewModel
import com.example.pomodorotimer.ViewModels.SettingViewModel
import com.example.pomodorotimer.ViewModels.ViewModelFactory
import com.example.pomodorotimer.Views.FooterView
import com.example.pomodorotimer.Views.HeaderView
import com.example.pomodorotimer.Views.PomodoroView
import com.example.pomodorotimer.Views.SettingView

class MainActivity : ComponentActivity() {
    private val settingDataModel by lazy { SettingDataModel(applicationContext) }
    private val viewModelFactory by lazy { ViewModelFactory.getInstance(settingDataModel) }
    private val settingViewModel by viewModels<SettingViewModel> { viewModelFactory }
    private lateinit var notificationController: NotificationController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // ContextとしてActivityを渡すのではなく、Applicationを渡す
        notificationController = NotificationController(application)

        setContent {
            PomodoroApp(settingViewModel,settingDataModel,notificationController) {
                SettingView(settingViewModel)
                PomodoroTimerViewModel(settingDataModel,notificationController)
            }
        }
    }
}

@Composable
fun PomodoroApp(settingViewModel: SettingViewModel,settingDataModel: SettingDataModel,notificationController: NotificationController,content: @Composable () -> Unit) {
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
                0 -> PomodoroView(viewModel = PomodoroTimerViewModel(settingDataModel, notificationController))
                1 -> SettingView(viewModel = settingViewModel)
//                2 -> HelpView(viewModel = HelpViewModel())
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
    val settingDataModel = remember { SettingDataModel(context) }
    val viewModelFactory = ViewModelFactory.getInstance(settingDataModel)
    val settingViewModel = viewModelFactory.create(SettingViewModel::class.java)
    val dummyNotificationController = DummyNotificationController()

    PomodoroApp(settingViewModel,settingDataModel,dummyNotificationController) {
        SettingView(settingViewModel)
        PomodoroTimerViewModel(settingDataModel,dummyNotificationController)
    }
}