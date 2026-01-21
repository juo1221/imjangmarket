package com.example.imjangmarket.service

import com.example.imjangmarket.repository.UserRepository
import org.springframework.stereotype.Service
import kotlin.random.Random

@Service
class UserService(private val userRepository: UserRepository) {

    fun createRandomUser() {
        val randomName = "User" + Random.nextInt(1000)
        val randomAge = Random.nextInt(18, 100)

        userRepository.insertUser(randomName, randomAge)
    }
}
