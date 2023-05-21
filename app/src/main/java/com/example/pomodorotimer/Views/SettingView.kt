package com.example.pomodorotimer.Views

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pomodorotimer.ViewModels.SettingViewModel

@Composable
fun SettingView(viewModel: SettingViewModel) {
    val numberTextFieldErrAlert = remember { mutableStateOf(false) }
    val numberErrAlertText = remember { mutableStateOf("") }

    //入力値がエラーの場合はアラートを出す
    if (numberTextFieldErrAlert.value){
        AlertDialog(
            onDismissRequest = { numberTextFieldErrAlert.value = false },
            title = {
                Text(
                    text = "エラー",
                    fontSize = 20.sp
                )},
            text = {
                Text(
                    text = numberErrAlertText.value,
                    fontSize = 14.sp
                ) },
            confirmButton = {
                Button(onClick = { numberTextFieldErrAlert.value = false }) {
                    Text("OK")
                    Alignment.Center
                }
            }
        )
    }

    BoxWithConstraints() {
        val labelWidth = remember { maxWidth * 0.45f }
        val componentHeight = remember { 60.dp }

        Column(Modifier.padding(16.dp)) {
            NumberPickerView(
                label = "作業時間",
                initialValue = viewModel.workTime,
                range = 1..120,
                onValueChange = { viewModel.workTime = it },
                onValueError = { numberErrAlertText.value = it; numberTextFieldErrAlert.value = true },
                labelWidth = labelWidth,
                componentHeight = componentHeight
            )
            NumberPickerView(
                label = "休憩時間",
                initialValue = viewModel.shortBreakTime,
                range = 1..60,
                onValueChange = { viewModel.shortBreakTime = it },
                onValueError = { numberErrAlertText.value = it; numberTextFieldErrAlert.value = true },
                labelWidth = labelWidth,
                componentHeight = componentHeight
            )
            NumberPickerView(
                label = "休憩時間（長）",
                initialValue = viewModel.longBreakTime,
                range = 1..180,
                onValueChange = { viewModel.longBreakTime = it },
                onValueError = { numberErrAlertText.value = it; numberTextFieldErrAlert.value = true },
                labelWidth = labelWidth,
                componentHeight = componentHeight
            )
            NumberPickerView(
                label = "作業と休憩のセット数",
                initialValue = viewModel.workBreakSetCount,
                range = 1..20,
                onValueChange = { viewModel.workBreakSetCount = it },
                onValueError = { numberErrAlertText.value = it; numberTextFieldErrAlert.value = true },
                labelWidth = labelWidth,
                componentHeight = componentHeight
            )
            NumberPickerView(
                label = "全てのセット数",
                initialValue = viewModel.totalSetCount,
                range = 1..20,
                onValueChange = { viewModel.totalSetCount = it },
                onValueError = { numberErrAlertText.value = it; numberTextFieldErrAlert.value = true },
                labelWidth = labelWidth,
                componentHeight = componentHeight
            )
            SwitchView(
                label = "タイマー終了時のバイブ",
                isChecked = viewModel.isTimerVibration,
                onCheckedChange = { viewModel.setIsTimerVibration(it) },
                labelWidth = labelWidth,
                componentHeight = componentHeight
            )
            SwitchView(
                label = "タイマー終了時のアラート",
                isChecked = viewModel.isTimerAlert,
                onCheckedChange = { viewModel.setIsTimerAlert(it) },
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
    onValueError: (String) -> Unit,
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
            onValueChange = { input ->
                //変換した入力値：空の時はデフォルトとして1を設定
                val inputValue = if(input.isEmpty()) 1 else input.toInt()
                //数値かどうかのチェック結果：全削除している時は1がデフォルトで返るので何もしない
                val isIntValue = if(input.toIntOrNull() == null) input.isEmpty() else true
                //範囲内かどうかのチェック結果
                val hasNumberRange = inputValue >= range.first && inputValue <= range.last

                //範囲外・数値以外の時はアラート
                if (hasNumberRange && isIntValue){
                    textFieldValue.value = inputValue
                    onValueChange(inputValue)
                } else {
                    val errText = if (!isIntValue) {
                        //数値以外の場合
                        "数値を入力して下さい。"
                    } else {
                        //入力範囲外の場合
                        "入力した値が範囲外です。\n" +
                            "最小値：" + range.first + "\n" +
                            "最大値：" + range.last
                    }
                    onValueError(errText)
                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),  //数値のみ入力
            singleLine = true,
            modifier = Modifier
                .weight(1f)
                .heightIn(min = componentHeight, max = componentHeight),
            textStyle = TextStyle.Default.copy(textAlign = TextAlign.End)
        )

        IconButton(
            onClick = {
                if (textFieldValue.value + 1 <= range.last) {
                    textFieldValue.value = textFieldValue.value + 1
                    onValueChange(textFieldValue.value)
                }
            },
            enabled = textFieldValue.value + 1 <= range.last,
            modifier = Modifier
                .heightIn(min = componentHeight, max = componentHeight)
        ) {
            Icon(Icons.Default.Add, contentDescription = "増やす")
        }

        IconButton(
            onClick = {
                if (textFieldValue.value - 1 >= range.first) {
                    textFieldValue.value = textFieldValue.value - 1
                    onValueChange(textFieldValue.value)
                }
            },
            enabled = textFieldValue.value - 1 >= range.first,
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
        Switch(
            checked = isChecked,
            onCheckedChange = { isChecked ->
                onCheckedChange(isChecked)
            }
        )

    }
}

/**
 * 下線の定義
 */
private fun Modifier.bottomBorder(strokeWidth: Dp, color: Color) = composed(
    factory = {
        val density = LocalDensity.current
        val strokeWidthPx = density.run { strokeWidth.toPx() }

        Modifier.drawBehind {
            val width = size.width
            val height = size.height - strokeWidthPx / 2

            drawLine(
                color = color,
                start = Offset(x = 0f, y = height),
                end = Offset(x = width, y = height),
                strokeWidth = strokeWidthPx
            )
        }
    }
)