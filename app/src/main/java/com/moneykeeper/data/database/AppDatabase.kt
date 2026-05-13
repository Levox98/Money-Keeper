package com.moneykeeper.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.moneykeeper.data.dao.CategoryDao
import com.moneykeeper.data.dao.TransactionDao
import com.moneykeeper.data.entity.CategoryEntity
import com.moneykeeper.data.entity.TransactionEntity

@Database(entities = [TransactionEntity::class, CategoryEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao
    abstract fun categoryDao(): CategoryDao
}
