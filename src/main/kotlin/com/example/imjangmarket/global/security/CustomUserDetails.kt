package com.example.imjangmarket.global.security

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class CustomUserDetails(
     val id: Long,
     private val userId: String,
     private val authorities: Collection<GrantedAuthority>
): UserDetails {
     override fun getAuthorities(): Collection<GrantedAuthority> = authorities
     override fun getPassword(): String = ""
     override fun getUsername(): String = userId

     override fun isAccountNonExpired(): Boolean = true
     override fun isAccountNonLocked(): Boolean = true
     override fun isCredentialsNonExpired(): Boolean = true
     override fun isEnabled(): Boolean = true
}

