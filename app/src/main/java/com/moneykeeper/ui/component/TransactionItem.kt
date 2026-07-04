package com.moneykeeper.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.*

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
