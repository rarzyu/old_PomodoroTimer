package com.example.pomodorotimer.Views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.pomodorotimer.ViewModels.SettingViewModel


@Composable
fun SettingView(viewModel: SettingViewModel) {
    Column(Modifier.padding(16.dp)) {
        val context = LocalContext.current

        NumberPickerView(
            label = "作業時間",
            initialValue = viewModel.workTime.value,
            range = 1..120,
            onValueChange = { viewModel.workTime.value = it }
        )
        NumberPickerView(
            label = "休憩時間",
            initialValue = viewModel.shortBreakTime.value,
            range = 1..60,
            onValueChange = { viewModel.shortBreakTime.value = it }
        )
        NumberPickerView(
            label = "休憩時間（長）",
            initialValue = viewModel.longBreakTime.value,
            range = 1..180,
            onValueChange = { viewModel.longBreakTime.value = it }
        )
        NumberPickerView(
            label = "作業と休憩のセット数",
            initialValue = viewModel.numberOfWorkBreakSets.value,
            range = 1..20,
            onValueChange = { viewModel.numberOfWorkBreakSets.value = it }
        )
        NumberPickerView(
            label = "全てのセット数",
            initialValue = viewModel.numberOfAllSets.value,
            range = 1..20,
            onValueChange = { viewModel.numberOfAllSets.value = it }
        )
        SwitchView("タイマー終了時のバイブ", viewModel.timerVibration.value) { viewModel.timerVibration.value = it }
        SwitchView("タイマー終了時のアラート", viewModel.timerAlert.value) { viewModel.timerAlert.value = it }
    }
}

@Composable
fun NumberPickerView(label: String, initialValue: Int, range: IntRange, onValueChange: (Int) -> Unit) {
    var pickerValue by remember { mutableStateOf(initialValue) }

    Column {
        Text(label)

        Row {
            Button(
                onClick = { if (pickerValue > range.first) pickerValue -= 1 },
                enabled = pickerValue > range.first
            ) {
                Text("-")
            }

            Text(pickerValue.toString())

            Button(
                onClick = { if (pickerValue < range.last) pickerValue += 1 },
                enabled = pickerValue < range.last
            ) {
                Text("+")
            }
        }
    }
}


@Composable
fun SwitchView(label: String, isChecked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Column {
        Text(label)
        Switch(checked = isChecked, onCheckedChange = onCheckedChange)
    }
}