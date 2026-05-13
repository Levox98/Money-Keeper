package com.moneykeeper

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.moneykeeper.core.data.database.AppDatabase
import com.moneykeeper.core.data.repository.FundsRepositoryImpl
import com.moneykeeper.feature.transactions.screen.FundsScreen
import com.moneykeeper.core.ui.theme.MoneyKeeperTheme
import com.moneykeeper.feature.transactions.viewmodel.FundsViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Simple manual DI
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "money-keeper-db"
        ).build()
        
        val repository = FundsRepositoryImpl(
            transactionDao = db.transactionDao(),
            categoryDao = db.categoryDao(),
            context = applicationContext
        )
        
        val viewModelFactory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return FundsViewModel(repository) as T
            }
        }

        enableEdgeToEdge()
        setContent {
            val viewModel = remember {
                ViewModelProvider(this, viewModelFactory)[FundsViewModel::class.java]
            }
            
            MoneyKeeperTheme {
                FundsScreen(viewModel = viewModel)
            }
        }
    }
}
