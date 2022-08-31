package com.versec.domain.usecases

import com.versec.domain.repository.UserRepositoryDepre

class GetUserUseCase(private val userRepository: UserRepositoryDepre)
{
    suspend operator fun invoke() = userRepository.getAllUser()
}