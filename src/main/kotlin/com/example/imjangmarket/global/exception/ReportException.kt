package com.example.imjangmarket.global.exception

import org.springframework.http.HttpStatus

/**
 * 임장 보고서 관련 예외
 */
class ReportAlreadyExistsException(message: String = "이미 해당 사건에 대해 작성된 보고서가 존재합니다.")
     : BusinessException(message, HttpStatus.CONFLICT)

class InvalidCaseNumberException(message: String = "올바르지 않은 사건번호 형식입니다.")
     : BusinessException(message, HttpStatus.BAD_REQUEST)