package com.example.imjangmarket.global.exception

import org.springframework.http.HttpStatus

/**
 * 임장 보고서 관련 예외
 */
sealed class ReportException(message: String) : BusinessException(message) {
     class AlreadyExists : ReportException("보고서가 이미 존재합니다.")
     class InvalidFormat : ReportException("올바르지 않은 사건번호 형식입니다.")
     class AccessDenied : ReportException("보고서에 대한 접근 권한이 없습니다.")
}

