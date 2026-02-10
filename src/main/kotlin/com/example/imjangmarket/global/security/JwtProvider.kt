package com.example.imjangmarket.global.security

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import java.util.Date
import org.springframework.stereotype.Component

@Component
class JwtProvider(
     @param:Value($$"${jwt.secret}") private val secretKey: String
) {
     private val key = Keys.hmacShaKeyFor(secretKey.toByteArray())

     // 유효 기간 설정
     val accessTokenValidTime = 1000L * 60 * 30 // 30분
     val refreshTokenValidTime = 1000L * 60 * 60 * 24 * 7 // 7일

     fun createAccessToken(id: Long, userId: String, role: String): String {
          return createToken(id, userId, role, accessTokenValidTime)
     }

     fun createRefreshToken(id: Long, userId: String, role: String): String {
          return createToken(id, userId, role, refreshTokenValidTime)
     }

     private fun createToken(id: Long, userId: String, role: String, validTime: Long): String {
          val now = Date()
          return Jwts.builder()
               .subject(userId)
               .claim("memberId", id)
               .claim("role", role)
               .issuedAt(now)
               .expiration(Date(now.time + validTime))
               .signWith(key)
               .compact()
     }

     // 쿠키 생성 메서드
     fun createCookie(name: String, value: String, maxAge: Long): Cookie {
          return Cookie(name, value).apply {
               isHttpOnly = true
               secure = true // HTTPS 환경 권장
               path = "/"
               this.maxAge = (maxAge / 1000).toInt()
          }
     }
     /**
      * request에서 accessToken 추출
      */
     fun resolveToken(request: HttpServletRequest): String? {
          val bearerToken = request.getHeader("Authorization")
          if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
               return bearerToken.substring(7)
          }
          val cookies = request.cookies
          return cookies?.find { it.name == "accessToken" }?.value
     }

     /**
      *  토큰의 유효성을 검증
      */
     fun validateToken(token: String): Boolean  =
          try {
               Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
               true
          } catch (e: Exception) {
               false
          }

     /**
      * 유효한 토큰에서 사용자 정보를 꺼내 인증 객체로 변환
      */
     fun getAuthentication(token: String): Authentication {
          println("token : $token")
          // 토큰에서 클레임(데이터 내용물) 추출
          val claims = Jwts.parser()
               .verifyWith(key)
               .build()
               .parseSignedClaims(token)
               .payload

          val memberId = claims["memberId"].toString().toLong()
          val userId = claims.subject
          val role = claims["role"] as String

          /*권한리스트*/
          val authorities = listOf(SimpleGrantedAuthority("ROLE_$role"))
          val principal = CustomUserDetails(memberId, userId, authorities)
          return UsernamePasswordAuthenticationToken(principal, token, authorities)
     }
}