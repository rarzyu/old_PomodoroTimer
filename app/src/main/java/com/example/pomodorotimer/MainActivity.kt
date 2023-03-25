package com.example.pomodorotimer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pomodorotimer.ViewModels.FooterViewModel
import com.example.pomodorotimer.ViewModels.HeaderViewModel
import com.example.pomodorotimer.ViewModels.PomodoroViewModel
import com.example.pomodorotimer.Views.FooterView
import com.example.pomodorotimer.Views.HeaderView
import com.example.pomodorotimer.Views.PomodoroScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PomodoroApp {

            }
        }
    }
}

@Composable
fun PomodoroApp(content: @Composable () -> Unit) {
    val (selectedTab) = remember { mutableStateOf(0) }
    Column(Modifier.fillMaxSize()) {
        //ヘッダー
        val headerViewModel : HeaderViewModel = viewModel()
        val title = when (selectedTab) {
            0 -> "タイマー"
            1 -> "設定"
            2 -> "ヘルプ"
            else -> ""
        }
        headerViewModel.headerTitle = title
        HeaderView(viewModel = headerViewModel)

        //メイン
        Box(Modifier.weight(1f)) {
            val viewModel: PomodoroViewModel = viewModel()
            PomodoroScreen(viewModel = viewModel)
        }

        //フッター
        val footerViewModel : FooterViewModel = viewModel()
        footerViewModel.selectedTab = selectedTab
        FooterView(viewModel = footerViewModel)
    }
}

@Preview(showBackground = true)
@Composable
fun MainActivityPreview() {
    PomodoroApp {

    }
}

@Composable
fun TimerView(viewModel: PomodoroViewModel) {
}

@Composable
fun SettingsView(viewModel: PomodoroViewModel) {
}

@Composable
fun HelpView(viewModel: PomodoroViewModel) {
}