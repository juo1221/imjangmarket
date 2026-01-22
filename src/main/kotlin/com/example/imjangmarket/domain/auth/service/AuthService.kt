package com.example.imjangmarket.domain.auth.service

import com.example.imjangmarket.domain.auth.dto.LoginRequest
import com.example.imjangmarket.global.security.JwtProvider
import com.example.imjangmarket.jooq.tables.references.MEMBER
import com.example.imjangmarket.jooq.tables.references.REFRESH_TOKEN
import com.example.imjangmarket.domain.auth.dto.SignupRequest

import org.jooq.DSLContext
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class AuthService(
     private val dsl: DSLContext,
     private val passwordEncoder: PasswordEncoder,
     private val jwtProvider: JwtProvider
) {
     private val memberTABLE = MEMBER // jOOQ 생성 클래스
     private val refreshTokenTABLE = REFRESH_TOKEN

     @Transactional
     fun signup(request: SignupRequest) {
          // 1. 아이디 중복 체크 (jOOQ)
          val exists = dsl.fetchExists(memberTABLE, memberTABLE.USER_ID.eq(request.userId))
          if (exists) throw RuntimeException("이미 존재하는 아이디입니다.")

          // 2. 비밀번호 암호화 (BCrypt + Salt 자동 적용)
          val encodedPassword = passwordEncoder.encode(request.password)

          // 3. 저장
          dsl.insertInto(memberTABLE)
               .set(memberTABLE.USER_ID, request.userId)
               .set(memberTABLE.PASSWORD, encodedPassword)
               .set(memberTABLE.NICKNAME, request.nickname)
               .set(memberTABLE.ROLE, "USER")
               .execute()
     }
     @Transactional
     fun login(request: LoginRequest): Pair<String,String> {
          val member = dsl.selectFrom(memberTABLE)
               .where(memberTABLE.USER_ID.eq(request.userId))
               .fetchOne() ?: throw RuntimeException("사용자를 찾을 수 없습니다.")
          if(!passwordEncoder.matches(request.password, member.password)) throw RuntimeException("비밀번호가 일치하지 않습니다.")
          val accessToken = jwtProvider.createAccessToken(member.userId!!,member.role!!)
          val refreshToken = jwtProvider.createRefreshToken(member.userId!!,member.role!!)
               dsl.insertInto(refreshTokenTABLE)
                    .set(refreshTokenTABLE.USER_ID,member.userId)
                    .set(refreshTokenTABLE.TOKEN_VALUE,refreshToken)
                    .set(refreshTokenTABLE.EXPIRY_DATE, LocalDateTime.now().plusDays(7))
                    .onDuplicateKeyUpdate()
                    .set(refreshTokenTABLE.TOKEN_VALUE,refreshToken)
                    .execute()
          return Pair(accessToken, refreshToken)
     }
     @Transactional
     fun logout(userId:String) {
          dsl.deleteFrom(refreshTokenTABLE)
               .where(refreshTokenTABLE.USER_ID.eq(userId))
               .execute()
     }
}