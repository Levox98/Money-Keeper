package com.moneykeeper

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.moneykeeper.data.database.AppDatabase
import com.moneykeeper.data.repository.AddTransactionRepositoryImpl
import com.moneykeeper.data.repository.FundsRepositoryImpl
import com.moneykeeper.domain.ai.FinanceAIAnalyzer
import com.moneykeeper.domain.usecase.AnalyzeFinancesUseCase
import com.moneykeeper.ui.screen.MainScreen
import com.moneykeeper.ui.theme.MoneyKeeperTheme
import com.moneykeeper.ui.viewmodel.AddTransactionViewModel
import com.moneykeeper.ui.viewmodel.FundsViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: get rid of this nonsense (use hilt or smth)
        // Simple manual DI for the sake of the task
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "money-keeper-db"
        ).build()

        val repository = FundsRepositoryImpl(
            transactionDao = db.transactionDao(),
            categoryDao = db.categoryDao(),
            context = applicationContext
        )

        val addTransactionRepository = AddTransactionRepositoryImpl(
            transactionDao = db.transactionDao(),
            categoryDao = db.categoryDao()
        )

        val aiAnalyzer = FinanceAIAnalyzer()
        val analyzeUseCase = AnalyzeFinancesUseCase(repository, aiAnalyzer)

        val viewModelFactory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return FundsViewModel(repository, analyzeUseCase) as T
            }
        }

        val addTransactionViewModelFactory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return AddTransactionViewModel(addTransactionRepository) as T
            }
        }

        enableEdgeToEdge()
        setContent {
            val viewModel = remember {
                ViewModelProvider(this, viewModelFactory)[FundsViewModel::class.java]
            }

            val addTransactionViewModel = remember {
                ViewModelProvider(
                    this,
                    addTransactionViewModelFactory
                )[AddTransactionViewModel::class.java]
            }

            MoneyKeeperTheme {
                MainScreen(viewModel = viewModel, addTransactionViewModel = addTransactionViewModel)
            }
        }
    }
}
