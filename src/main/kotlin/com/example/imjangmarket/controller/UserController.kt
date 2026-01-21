package com.example.imjangmarket.controller

import com.example.imjangmarket.service.UserService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/test")
class UserController(private val userService: UserService) {

    @PostMapping("/adduser")
    fun createRandomUser(): String {
        userService.createRandomUser()
        return "Random user created successfully!"
    }
}
