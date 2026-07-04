package com.moneykeeper.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.moneykeeper.R
import com.moneykeeper.domain.model.Category
import com.moneykeeper.domain.model.FinancialTransaction
import com.moneykeeper.domain.model.TransactionType
import com.moneykeeper.ui.component.CategoryExpenseItem
import com.moneykeeper.ui.component.PieChart
import com.moneykeeper.ui.theme.MoneyKeeperTheme
import com.moneykeeper.ui.viewmodel.FundsViewModel
import java.math.BigDecimal

@Composable
fun StatisticsScreen(viewModel: FundsViewModel) {
    val transactions by viewModel.transactions.collectAsState()

    StatisticsContent(
        transactions = transactions,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsContent(
    transactions: List<FinancialTransaction>,
    modifier: Modifier = Modifier
) {
    val expenseTransactions = transactions.filter { it.type == TransactionType.EXPENSE }
    val expensesByCategory = expenseTransactions.groupBy { it.category }
        .mapValues { entry -> entry.value.sumOf { it.amount } }
        .toList()
        .sortedByDescending { it.second }

    val totalExpenses = expensesByCategory.sumOf { it.second }


    Scaffold(
        topBar = {
            TopAppBar(title = { Text(stringResource(R.string.statistics)) })
        }
    ) { padding ->
        if (expenseTransactions.isEmpty()) {
            Box(
                modifier = modifier
                    .padding(padding)
                    .fillMaxSize(), contentAlignment = Alignment.Center
            ) {
                Text(text = stringResource(R.string.no_expenses_yet))
            }
        } else {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                PieChart(
                    data = expensesByCategory,
                    total = totalExpenses,
                    modifier = Modifier
                        .size(200.dp)
                        .padding(16.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    items(expensesByCategory) { (category, amount) ->
                        CategoryExpenseItem(category, amount, totalExpenses)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StatisticsScreenPreview() {
    val sampleCategories = listOf(
        Category(id = 1L, name = "Food", iconResId = null, colorHex = null),
        Category(id = 2L, name = "Transport", iconResId = null, colorHex = null),
        Category(id = 3L, name = "Rent", iconResId = null, colorHex = null)
    )
    val sampleTransactions = listOf(
        FinancialTransaction(
            id = 1L,
            amount = BigDecimal("500.00"),
            type = TransactionType.EXPENSE,
            category = sampleCategories[0],
            timestamp = System.currentTimeMillis()
        ),
        FinancialTransaction(
            id = 2L,
            amount = BigDecimal("200.00"),
            type = TransactionType.EXPENSE,
            category = sampleCategories[1],
            timestamp = System.currentTimeMillis()
        ),
        FinancialTransaction(
            id = 3L,
            amount = BigDecimal("1000.00"),
            type = TransactionType.EXPENSE,
            category = sampleCategories[2],
            timestamp = System.currentTimeMillis()
        ),
        FinancialTransaction(
            id = 4L,
            amount = BigDecimal("3000.00"),
            type = TransactionType.INCOME,
            category = sampleCategories[2],
            timestamp = System.currentTimeMillis()
        )
    )
    MoneyKeeperTheme {
        StatisticsContent(transactions = sampleTransactions)
    }
}
