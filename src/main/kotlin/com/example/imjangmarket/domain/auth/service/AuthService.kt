package com.example.imjangmarket.domain.auth.service

import com.example.imjangmarket.domain.auth.dto.LoginRequest
import com.example.imjangmarket.global.security.JwtProvider
import com.example.imjangmarket.jooq.tables.references.MEMBER
import com.example.imjangmarket.jooq.tables.references.REFRESH_TOKEN
import com.example.imjangmarket.domain.auth.dto.SignupRequest
import com.example.imjangmarket.domain.shop.repository.ShopRepository
import com.example.imjangmarket.global.exception.AuthException

import org.jooq.DSLContext
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.support.TransactionOperations
import java.time.LocalDateTime

@Service
class AuthService(
     private val dsl: DSLContext,
     private val passwordEncoder: PasswordEncoder,
     private val jwtProvider: JwtProvider,
     private val transaction: TransactionOperations,
     private val shopRepository: ShopRepository
) {
fun signup(request: SignupRequest) {
     val exists = dsl.fetchExists(MEMBER, MEMBER.USER_ID.eq(request.userId))
     if (exists) throw AuthException.DuplicateId()
     val encodedPassword = passwordEncoder.encode(request.password)
     transaction.execute {
          val memberId = dsl.insertInto(MEMBER)
               .set(MEMBER.USER_ID, request.userId)
               .set(MEMBER.PASSWORD, encodedPassword)
               .set(MEMBER.NICKNAME, request.nickname)
               .set(MEMBER.ROLE, "USER")
               .returning(MEMBER.ID) // 1. 돌려받을 컬럼(PK)을 지정합니다.
               .fetchOne()           // 2. 단일 행을 가져옵니다.
               ?.id
          shopRepository.save(memberId!!)
     }
}

fun login(request: LoginRequest): Pair<String, String> {
     val member = dsl.selectFrom(MEMBER)
          .where(MEMBER.USER_ID.eq(request.userId))
          .fetchOne() ?: throw AuthException.UserNotFound()
     if (!passwordEncoder.matches(request.password, member.password)) throw AuthException.InvalidPassword()
     val accessToken = jwtProvider.createAccessToken(member.id!!, member.userId!!,  member.role!!)
     val refreshToken = jwtProvider.createRefreshToken(member.id!!,member.userId!!,  member.role!!)
     transaction.execute {
          dsl.insertInto(REFRESH_TOKEN)
               .set(REFRESH_TOKEN.USER_ID, member.userId)
               .set(REFRESH_TOKEN.TOKEN_VALUE, refreshToken)
               .set(REFRESH_TOKEN.EXPIRY_DATE, LocalDateTime.now().plusDays(7))
               .onDuplicateKeyUpdate()
               .set(REFRESH_TOKEN.TOKEN_VALUE, refreshToken)
               .execute()
     }
     return Pair(accessToken, refreshToken)
}

fun logout(userId: String) {
     transaction.execute {
          dsl.deleteFrom(REFRESH_TOKEN)
               .where(REFRESH_TOKEN.USER_ID.eq(userId))
               .execute()
     }
}
}