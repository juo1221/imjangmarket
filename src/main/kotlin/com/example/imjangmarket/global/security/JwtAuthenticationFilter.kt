package com.example.imjangmarket.global.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter

/**
 * 모든 요청마다 실행되어 JWT 토큰의 유효성을 검사하는 필터
 * OncePerRequestFilter : 하나의 요청에 대해 딱 한 번만 실행되도록 보장
 */
class JwtAuthenticationFilter(
     private val jwtProvider: JwtProvider
): OncePerRequestFilter() {
    override fun doFilterInternal(
         request: HttpServletRequest,
         response: HttpServletResponse,
         filterChain: FilterChain
    ) {
         /*accessToken 추출*/
         val token = jwtProvider.resolveToken(request)

        if(token  != null && jwtProvider.validateToken(token)) {
            SecurityContextHolder.getContext().authentication = jwtProvider.getAuthentication(token)
        }
         /*에러처리는 안함
         * 1. login or signup인 경우일때는 pass해야됨.
         * 2. 나머지는 상황에 따라 다음 필터에서 처리하도록
         * */

         filterChain.doFilter(request, response)
    }
}