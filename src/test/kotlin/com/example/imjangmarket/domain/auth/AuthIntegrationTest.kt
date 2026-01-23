package com.example.imjangmarket.domain.auth

import com.example.imjangmarket.domain.auth.dto.LoginRequest
import com.example.imjangmarket.domain.auth.dto.SignupRequest
import com.example.imjangmarket.domain.auth.service.AuthService
import org.junit.jupiter.api.DisplayName
import org.springframework.boot.test.context.SpringBootTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@ActiveProfiles("test")
class AuthIntegrationTest @Autowired constructor (private val authService: AuthService) {
     @Test
     @DisplayName("회원가입 후 로그인 시 Access와 Refresh 토큰이 발급되어야 한다.")
     fun signupAndLoginSuccess() {
          // given
          val signupRequest = SignupRequest("testuser", "qwer1234", "nickname")
          authService.signup(signupRequest)
          // when
          val loginRequest = LoginRequest("testuser", "qwer1234")
          val tokenPair = authService.login(loginRequest)
          assertNotNull(tokenPair.first,"Access Token은  null이 아니어야 한다.")
          assertNotNull(tokenPair.second,"Refresh Token은  null이 아니어야 한다.")

          println("발급된 Access Token : ${tokenPair.first}")
     }
}