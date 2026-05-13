package com.moneykeeper.data.mapper

import com.moneykeeper.data.entity.CategoryEntity
import com.moneykeeper.domain.model.Category

fun CategoryEntity.toDomain(): Category = Category(
    id = id,
    name = name,
    iconResId = iconResId,
    colorHex = colorHex
)

fun Category.toEntity(): CategoryEntity = CategoryEntity(
    id = id ?: 0,
    name = name,
    iconResId = iconResId,
    colorHex = colorHex
)
