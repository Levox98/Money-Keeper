package com.moneykeeper.domain.model

import java.math.BigDecimal

data class FinancialTransaction(
    val id: Long? = null,
    val amount: BigDecimal,
    val type: TransactionType,
    val category: Category,
    val timestamp: Long = System.currentTimeMillis(),
    val note: String? = null
)
