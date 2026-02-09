package com.example.imjangmarket.domain.shop.dto

data class ShopRes(
     val id: Long,
     val memberId: Long,
     val nickname: String,
     val totalReportCount: Int,
     val averageRating: Double
)