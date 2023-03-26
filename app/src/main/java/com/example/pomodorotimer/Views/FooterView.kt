package com.example.pomodorotimer.Views

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Timer
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.pomodorotimer.ViewModels.PomodoroViewModel

@Composable
fun FooterView(selectedTab: Int, setSelectedTab: (Int) -> Unit) {
    val navController = rememberNavController()
    val pomodoroViewModel : PomodoroViewModel = viewModel()

        BottomNavigation{

            BottomNavigationItem(
                icon = { Icon(Icons.Filled.Timer, contentDescription = null) },
                selected = selectedTab == 0,
                label = { Text("タイマー") },
                onClick = {
                    setSelectedTab(0)
                }
            )

            BottomNavigationItem(
                icon = { Icon(Icons.Filled.Settings, contentDescription = null) },
                selected = selectedTab == 1,
                label = { Text("設定") },
                onClick = {
                    setSelectedTab(1)
                }
            )

            BottomNavigationItem(
                icon = { Icon(Icons.Filled.HelpOutline, contentDescription = null) },
                selected = selectedTab == 2,
                label = { Text("ヘルプ") },
                onClick = {
                    setSelectedTab(2)
                }
            )
        }
}
