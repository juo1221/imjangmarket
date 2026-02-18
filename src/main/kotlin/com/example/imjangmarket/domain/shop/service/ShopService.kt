package com.example.imjangmarket.domain.shop.service

import com.example.imjangmarket.domain.shop.dto.ShopRes
import com.example.imjangmarket.domain.shop.error.ShopError
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
     fun getShopDetail(shopId: Long): ServiceResult<ShopRes> {
          val shop = shopRepository.findShopDetail(shopId)
          return if (shop != null) ServiceResult.Success(shop) else ServiceResult.Failure(ShopError.NotExist)
     }
     fun getMyShop(memberId: Long): ServiceResult<ShopRes> {
          val shop = shopRepository.findByMemberId(memberId)
          return if (shop != null) ServiceResult.Success(shop) else ServiceResult.Failure(ShopError.NotExist)
     }
}