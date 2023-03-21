package com.example.pomodorotimer.Views

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Timer
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController



@Composable
fun FooterView(navController: NavHostController,modifier: Modifier = Modifier) {
    BottomNavigation(modifier = modifier) {
        BottomNavigationItem(
            icon = { Icon(Icons.Filled.Timer, contentDescription = "タイマー") },
            selected = navController.currentDestination?.route == "timer",
            onClick = {
                navController.navigate("timer") {
                    launchSingleTop = true
                }
            }
        )

        BottomNavigationItem(
            icon = { Icon(Icons.Filled.Settings, contentDescription = "設定") },
            selected = navController.currentDestination?.route == "settings",
            onClick = {
                navController.navigate("settings") {
                    launchSingleTop = true
                }
            }
        )
    }
}
