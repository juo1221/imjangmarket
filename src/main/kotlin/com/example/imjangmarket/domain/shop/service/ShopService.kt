package com.example.imjangmarket.domain.shop.service

import com.example.imjangmarket.domain.shop.dto.ShopRes
import com.example.imjangmarket.domain.shop.repository.ShopRepository
import com.example.imjangmarket.global.exception.BaseError
import com.example.imjangmarket.global.result.ServiceResult
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class ShopService(
     private val shopRepository: ShopRepository
) {
     sealed interface ShopError : BaseError {
          object NotExist : ShopError {
               override val code = "001"
               override val msg = "존재하지 않는 상점입니다."
          }
          object NotFound : ShopError {
               override val code = "002"
               override val msg = "상점 정보를 찾을 수 없습니다."
          }
     }
     fun getShopDetail(shopId: Long): ServiceResult<ShopRes> {
          val shop = shopRepository.findShopDetail(shopId)
          return if (shop != null) ServiceResult.Success(shop) else ServiceResult.Failure(ShopError.NotExist)
     }
     fun getMyShop(memberId: Long): ServiceResult<ShopRes> {
          println("memberId : $memberId")
          val shop = shopRepository.findByMemberId(memberId)
          println("shop : $shop")
          return if (shop != null) ServiceResult.Success(shop) else ServiceResult.Failure(ShopError.NotFound)
     }
}