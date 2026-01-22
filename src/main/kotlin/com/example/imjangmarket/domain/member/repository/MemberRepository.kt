package com.example.imjangmarket.domain.member.repository

import com.example.imjangmarket.jooq.tables.records.MemberRecord
import com.example.imjangmarket.jooq.tables.references.MEMBER
import org.jooq.DSLContext

class MemberRepository(private val dsl: DSLContext) {
     private val member = MEMBER
     fun findByUserId(userId:String): MemberRecord? {
          return dsl.selectFrom(member)
               .where(member.USER_ID.eq(userId))
               .fetchOne()
     }
     fun save(record: MemberRecord) {
          dsl.insertInto(member)
               .set(record)
               .execute()
     }
}