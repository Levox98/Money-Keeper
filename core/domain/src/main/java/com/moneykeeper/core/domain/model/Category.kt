package com.moneykeeper.core.domain.model

data class Category(
    val id: Long? = null,
    val name: String,
    val iconResId: Int? = null,
    val colorHex: String? = null
)
