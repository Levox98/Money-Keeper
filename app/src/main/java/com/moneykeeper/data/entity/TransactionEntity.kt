package com.moneykeeper.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.moneykeeper.domain.model.TransactionType
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
