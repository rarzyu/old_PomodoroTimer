package com.example.pomodorotimer.Views

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.example.pomodorotimer.ViewModels.HeaderViewModel

@Composable
fun HeaderView(viewModel : HeaderViewModel) {
    TopAppBar {
        Text(
            text = viewModel.headerTitle,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
    }
}
