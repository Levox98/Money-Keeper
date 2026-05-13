package com.moneykeeper.data.mapper

import com.moneykeeper.data.entity.TransactionEntity
import com.moneykeeper.domain.model.Category
import com.moneykeeper.domain.model.FinancialTransaction

fun TransactionEntity.toDomain(category: Category): FinancialTransaction = FinancialTransaction(
    id = id,
    amount = amount,
    type = type,
    category = category,
    timestamp = timestamp,
    note = note
)

fun FinancialTransaction.toEntity(): TransactionEntity = TransactionEntity(
    id = id ?: 0,
    amount = amount,
    type = type,
    categoryId = category.id ?: 0,
    timestamp = timestamp,
    note = note
)
