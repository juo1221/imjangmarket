package com.example.imjangmarket.domain.shop.controller

import com.example.imjangmarket.domain.shop.dto.ShopRes
import com.example.imjangmarket.domain.shop.service.ShopService
import com.example.imjangmarket.global.api.ApiResponse
import com.example.imjangmarket.global.result.toApiResponse
import com.example.imjangmarket.global.security.CustomUserDetails
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.User
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/shops")
@Tag(name = "shop", description = "상점 관리 API")
class ShopController(private val shopService: ShopService) {

     @Operation(
          summary = "특정 상점 상세 조회",
          description = "상점 ID를 이용해 해당 상점의 프로필과 통계 정보를 조회합니다.",
          security = []
     )
     @GetMapping("/{shopId}")
     fun getShopDetail(@PathVariable shopId: Long): ApiResponse<ShopRes>  = shopService.getShopDetail(shopId).toApiResponse()


     @Operation(
          summary = "내 상점 정보 조회",
          description = "로그인한 사용자의 상점 정보를 조회합니다.",
     )
     @GetMapping("/me")
     fun getMyShop(@AuthenticationPrincipal userDetails: CustomUserDetails): ApiResponse<ShopRes>  {
          return  shopService.getMyShop(userDetails.id).toApiResponse()
     }
}