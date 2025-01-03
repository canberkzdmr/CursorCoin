package com.cursorcoin.domain.usecase

import com.cursorcoin.domain.repository.CoinRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ManageSettingsUseCase @Inject constructor(
    private val repository: CoinRepository,
    @com.cursorcoin.core.di.IoDispatcher private val dispatcher: CoroutineDispatcher
) {
    fun getUseLocalData(): Flow<Boolean> = repository.getUseLocalData()

    suspend fun setUseLocalData(useLocal: Boolean) {
        repository.setUseLocalData(useLocal)
    }
} 