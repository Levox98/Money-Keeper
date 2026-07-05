package com.moneykeeper.domain.model

import java.math.BigDecimal

data class FinancialTransaction(
    val id: Long? = null,
    val amount: BigDecimal = BigDecimal(0),
    val type: TransactionType = TransactionType.EXPENSE,
    val category: Category = Category(name = "Empty Category"), // TODO: change to null or localizable string
    val timestamp: Long = System.currentTimeMillis(),
    val note: String? = null
)
