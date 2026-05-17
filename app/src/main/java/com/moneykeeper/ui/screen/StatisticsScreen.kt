package com.moneykeeper.ui.screen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.moneykeeper.R
import com.moneykeeper.domain.model.Category
import com.moneykeeper.domain.model.FinancialTransaction
import com.moneykeeper.domain.model.TransactionType
import com.moneykeeper.ui.theme.MoneyKeeperTheme
import com.moneykeeper.ui.viewmodel.FundsViewModel
import java.math.BigDecimal

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsScreen(viewModel: FundsViewModel) {
    val transactions by viewModel.transactions.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(stringResource(R.string.statistics)) })
        }
    ) { padding ->
        StatisticsContent(
            transactions = transactions,
            modifier = Modifier.padding(padding)
        )
    }
}

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

    if (expenseTransactions.isEmpty()) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = stringResource(R.string.no_expenses_yet))
        }
    } else {
        Column(
            modifier = modifier
                .fillMaxSize()
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

@Composable
fun PieChart(
    data: List<Pair<Category, BigDecimal>>,
    total: BigDecimal,
    modifier: Modifier = Modifier
) {
    val colors = listOf(
        Color(0xFFF44336), Color(0xFFE91E63), Color(0xFF9C27B0),
        Color(0xFF673AB7), Color(0xFF3F51B5), Color(0xFF2196F3),
        Color(0xFF03A9F4), Color(0xFF00BCD4), Color(0xFF009688),
        Color(0xFF4CAF50), Color(0xFF8BC34A), Color(0xFFCDDC39),
        Color(0xFFFFEB3B), Color(0xFFFFC107), Color(0xFFFF9800),
        Color(0xFFFF5722)
    )

    Canvas(modifier = modifier) {
        var startAngle = -90f
        data.forEachIndexed { index, pair ->
            val sweepAngle = (pair.second.toDouble() / total.toDouble() * 360f).toFloat()
            drawArc(
                color = colors[index % colors.size],
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                useCenter = true
            )
            startAngle += sweepAngle
        }
    }
}

@Composable
fun CategoryExpenseItem(category: Category, amount: BigDecimal, total: BigDecimal) {
    val percentage = if (total > BigDecimal.ZERO) {
        (amount.toDouble() / total.toDouble() * 100).toInt()
    } else 0

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = category.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "$percentage%",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
            Text(
                text = stringResource(R.string.expense_format, amount.toString()),
                color = Color.Red,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
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
