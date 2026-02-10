package com.example.imjangmarket.global.config

import com.example.imjangmarket.global.security.JwtAuthenticationFilter
import com.example.imjangmarket.global.security.JwtProvider
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
class SecurityConfig(
     private val jwtProvider: JwtProvider
) {

     @Bean
     fun filterChain(http: HttpSecurity): SecurityFilterChain {
          http
               .csrf { it.disable() } // JWT 사용 시 일반적으로 disable
               .formLogin { it.disable() }
               .httpBasic { it.disable() }
               // 세션을 사용하지 않음 (Stateless)
               .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
               .authorizeHttpRequests { auth ->
                    auth
                         .requestMatchers(
                              "/api/auth/**",
                              "/v3/api-docs/**",
                              "/swagger-ui/**",
                              "/swagger-ui.html"
                         ).permitAll() // 회원가입, 로그인은 허용
                         .requestMatchers("/api/admin/**").hasRole("ADMIN")// 관리자 전용
                         .anyRequest().authenticated() // 그 외 모든 요청은 로그인이 필요함
               }.addFilterBefore(
                    JwtAuthenticationFilter(jwtProvider),
                    UsernamePasswordAuthenticationFilter::class.java
               )

          return http.build()
     }
}
