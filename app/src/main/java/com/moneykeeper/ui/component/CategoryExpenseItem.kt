package com.moneykeeper.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.moneykeeper.R
import com.moneykeeper.domain.model.Category
import com.moneykeeper.ui.theme.MoneyKeeperTheme
import java.math.BigDecimal

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
fun CategoryExpenseItemPreview() {
    val sampleCategory = Category(id = 1L, name = "Food", iconResId = null, colorHex = null)
    MoneyKeeperTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            CategoryExpenseItem(
                category = sampleCategory,
                amount = BigDecimal("500.00"),
                total = BigDecimal("1000.00")
            )
        }
    }
}
