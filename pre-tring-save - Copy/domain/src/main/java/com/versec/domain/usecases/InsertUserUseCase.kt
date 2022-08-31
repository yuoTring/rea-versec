package com.versec.domain.usecases

import com.versec.domain.model.UserModel
import com.versec.domain.repository.UserRepositoryDepre

class InsertUserUseCase(private val userRepository: UserRepositoryDepre) {
    suspend operator fun invoke(userModel: UserModel) = userRepository.insertUser(userModel)
}