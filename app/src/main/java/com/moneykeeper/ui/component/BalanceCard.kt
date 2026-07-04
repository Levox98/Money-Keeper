package com.moneykeeper.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.moneykeeper.R
import com.moneykeeper.ui.theme.MoneyKeeperTheme
import java.math.BigDecimal

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

@Preview(showBackground = true)
@Composable
fun BalanceCardPreview() {
    MoneyKeeperTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            BalanceCard(balance = BigDecimal("1234.56"))
        }
    }
}
