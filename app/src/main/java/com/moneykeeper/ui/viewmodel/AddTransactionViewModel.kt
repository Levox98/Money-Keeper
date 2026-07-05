package com.moneykeeper.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moneykeeper.domain.model.Category
import com.moneykeeper.domain.model.FinancialTransaction
import com.moneykeeper.domain.model.TransactionType
import com.moneykeeper.domain.repository.AddTransactionRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.math.BigDecimal

class AddTransactionViewModel(
    private val repository: AddTransactionRepository,
) : ViewModel() {

    val categories: StateFlow<List<Category>> = repository.getCategories()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private var currentTransaction: FinancialTransaction = FinancialTransaction()

    fun addTransaction() {
        viewModelScope.launch {
            repository.addTransaction(currentTransaction)
        }
    }

    fun setAmount(amount: BigDecimal) {
        currentTransaction = currentTransaction.copy(amount = amount)
    }

    fun setType(type: TransactionType) {
        currentTransaction = currentTransaction.copy(type = type)
    }

    fun setCategory(category: Category) {
        currentTransaction = currentTransaction.copy(category = category)
    }

    fun setNote(note: String) {
        currentTransaction = currentTransaction.copy(note = note)
    }
}