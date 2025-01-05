package com.cursorcoin.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.cursorcoin.domain.model.Portfolio

@Entity(tableName = "portfolio")
data class PortfolioEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val coinId: String,
    val amount: Double,
    val purchasePrice: Double,
    val lastUpdated: Long = System.currentTimeMillis()
) {
    fun toDomain(currentPrice: Double = 0.0) = Portfolio(
        id = id,
        coinId = coinId,
        amount = amount,
        purchasePrice = purchasePrice,
        currentPrice = currentPrice
    )
} 