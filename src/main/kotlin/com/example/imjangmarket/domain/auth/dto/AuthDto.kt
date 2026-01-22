package com.example.imjangmarket.domain.auth.dto
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size


data class SignupRequest(
     @field:NotBlank(message = "아이디는 필수입니다.")
     @field:Size(min = 4, max = 20, message = "아이디는 4~20자 사이여야 합니다.")
     val userId: String,

     @field:NotBlank(message = "비밀번호는 필수입니다.")
     @field:Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다.")
     val password: String,

     @field:NotBlank(message = "닉네임은 필수입니다.")
     val nickname: String
)

data class LoginRequest(
     @field:NotBlank(message = "아이디를 입력해주세요.")
     val userId: String,

     @field:NotBlank(message = "비밀번호를 입력해주세요.")
     val password: String
)