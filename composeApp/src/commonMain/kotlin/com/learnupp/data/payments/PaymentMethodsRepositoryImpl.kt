package com.learnupp.data.payments

import com.learnupp.domain.repo.PaymentMethodsRepository
import kotlinx.coroutines.flow.Flow

class PaymentMethodsRepositoryImpl(
    private val api: PaymentMethodsApi,
    private val storage: PaymentMethodsStorage
) : PaymentMethodsRepository {
    override fun getMethods(): Flow<List<com.learnupp.domain.model.payments.PaymentMethod>> =
        storage.getMethods()

    override suspend fun refreshData() {
        storage.save(api.fetchMethods())
    }

    override suspend fun needsRefresh(): Boolean {
        return storage.getMethods().value.isEmpty()
    }
}

