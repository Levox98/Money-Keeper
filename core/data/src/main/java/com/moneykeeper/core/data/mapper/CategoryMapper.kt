package com.moneykeeper.core.data.mapper

import com.moneykeeper.core.data.entity.CategoryEntity
import com.moneykeeper.core.domain.model.Category

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
