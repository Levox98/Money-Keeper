package com.moneykeeper.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.moneykeeper.ui.component.MainNavigationBar
import com.moneykeeper.ui.viewmodel.FundsViewModel

enum class Screen {
    TRANSACTIONS, STATISTICS, AI_ANALYSIS
}

@Composable
fun MainScreen(viewModel: FundsViewModel) {
    var currentScreen by remember { mutableStateOf(Screen.TRANSACTIONS) }

    Scaffold(
        bottomBar = {
            MainNavigationBar(
                currentScreen = currentScreen,
                onScreenSelected = { currentScreen = it }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(bottom = padding.calculateBottomPadding())) {
            when (currentScreen) {
                Screen.TRANSACTIONS -> FundsScreen(viewModel = viewModel)
                Screen.STATISTICS -> StatisticsScreen(viewModel = viewModel)
                Screen.AI_ANALYSIS -> AIScreen(viewModel = viewModel)
            }
        }
    }
}
