package com.moneykeeper.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moneykeeper.domain.model.Category
import com.moneykeeper.domain.model.FinancialTransaction
import com.moneykeeper.domain.model.TransactionType
import com.moneykeeper.domain.repository.FundsRepository
import com.moneykeeper.domain.usecase.AnalyzeFinancesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.math.BigDecimal

class FundsViewModel(
    private val repository: FundsRepository,
    private val analyzeFinancesUseCase: AnalyzeFinancesUseCase
) : ViewModel() {

    val transactions: StateFlow<List<FinancialTransaction>> = repository.getAllTransactions()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val categories: StateFlow<List<Category>> = repository.getCategories()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _aiAnalysis = MutableStateFlow<String?>(null)
    val aiAnalysis: StateFlow<String?> = _aiAnalysis.asStateFlow()

    private val _isAnalyzing = MutableStateFlow(false)
    val isAnalyzing: StateFlow<Boolean> = _isAnalyzing.asStateFlow()

    fun runAiAnalysis() {
        viewModelScope.launch {
            _isAnalyzing.value = true
            analyzeFinancesUseCase.execute()
                .onSuccess { _aiAnalysis.value = it }
                .onFailure { _aiAnalysis.value = "Ошибка: ${it.message}" }
            _isAnalyzing.value = false
        }
    }

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
