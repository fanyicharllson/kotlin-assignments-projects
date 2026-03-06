package com.fanyicharllson.gradecalculator

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fanyicharllson.gradecalculator.model.AppState
import com.fanyicharllson.gradecalculator.ui.HomeScreen
import com.fanyicharllson.gradecalculator.ui.PreviewScreen
import com.fanyicharllson.gradecalculator.ui.ResultScreen
import com.fanyicharllson.gradecalculator.ui.theme.*
import com.fanyicharllson.gradecalculator.viewmodel.AppViewModel


/**
 * ## AppContent
 * Reads [AppState] from ViewModel and renders the correct screen.
 * Animated transitions between screens via [AnimatedContent].
 */
@Composable
@Preview
fun App(viewModel: AppViewModel = viewModel { AppViewModel() }) {
    val state by viewModel.state.collectAsState()

    AnimatedContent(
        targetState = state,
        transitionSpec = {
            fadeIn(initialAlpha = 0f) + slideInHorizontally { it / 20 } togetherWith
                    fadeOut() + slideOutHorizontally { -it / 20 }
        }
    ) { currentState ->
        // when on sealed AppState — compiler enforces all cases handled
        when (currentState) {
            is AppState.Home -> HomeScreen(viewModel)
            is AppState.Preview -> PreviewScreen(
                file = currentState.file,
                students = currentState.students,
                viewModel = viewModel
            )

            is AppState.Calculating -> LoadingScreen(currentState.message)
            is AppState.Results -> ResultScreen(
                sourceFile = currentState.file,
                results = currentState.results,
                viewModel = viewModel
            )

            is AppState.Error -> ErrorScreen(
                message = currentState.message,
                onRetry = { viewModel.onReset() }
            )
        }
    }
}


@Composable
private fun LoadingScreen(message: String = "Calculating grades...") {
    Box(
        modifier = Modifier.fillMaxSize().background(DarkBg),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.widthIn(max = 380.dp)
        ) {
            CircularProgressIndicator(color = BluePrimary, strokeWidth = 3.dp)
            Spacer(Modifier.height(20.dp))
            // Split on newline so multiline messages display properly
            message.split("\n").forEach { line ->
                Text(
                    line,
                    color = if (line == message.split("\n").first()) OffWhite else GrayMid,
                    fontSize = if (line == message.split("\n").first()) 15.sp else 13.sp,
                    fontWeight = if (line == message.split("\n").first()) FontWeight.SemiBold else FontWeight.Normal
                )
            }
        }
    }
}

@Composable
private fun ErrorScreen(message: String, onRetry: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize().background(DarkBg),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.widthIn(max = 420.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(70.dp)
                    .background(AccentRed.copy(alpha = 0.15f), RoundedCornerShape(20.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text("!", fontSize = 30.sp, fontWeight = FontWeight.Bold, color = AccentRed)
            }
            Spacer(Modifier.height(20.dp))
            Text("Something went wrong", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = OffWhite)
            Spacer(Modifier.height(8.dp))
            Text(message, color = GrayMid, fontSize = 14.sp)
            Spacer(Modifier.height(28.dp))
            androidx.compose.material3.Button(
                onClick = onRetry,
                shape = RoundedCornerShape(12.dp),
                colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                    containerColor = BluePrimary
                )
            ) {
                Text("Try Again", color = DarkBg, fontWeight = FontWeight.Bold)
            }
        }
    }
}