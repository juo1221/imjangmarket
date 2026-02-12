package com.example.imjangmarket.global.config

import kotlin.reflect.KClass

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class ApiErrorDescription(
     val errorClass: KClass<*>
)