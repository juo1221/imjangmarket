package com.example.imjangmarket.domain.auth.controller

import com.example.imjangmarket.domain.auth.dto.LoginRequest
import com.example.imjangmarket.domain.auth.service.AuthService
import com.example.imjangmarket.domain.auth.dto.SignupRequest
import com.example.imjangmarket.global.security.JwtProvider
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal


@RestController
@RequestMapping("/api/auth")
class AuthController(
     private val authService: AuthService,
     private val jwtProvider: JwtProvider
) {

     // 회원가입 API
     @PostMapping("/signup")
     fun signup(@RequestBody request: SignupRequest): ResponseEntity<String> {
          authService.signup(request)
          return ResponseEntity.ok("회원가입이 완료되었습니다.")
     }

     // 로그인 API
     @PostMapping("/login")
     fun login(@RequestBody request: LoginRequest, response: HttpServletResponse): ResponseEntity<String> {
          val (accessToken, refreshToken) = authService.login(request)
          response.addCookie(jwtProvider.createCookie("accessToken", accessToken, 1800000))
          response.addCookie(jwtProvider.createCookie("refreshToken", refreshToken, 604800000))
          return ResponseEntity.ok("로그인되었습니다.")
     }

     @PostMapping("/logout")
     /*Principal : 현재 로그인한 사용자의 정보를 담고 있는 객체*/
     fun logout(principal: Principal, response: HttpServletResponse): ResponseEntity<String> {
          authService.logout(principal.name)
          val atCookie = Cookie("accessToken", null).also {
               it.maxAge = 0
               it.isHttpOnly = true
               it.path = "/"
          }
          val rtCookie = Cookie("refreshToken", null).also {
               it.maxAge = 0
               it.isHttpOnly = true
               it.path = "/"
          }
          response.addCookie(atCookie)
          response.addCookie(rtCookie)
          return ResponseEntity.ok("로그아웃되었습니다.")
     }
}