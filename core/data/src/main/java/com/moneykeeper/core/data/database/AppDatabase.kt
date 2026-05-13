package com.moneykeeper.core.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.moneykeeper.core.data.dao.CategoryDao
import com.moneykeeper.core.data.dao.TransactionDao
import com.moneykeeper.core.data.entity.CategoryEntity
import com.moneykeeper.core.data.entity.TransactionEntity

@Database(entities = [TransactionEntity::class, CategoryEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao
    abstract fun categoryDao(): CategoryDao
}
