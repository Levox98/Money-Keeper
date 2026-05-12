package com.moneykeeper.domain.repository

import com.moneykeeper.domain.model.Category
import com.moneykeeper.domain.model.FinancialTransaction
import kotlinx.coroutines.flow.Flow

interface FundsRepository {

    fun getAllTransactions(): Flow<List<FinancialTransaction>>

    suspend fun getTransactionById(id: Long): FinancialTransaction?

    suspend fun addTransaction(transaction: FinancialTransaction)

    suspend fun updateTransaction(transaction: FinancialTransaction)

    suspend fun deleteTransaction(transaction: FinancialTransaction)

    fun getCategories(): Flow<List<Category>>

    suspend fun addCategory(category: Category)

    suspend fun deleteCategory(category: Category)
}
