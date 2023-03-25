package com.example.pomodorotimer.Views

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Timer
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pomodorotimer.HelpView
import com.example.pomodorotimer.SettingsView
import com.example.pomodorotimer.TimerView
import com.example.pomodorotimer.ViewModels.FooterViewModel
import com.example.pomodorotimer.ViewModels.PomodoroViewModel

@Composable
fun FooterView(viewModel: FooterViewModel) {
    val navController = rememberNavController()
    val pomodoroViewModel : PomodoroViewModel = viewModel()

    Box(Modifier.fillMaxWidth()) {
        NavHost(navController = navController, startDestination = "timer") {
            composable("timer") {
                TimerView(pomodoroViewModel)
            }
            composable("settings") {
                SettingsView(pomodoroViewModel)
            }
            composable("help") {
                HelpView(pomodoroViewModel)
            }
        }
    }

        BottomNavigation{
//            val selectedIndex = when (navController.currentDestination?.route) {
//                "timer" -> 0
//                "settings" -> 1
//                "help" -> 2
//                else -> 0
//            }

            BottomNavigationItem(
                icon = { Icon(Icons.Filled.Timer, contentDescription = "タイマー") },
                selected = viewModel.selectedTab == 0,
                onClick = {
                    navController.navigate("timer") {
                        launchSingleTop = true
                    }
                }
            )

            BottomNavigationItem(
                icon = { Icon(Icons.Filled.Settings, contentDescription = "設定") },
                selected = viewModel.selectedTab == 1,
                onClick = {
                    navController.navigate("settings") {
                        launchSingleTop = true
                    }
                }
            )

            BottomNavigationItem(
                icon = { Icon(Icons.Filled.HelpOutline, contentDescription = "ヘルプ") },
                selected = viewModel.selectedTab == 2,
                onClick = {
                    navController.navigate("help") {
                        launchSingleTop = true
                    }
                }
            )
        }
}
