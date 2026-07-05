package com.moneykeeper.domain.repository

import com.moneykeeper.domain.model.Category
import com.moneykeeper.domain.model.FinancialTransaction
import kotlinx.coroutines.flow.Flow

interface AddTransactionRepository {

    suspend fun addTransaction(transaction: FinancialTransaction)

    fun getCategories(): Flow<List<Category>>
}