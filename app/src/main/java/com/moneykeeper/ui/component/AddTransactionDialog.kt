package com.moneykeeper.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.moneykeeper.R
import com.moneykeeper.domain.model.Category
import com.moneykeeper.domain.model.TransactionType
import com.moneykeeper.ui.theme.MoneyKeeperTheme
import java.math.BigDecimal

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
