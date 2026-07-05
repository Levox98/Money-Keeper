package com.moneykeeper.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import com.moneykeeper.domain.model.Category
import com.moneykeeper.domain.model.TransactionType
import com.moneykeeper.ui.theme.MoneyKeeperTheme
import com.moneykeeper.ui.viewmodel.AddTransactionViewModel
import java.math.BigDecimal

@Composable
fun AddTransactionScreen(viewmodel: AddTransactionViewModel) {

    val categories by viewmodel.categories.collectAsState()

    AddTransactionContent(
        categories = categories,
        onCategorySelected = viewmodel::setCategory,
        onAmountChanged = viewmodel::setAmount,
        onNoteChanged = viewmodel::setNote,
        onTypeChanged = viewmodel::setType,
        onAddTransaction = viewmodel::addTransaction
    )
}

// TODO: add an actual screen
@Composable
private fun AddTransactionContent(
    categories: List<Category> = emptyList(),
    onCategorySelected: (Category) -> Unit = {},
    onAmountChanged: (BigDecimal) -> Unit = {},
    onNoteChanged: (String) -> Unit = {},
    onTypeChanged: (TransactionType) -> Unit = {},
    onAddTransaction: () -> Unit = {}
) {

}

@Preview(showBackground = true)
@Composable
fun AddTransactionScreenPreview() {
    MoneyKeeperTheme {
        AddTransactionContent()
    }
}