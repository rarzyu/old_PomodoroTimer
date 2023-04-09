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
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.pomodorotimer.ViewModels.SettingViewModel

@Composable
fun SettingView(viewModel: SettingViewModel) {

    BoxWithConstraints() {
        val labelWidth = remember { maxWidth * 0.45f }
        val componentHeight = remember { 60.dp }

        Column(Modifier.padding(16.dp)) {
            val context = LocalContext.current

            NumberPickerView(
                label = "作業時間",
                initialValue = viewModel.workTime.value,
                range = 1..120,
                onValueChange = { viewModel.workTime.value = it },
                labelWidth = labelWidth,
                componentHeight = componentHeight
            )
            NumberPickerView(
                label = "休憩時間",
                initialValue = viewModel.shortBreakTime.value,
                range = 1..60,
                onValueChange = { viewModel.shortBreakTime.value = it },
                labelWidth = labelWidth,
                componentHeight = componentHeight
            )
            NumberPickerView(
                label = "休憩時間（長）",
                initialValue = viewModel.longBreakTime.value,
                range = 1..180,
                onValueChange = { viewModel.longBreakTime.value = it },
                labelWidth = labelWidth,
                componentHeight = componentHeight
            )
            NumberPickerView(
                label = "作業と休憩のセット数",
                initialValue = viewModel.numberOfWorkBreakSets.value,
                range = 1..20,
                onValueChange = { viewModel.numberOfWorkBreakSets.value = it },
                labelWidth = labelWidth,
                componentHeight = componentHeight
            )
            NumberPickerView(
                label = "全てのセット数",
                initialValue = viewModel.numberOfAllSets.value,
                range = 1..20,
                onValueChange = { viewModel.numberOfAllSets.value = it },
                labelWidth = labelWidth,
                componentHeight = componentHeight
            )
            SwitchView(
                label = "タイマー終了時のバイブ",
                isChecked = viewModel.timerVibration.value,
                onCheckedChange = { viewModel.timerVibration.value = it },
                labelWidth = labelWidth,
                componentHeight = componentHeight
            )
            SwitchView(
                label = "タイマー終了時のアラート",
                isChecked = viewModel.timerAlert.value,
                onCheckedChange = { viewModel.timerAlert.value = it },
                labelWidth = labelWidth,
                componentHeight = componentHeight
            )
        }
    }
}

@Composable
fun NumberPickerView(
    label: String,
    initialValue: Int,
    range: IntRange,
    onValueChange: (Int) -> Unit,
    labelWidth: Dp,
    componentHeight: Dp
) {
    val textFieldValue = remember { mutableStateOf(initialValue) }

    Row(
        modifier = Modifier
            .padding(bottom = 8.dp)
            .bottomBorder(2.dp, Color.LightGray),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.width(5.dp))

        Text(
            text = label,
            modifier = Modifier
                .width(labelWidth)
        )

        TextField(
            value = textFieldValue.value.toString(),
            onValueChange = { textFieldValue.value = it.toInt() },
            singleLine = true,
            modifier = Modifier
                .weight(1f)
                .heightIn(min = componentHeight, max = componentHeight),
            textStyle = TextStyle.Default.copy(textAlign = TextAlign.End)
        )


        IconButton(
            onClick = {
                if (textFieldValue.value > range.first) {
                    textFieldValue.value = textFieldValue.value + 1
                }
            },
            enabled = textFieldValue.value > range.first,
            modifier = Modifier
                .heightIn(min = componentHeight, max = componentHeight)
        ) {
            Icon(Icons.Default.Add, contentDescription = "増やす")
        }

        IconButton(
            onClick = {
                if (textFieldValue.value > range.first) {
                    textFieldValue.value = textFieldValue.value - 1
                }
            },
            enabled = textFieldValue.value > range.first,
            modifier = Modifier
                .heightIn(min = componentHeight, max = componentHeight)
        ) {
            Icon(Icons.Default.Remove, contentDescription = "減らす")
        }
    }
}

@Composable
fun SwitchView(
    label: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    labelWidth: Dp,
    componentHeight: Dp
) {
    Row(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .height(componentHeight)
            .bottomBorder(2.dp, Color.LightGray)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.width(5.dp))
        Text(
            text = label,
            modifier = Modifier.width(labelWidth)
        )
        Spacer(modifier = Modifier.weight(1f))
        Switch(checked = isChecked, onCheckedChange = onCheckedChange)
    }
}

@Preview(showBackground = true)
@Composable
fun previewSettingView() {
    SettingView(viewModel = SettingViewModel())
}

fun Modifier.bottomBorder(strokeWidth: Dp, color: Color) = composed(
    factory = {
        val density = LocalDensity.current
        val strokeWidthPx = density.run { strokeWidth.toPx() }

        Modifier.drawBehind {
            val width = size.width
            val height = size.height - strokeWidthPx/2

            drawLine(
                color = color,
                start = Offset(x = 0f, y = height),
                end = Offset(x = width , y = height),
                strokeWidth = strokeWidthPx
            )
        }
    }
)