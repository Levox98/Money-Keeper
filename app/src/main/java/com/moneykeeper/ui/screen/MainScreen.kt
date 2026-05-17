package com.moneykeeper.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.moneykeeper.R
import com.moneykeeper.ui.viewmodel.FundsViewModel

enum class Screen {
    TRANSACTIONS, STATISTICS
}

@Composable
fun MainScreen(viewModel: FundsViewModel) {
    var currentScreen by remember { mutableStateOf(Screen.TRANSACTIONS) }

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = currentScreen == Screen.TRANSACTIONS,
                    onClick = { currentScreen = Screen.TRANSACTIONS },
                    icon = { Icon(Icons.Default.Home, contentDescription = null) },
                    label = { Text(stringResource(R.string.transactions)) }
                )
                NavigationBarItem(
                    selected = currentScreen == Screen.STATISTICS,
                    onClick = { currentScreen = Screen.STATISTICS },
                    icon = { Icon(Icons.Default.Info, contentDescription = null) },
                    label = { Text(stringResource(R.string.statistics)) }
                )
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(bottom = padding.calculateBottomPadding())) {
            when (currentScreen) {
                Screen.TRANSACTIONS -> FundsScreen(viewModel = viewModel)
                Screen.STATISTICS -> StatisticsScreen(viewModel = viewModel)
            }
        }
    }
}
