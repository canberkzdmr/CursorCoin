package com.cursorcoin.domain.usecase

import com.cursorcoin.core.Resource
import com.cursorcoin.domain.repository.CoinRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ManageSettingsUseCase @Inject constructor(
    private val repository: CoinRepository
) {
    fun getUseLocalData(): Flow<Boolean> = repository.getUseLocalData()

    suspend fun setUseLocalData(useLocal: Boolean) = repository.setUseLocalData(useLocal)
} 