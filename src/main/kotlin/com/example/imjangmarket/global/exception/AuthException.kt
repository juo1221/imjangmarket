package com.example.imjangmarket.global.exception


/**
 * 회원 관련 예외
 */
sealed class AuthException(message: String) : BusinessException(message) {
     class DuplicateId : AuthException("이미 사용 중인 아이디입니다")
     class InvalidPassword : AuthException("비밀번호가 일치하지 않습니다.")
     class UserNotFound : AuthException("사용자를 찾을 수 없습니다.")
}
