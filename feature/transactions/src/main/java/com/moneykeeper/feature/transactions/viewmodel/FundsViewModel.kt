package com.moneykeeper.feature.transactions.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moneykeeper.core.domain.model.Category
import com.moneykeeper.core.domain.model.FinancialTransaction
import com.moneykeeper.core.domain.model.TransactionType
import com.moneykeeper.core.domain.repository.FundsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.math.BigDecimal

class FundsViewModel(private val repository: FundsRepository) : ViewModel() {

    val transactions: StateFlow<List<FinancialTransaction>> = repository.getAllTransactions()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val categories: StateFlow<List<Category>> = repository.getCategories()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addTransaction(amount: BigDecimal, type: TransactionType, category: Category, note: String?) {
        viewModelScope.launch {
            val transaction = FinancialTransaction(
                amount = amount,
                type = type,
                category = category,
                note = note
            )
            repository.addTransaction(transaction)
        }
    }

    fun deleteTransaction(transaction: FinancialTransaction) {
        viewModelScope.launch {
            repository.deleteTransaction(transaction)
        }
    }

    fun addCategory(name: String) {
        viewModelScope.launch {
            repository.addCategory(Category(name = name))
        }
    }
}
