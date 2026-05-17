package com.moneykeeper.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.moneykeeper.R
import com.moneykeeper.domain.model.Category
import com.moneykeeper.domain.model.FinancialTransaction
import com.moneykeeper.domain.model.TransactionType
import com.moneykeeper.ui.theme.MoneyKeeperTheme
import com.moneykeeper.ui.viewmodel.FundsViewModel
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun FundsScreen(viewModel: FundsViewModel) {
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
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FundsScreenContent(
    transactions: List<FinancialTransaction>,
    categories: List<Category>,
    onAddTransaction: (BigDecimal, TransactionType, Category, String?) -> Unit,
    onAddCategory: (String) -> Unit,
    onDeleteTransaction: (FinancialTransaction) -> Unit
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
                        text = { Text(stringResource(R.string.income)) },
                        icon = { Icon(Icons.Default.Add, contentDescription = null) },
                        onClick = {
                            initialTransactionType = TransactionType.INCOME
                            showAddTransactionDialog = true
                            isFabExpanded = false
                        },
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    ExtendedFloatingActionButton(
                        text = { Text(stringResource(R.string.expense)) },
                        icon = { Icon(Icons.Default.Add, contentDescription = null) },
                        onClick = {
                            initialTransactionType = TransactionType.EXPENSE
                            showAddTransactionDialog = true
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

@Composable
fun BalanceCard(balance: BigDecimal) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(stringResource(R.string.total_balance), style = MaterialTheme.typography.labelLarge)
            Text(
                text = stringResource(R.string.currency_format, balance.toString()),
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.ExtraBold,
                color = if (balance >= BigDecimal.ZERO) Color(0xFF2E7D32) else Color.Red
            )
        }
    }
}

@Composable
fun TransactionItem(
    transaction: FinancialTransaction,
    onDelete: (FinancialTransaction) -> Unit
) {
    val dateFormatString = stringResource(R.string.date_format)
    val dateFormat = remember(dateFormatString) { SimpleDateFormat(dateFormatString, Locale.getDefault()) }
    
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
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = transaction.category.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                if (!transaction.note.isNullOrBlank()) {
                    Text(transaction.note, style = MaterialTheme.typography.bodySmall)
                }
                Text(
                    text = dateFormat.format(Date(transaction.timestamp)),
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray
                )
            }
            
            Row(verticalAlignment = Alignment.CenterVertically) {
                val amountText = if (transaction.type == TransactionType.INCOME) {
                    stringResource(R.string.income_format, transaction.amount.toString())
                } else {
                    stringResource(R.string.expense_format, transaction.amount.toString())
                }
                Text(
                    text = amountText,
                    color = if (transaction.type == TransactionType.INCOME) Color(0xFF2E7D32) else Color.Red,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                IconButton(onClick = { onDelete(transaction) }) {
                    Icon(Icons.Default.Delete, contentDescription = stringResource(R.string.delete), tint = Color.Gray)
                }
            }
        }
    }
}

@Composable
fun AddTransactionDialog(
    categories: List<Category>,
    initialType: TransactionType = TransactionType.EXPENSE,
    onDismiss: () -> Unit,
    onConfirm: (BigDecimal, TransactionType, Category, String?) -> Unit
) {
    var amount by remember { mutableStateOf("") }
    var type by remember { mutableStateOf(initialType) }
    var selectedCategory by remember { mutableStateOf(categories.firstOrNull()) }
    var note by remember { mutableStateOf("") }
    
    LaunchedEffect(categories) {
        if (selectedCategory == null && categories.isNotEmpty()) {
            selectedCategory = categories.first()
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.add_transaction)) },
        text = {
            Column {
                TextField(
                    value = amount,
                    onValueChange = { amount = it },
                    label = { Text(stringResource(R.string.amount)) },
                    modifier = Modifier.fillMaxWidth()
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(selected = type == TransactionType.INCOME, onClick = { type = TransactionType.INCOME })
                    Text(stringResource(R.string.income))
                    Spacer(modifier = Modifier.width(8.dp))
                    RadioButton(selected = type == TransactionType.EXPENSE, onClick = { type = TransactionType.EXPENSE })
                    Text(stringResource(R.string.expense))
                }
                
                Text(stringResource(R.string.category_label), style = MaterialTheme.typography.labelLarge)
                if (categories.isEmpty()) {
                    Text(stringResource(R.string.add_category_first), color = Color.Red)
                } else {
                    ScrollableTabRow(selectedTabIndex = categories.indexOf(selectedCategory).coerceAtLeast(0)) {
                        categories.forEach { category ->
                            Tab(
                                selected = selectedCategory == category,
                                onClick = { selectedCategory = category },
                                text = { Text(category.name) }
                            )
                        }
                    }
                }
                
                TextField(
                    value = note,
                    onValueChange = { note = it },
                    label = { Text(stringResource(R.string.note_optional)) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                enabled = amount.toBigDecimalOrNull() != null && selectedCategory != null,
                onClick = { 
                    onConfirm(BigDecimal(amount), type, selectedCategory!!, note.ifBlank { null })
                }
            ) { Text(stringResource(R.string.add)) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(stringResource(R.string.cancel)) }
        }
    )
}

@Composable
fun AddCategoryDialog(
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var name by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.add_category)) },
        text = {
            TextField(
                value = name,
                onValueChange = { name = it },
                label = { Text(stringResource(R.string.category_name)) },
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = {
            Button(
                enabled = name.isNotBlank(),
                onClick = { onConfirm(name) }
            ) { Text(stringResource(R.string.add)) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(stringResource(R.string.cancel)) }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun BalanceCardPreview() {
    MoneyKeeperTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            BalanceCard(balance = BigDecimal("1234.56"))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TransactionItemPreview() {
    val sampleCategory = Category(id = 1L, name = "Food", iconResId = null, colorHex = null)
    val sampleTransaction = FinancialTransaction(
        id = 1L,
        amount = BigDecimal("500.00"),
        type = TransactionType.EXPENSE,
        category = sampleCategory,
        timestamp = System.currentTimeMillis(),
        note = "Dinner at restaurant"
    )
    MoneyKeeperTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            TransactionItem(transaction = sampleTransaction, onDelete = {})
        }
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

@Preview(showBackground = true)
@Composable
fun AddTransactionDialogPreview() {
    val sampleCategories = listOf(
        Category(id = 1L, name = "Food", iconResId = null, colorHex = null),
        Category(id = 2L, name = "Salary", iconResId = null, colorHex = null)
    )
    MoneyKeeperTheme {
        AddTransactionDialog(
            categories = sampleCategories,
            onDismiss = {},
            onConfirm = { _, _, _, _ -> }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AddCategoryDialogPreview() {
    MoneyKeeperTheme {
        AddCategoryDialog(onDismiss = {}, onConfirm = {})
    }
}
