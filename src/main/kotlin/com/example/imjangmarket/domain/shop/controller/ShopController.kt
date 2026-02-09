package com.example.imjangmarket.domain.shop.controller

import com.example.imjangmarket.domain.report.dto.ReporRes
import com.example.imjangmarket.domain.shop.dto.ShopRes
import com.example.imjangmarket.domain.shop.service.ShopService
import com.example.imjangmarket.global.api.ApiResponse
import com.example.imjangmarket.global.result.toApiResponse
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/shops")
class ShopController(private val shopService: ShopService) {

     // 특정 상점 상세 조회 (누구나 가능)
     @GetMapping("/{shopId}")
     fun getShopDetail(@PathVariable shopId: Long): ApiResponse<ShopRes>  = shopService.getShopDetail(shopId).toApiResponse()

     @GetMapping("/me")
     fun getMyShop(@AuthenticationPrincipal memberId: Long): ApiResponse<ShopRes>  = shopService.getMyShop(memberId).toApiResponse()
}