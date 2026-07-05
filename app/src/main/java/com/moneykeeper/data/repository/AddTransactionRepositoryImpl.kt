package com.moneykeeper.data.repository

import com.moneykeeper.data.dao.CategoryDao
import com.moneykeeper.data.dao.TransactionDao
import com.moneykeeper.data.mapper.toDomain
import com.moneykeeper.data.mapper.toEntity
import com.moneykeeper.domain.model.Category
import com.moneykeeper.domain.model.FinancialTransaction
import com.moneykeeper.domain.repository.AddTransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AddTransactionRepositoryImpl(
    private val transactionDao: TransactionDao,
    private val categoryDao: CategoryDao
) : AddTransactionRepository {
    override suspend fun addTransaction(transaction: FinancialTransaction) {
        transactionDao.insertTransaction(transaction.toEntity())
    }

    override fun getCategories(): Flow<List<Category>> {
        return categoryDao.getAllCategories().map { list ->
            list.map { it.toDomain() }
        }
    }
}