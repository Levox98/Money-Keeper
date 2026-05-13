package com.moneykeeper.data.repository

import com.moneykeeper.data.dao.CategoryDao
import com.moneykeeper.data.dao.TransactionDao
import com.moneykeeper.data.mapper.toDomain
import com.moneykeeper.data.mapper.toEntity
import com.moneykeeper.domain.model.Category
import com.moneykeeper.domain.model.FinancialTransaction
import com.moneykeeper.domain.repository.FundsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

class FundsRepositoryImpl(
    private val transactionDao: TransactionDao,
    private val categoryDao: CategoryDao
) : FundsRepository {

    override fun getAllTransactions(): Flow<List<FinancialTransaction>> {
        return combine(
            transactionDao.getAllTransactions(),
            categoryDao.getAllCategories()
        ) { transactions, categories ->
            val categoryMap = categories.associateBy { it.id }
            transactions.map { entity ->
                val categoryEntity = categoryMap[entity.categoryId]
                val category = categoryEntity?.toDomain() ?: Category(name = "Unknown")
                entity.toDomain(category)
            }
        }
    }

    override suspend fun getTransactionById(id: Long): FinancialTransaction? {
        val entity = transactionDao.getTransactionById(id) ?: return null
        val categoryEntity = categoryDao.getCategoryById(entity.categoryId)
        val category = categoryEntity?.toDomain() ?: Category(name = "Unknown")
        return entity.toDomain(category)
    }

    override suspend fun addTransaction(transaction: FinancialTransaction) {
        transactionDao.insertTransaction(transaction.toEntity())
    }

    override suspend fun updateTransaction(transaction: FinancialTransaction) {
        transactionDao.updateTransaction(transaction.toEntity())
    }

    override suspend fun deleteTransaction(transaction: FinancialTransaction) {
        transactionDao.deleteTransaction(transaction.toEntity())
    }

    override fun getCategories(): Flow<List<Category>> {
        return categoryDao.getAllCategories().map { list ->
            list.map { it.toDomain() }
        }
    }

    override suspend fun addCategory(category: Category) {
        categoryDao.insertCategory(category.toEntity())
    }

    override suspend fun deleteCategory(category: Category) {
        categoryDao.deleteCategory(category.toEntity())
    }
}
