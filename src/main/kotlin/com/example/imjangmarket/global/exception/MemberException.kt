package com.example.imjangmarket.global.exception

import jdk.internal.joptsimple.internal.Messages.message
import org.springframework.http.HttpStatus

/**
 * 회원 관련 예외
 */
val ErrDuplicateMember = object : BusinessException("이미 사용중인 아이디입니다.", HttpStatus.CONFLICT) {}
val ErrPasswordException = object : BusinessException("비밀번호가 일치하지 않습니다.", HttpStatus.UNAUTHORIZED){}