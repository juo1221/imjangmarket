package com.example.imjangmarket.domain.shop.repository

import com.example.imjangmarket.domain.shop.dto.ShopRes
import com.example.imjangmarket.jooq.tables.references.MEMBER
import com.example.imjangmarket.jooq.tables.references.SHOP
import org.jooq.DSLContext
import org.springframework.stereotype.Repository
import kotlin.jvm.java

@Repository
class ShopRepository(private val dsl: DSLContext) {

     fun save(memberId: Long) {
          dsl.insertInto(SHOP)
               .set(SHOP.MEMBER_ID, memberId)
               .set(SHOP.TOTAL_REPORT_COUNT, 0)
               .set(SHOP.AVERAGE_RATING, 0.0.toBigDecimal())
               .execute()
     }

     fun findShopDetail(shopId:Long): ShopRes? {
          return dsl.select(
               SHOP.ID,
               SHOP.MEMBER_ID,
               MEMBER.NICKNAME,
               SHOP.TOTAL_REPORT_COUNT,
               SHOP.AVERAGE_RATING
          )
               .from(SHOP)
               .join(MEMBER).on(SHOP.MEMBER_ID.eq(MEMBER.ID))
               .where(SHOP.ID.eq(shopId))
               .fetchOneInto(ShopRes::class.java)
     }

     fun findByMemberId(memberId:Long): ShopRes? {
          return dsl.select(
               SHOP.ID,
               SHOP.MEMBER_ID,
               MEMBER.NICKNAME,
               SHOP.TOTAL_REPORT_COUNT,
               SHOP.AVERAGE_RATING
          )
               .from(SHOP)
               .join(MEMBER).on(SHOP.MEMBER_ID.eq(MEMBER.ID))
               .where(SHOP.MEMBER_ID.eq(memberId))
               .fetchOneInto(ShopRes::class.java)
     }
}