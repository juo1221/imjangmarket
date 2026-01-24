package com.example.imjangmarket.global.exception
import org.springframework.http.HttpStatus

abstract class BusinessException(
     override val message: String,
     val status: HttpStatus
) : RuntimeException(message)
