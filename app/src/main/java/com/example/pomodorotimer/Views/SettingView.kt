package com.example.pomodorotimer.Views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.material.Slider
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.text.input.KeyboardType
import com.example.pomodorotimer.ViewModels.SettingViewModel

@Composable
fun SettingView(viewModel: SettingViewModel) {
    Column(Modifier.padding(16.dp)) {
        val context = LocalContext.current

        NumberPickerView(
            label = "作業時間",
            value = viewModel.workTime.value,
            range = 1..120,
            onValueChange = { viewModel.workTime.value = it },
            steps = 180
        )
        NumberPickerView(
            label = "休憩時間",
            value = viewModel.shortBreakTime.value,
            range = 1..60,
            onValueChange = { viewModel.shortBreakTime.value = it },
            steps = 180
        )
        NumberPickerView(
            label = "休憩時間（長）",
            value = viewModel.longBreakTime.value,
            range = 1..180,
            onValueChange = { viewModel.longBreakTime.value = it },
            steps = 180
        )
        NumberPickerView(
            label = "作業と休憩のセット数",
            value = viewModel.numberOfWorkBreakSets.value,
            range = 1..20,
            onValueChange = { viewModel.numberOfWorkBreakSets.value = it },
            steps = 180
        )
        NumberPickerView(
            label = "全てのセット数",
            value = viewModel.numberOfAllSets.value,
            range = 1..20,
            onValueChange = { viewModel.numberOfAllSets.value = it },
            steps = 180
        )
        SwitchView("タイマー終了時のバイブ", viewModel.timerVibration.value) { viewModel.timerVibration.value = it }
        SwitchView("タイマー終了時のアラート", viewModel.timerAlert.value) { viewModel.timerAlert.value = it }
    }
}

@Composable
fun NumberPickerView(
    label: String,
    value: Int,
    range: IntRange = 0..100,
    onValueChange: (Int) -> Unit,
    steps: Int = 10
) {
    var textFieldValue by remember { mutableStateOf(value.toString()) }

    Column {
        Row() {
            Text(text = label)

            TextField(
                value = textFieldValue,
                onValueChange = { newValue ->
                    textFieldValue = newValue
                    newValue.toIntOrNull()?.let {
                        if (it in range) {
                            onValueChange(it)
                        }
                    }
                },
                keyboardType = KeyboardType.Number,
                label = { Text("Number Picker") }
            )

            Slider(
                value = value.toFloat(),
                onValueChange = { newValue ->
                    onValueChange(newValue.toInt())
                    textFieldValue = newValue.toInt().toString()
                },
                valueRange = range.first.toFloat()..range.last.toFloat(),
                steps = steps
            )
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