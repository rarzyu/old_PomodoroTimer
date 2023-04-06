package com.example.pomodorotimer.Views

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
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
    Row(
        modifier = Modifier.padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label)

        Spacer(modifier = Modifier.width(8.dp))

        Card(
            modifier = Modifier
                .width(200.dp)
                .height(48.dp),
            elevation = 4.dp
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val textFieldValue = remember { mutableStateOf(initialValue) }

                TextField(
                    value = textFieldValue.value.toString(),
                    modifier = Modifier.weight(1f),
                    onValueChange = { textFieldValue.value = it.toInt() },
                    singleLine = true
                )

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(
                    onClick = {
                        if (textFieldValue.value > range.first) {
                            textFieldValue.value = textFieldValue.value + 1
                        }
                    },
                    enabled = textFieldValue.value > range.first
                ) {
                    Icon(Icons.Default.Add, contentDescription = "増やす")
                }

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(
                    onClick ={
                        if (textFieldValue.value > range.first) {
                            textFieldValue.value = textFieldValue.value - 1
                        }
                    },
                    enabled = textFieldValue.value > range.first
                ){
                    Icon(Icons.Default.Remove, contentDescription = "減らす")
                }
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