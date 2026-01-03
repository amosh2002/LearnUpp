package com.learnupp.domain.usecase.profile

import com.learnupp.domain.repo.ProfileRepository

interface UpdateProfileUseCase {
    suspend operator fun invoke(username: String?, fullName: String?, about: String?): Boolean
}

class UpdateProfileUseCaseImpl(
    private val repo: ProfileRepository
) : UpdateProfileUseCase {
    override suspend fun invoke(username: String?, fullName: String?, about: String?): Boolean =
        repo.updateProfile(username, fullName, about)
}

