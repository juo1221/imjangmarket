package com.example.imjangmarket.repository

import org.jooq.DSLContext
import org.springframework.stereotype.Repository
import com.example.imjangmarket.jooq.tables.references.USERS

@Repository
class UserRepository(
    private val dslContext: DSLContext
) {
    fun insertUser(name: String, age: Int) {
        dslContext.insertInto(USERS)
            .set(USERS.NAME, name)
            .set(USERS.AGE, age)
            .execute()
    }
}
