package com.moneykeeper.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.moneykeeper.ui.component.MainNavigationBar
import com.moneykeeper.ui.viewmodel.AddTransactionViewModel
import com.moneykeeper.ui.viewmodel.FundsViewModel

enum class Screen {
    TRANSACTIONS, STATISTICS, AI_ANALYSIS
}

@Composable
fun MainScreen(
    viewModel: FundsViewModel,
    addTransactionViewModel: AddTransactionViewModel
) {
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
                Screen.TRANSACTIONS -> {
                    val navController = rememberNavController()
                    // TODO: do navigation correctly
                    NavHost(
                        navController = navController,
                        startDestination = "funds"
                    ) {
                        composable("funds") {
                            FundsScreen(viewModel = viewModel) {
                                println("Add transaction button clicked")
                                navController.navigate("addTransaction")
                            }
                        }

                        composable("addTransaction") {
                            AddTransactionScreen(viewmodel = addTransactionViewModel)
                        }
                    }
                }
                Screen.STATISTICS -> StatisticsScreen(viewModel = viewModel)
                Screen.AI_ANALYSIS -> AIScreen(viewModel = viewModel)
            }
        }
    }
}
