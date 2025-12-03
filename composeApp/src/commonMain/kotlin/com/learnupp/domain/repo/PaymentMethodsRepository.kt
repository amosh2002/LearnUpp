package com.learnupp.domain.repo

import com.learnupp.domain.model.payments.PaymentMethod
import kotlinx.coroutines.flow.Flow

interface PaymentMethodsRepository : BaseRepository {
    fun getMethods(): Flow<List<PaymentMethod>>
}

