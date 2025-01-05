package com.cursorcoin.data.remote.dto

import com.cursorcoin.domain.model.CoinHistory

fun CoinHistoryDto.toCoinHistory(): List<CoinHistory> {
    return prices.map { (timestamp, price) ->
        CoinHistory(
            timestamp = timestamp.toLong(),
            price = price
        )
    }
} 