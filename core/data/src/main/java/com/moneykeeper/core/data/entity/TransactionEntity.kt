package com.moneykeeper.core.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.moneykeeper.core.domain.model.TransactionType
import java.math.BigDecimal

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val amount: BigDecimal,
    val type: TransactionType,
    val categoryId: Long,
    val timestamp: Long,
    val note: String?
)
