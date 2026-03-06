package com.fanyicharllson.gradecalculator

import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.fanyicharllson.gradecalculator.ui.theme.AppTheme

fun main() = application {
    val windowState = rememberWindowState(width = 900.dp, height = 680.dp)
    Window(
        onCloseRequest = ::exitApplication,
        title = "Grade Calculator — ICT University",
        state = windowState
    ) {
        AppTheme {
            App()
        }
    }
}