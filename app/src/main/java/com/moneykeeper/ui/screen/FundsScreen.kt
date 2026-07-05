package com.moneykeeper.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import com.moneykeeper.R
import com.moneykeeper.domain.model.Category
import com.moneykeeper.domain.model.FinancialTransaction
import com.moneykeeper.domain.model.TransactionType
import com.moneykeeper.ui.component.AddCategoryDialog
import com.moneykeeper.ui.component.AddTransactionDialog
import com.moneykeeper.ui.component.BalanceCard
import com.moneykeeper.ui.component.TransactionItem
import com.moneykeeper.ui.theme.MoneyKeeperTheme
import com.moneykeeper.ui.viewmodel.FundsViewModel
import java.math.BigDecimal

@Composable
fun FundsScreen(viewModel: FundsViewModel, toAddTransaction: () -> Unit) {
    val transactions by viewModel.transactions.collectAsState()
    val categories by viewModel.categories.collectAsState()

    FundsScreenContent(
        transactions = transactions,
        categories = categories,
        onAddTransaction = { amount, type, category, note ->
            viewModel.addTransaction(amount, type, category, note)
        },
        onAddCategory = { name ->
            viewModel.addCategory(name)
        },
        onDeleteTransaction = { transaction ->
            viewModel.deleteTransaction(transaction)
        },
        toAddTransaction = toAddTransaction
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FundsScreenContent(
    transactions: List<FinancialTransaction>,
    categories: List<Category>,
    onAddTransaction: (BigDecimal, TransactionType, Category, String?) -> Unit,
    onAddCategory: (String) -> Unit,
    onDeleteTransaction: (FinancialTransaction) -> Unit,
    toAddTransaction: () -> Unit = {}
) {
    var showAddTransactionDialog by remember { mutableStateOf(value = false) }
    var showAddCategoryDialog by remember { mutableStateOf(value = false) }
    var isFabExpanded by remember { mutableStateOf(false) }
    var initialTransactionType by remember { mutableStateOf(TransactionType.EXPENSE) }

    val totalBalance = transactions.sumOf { 
        if (it.type == TransactionType.INCOME) it.amount else it.amount.negate()
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(stringResource(R.string.app_name)) })
        },
        floatingActionButton = {
            Column(horizontalAlignment = Alignment.End) {
                if (isFabExpanded) {
                    ExtendedFloatingActionButton(
                        text = { Text(stringResource(R.string.add_category)) },
                        icon = { Icon(Icons.Default.Add, contentDescription = null) },
                        onClick = {
                            showAddCategoryDialog = true
                            isFabExpanded = false
                        },
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    ExtendedFloatingActionButton(
                        text = { Text(stringResource(R.string.add_transaction)) },
                        icon = { Icon(Icons.Default.Add, contentDescription = null) },
                        onClick = {
                            initialTransactionType = TransactionType.INCOME
                            toAddTransaction()
                            isFabExpanded = false
                        },
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
                FloatingActionButton(onClick = { isFabExpanded = !isFabExpanded }) {
                    Icon(
                        imageVector = if (isFabExpanded) Icons.Default.Close else Icons.Default.Add,
                        contentDescription = stringResource(if (isFabExpanded) R.string.cancel else R.string.add_transaction)
                    )
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            BalanceCard(totalBalance)
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                text = stringResource(R.string.recent_transactions),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(transactions) { transaction ->
                    TransactionItem(
                        transaction = transaction,
                        onDelete = { onDeleteTransaction(transaction) }
                    )
                }
            }
        }
    }

    if (showAddTransactionDialog) {
        AddTransactionDialog(
            categories = categories,
            initialType = initialTransactionType,
            onDismiss = { showAddTransactionDialog = false },
            onConfirm = { amount, type, category, note ->
                onAddTransaction(amount, type, category, note)
                showAddTransactionDialog = false
            }
        )
    }

    if (showAddCategoryDialog) {
        AddCategoryDialog(
            onDismiss = { showAddCategoryDialog = false },
            onConfirm = { name ->
                onAddCategory(name)
                showAddCategoryDialog = false
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun FundsScreenPreview() {
    val sampleCategories = listOf(
        Category(id = 1L, name = "Food", iconResId = null, colorHex = null),
        Category(id = 2L, name = "Salary", iconResId = null, colorHex = null)
    )
    val sampleTransactions = listOf(
        FinancialTransaction(
            id = 1L,
            amount = BigDecimal("1000.00"),
            type = TransactionType.INCOME,
            category = sampleCategories[1],
            timestamp = System.currentTimeMillis(),
            note = "Monthly salary"
        ),
        FinancialTransaction(
            id = 2L,
            amount = BigDecimal("150.50"),
            type = TransactionType.EXPENSE,
            category = sampleCategories[0],
            timestamp = System.currentTimeMillis() - 86400000,
            note = "Grocery store"
        )
    )
    MoneyKeeperTheme {
        FundsScreenContent(
            transactions = sampleTransactions,
            categories = sampleCategories,
            onAddTransaction = { _, _, _, _ -> },
            onAddCategory = {},
            onDeleteTransaction = {}
        )
    }
}
