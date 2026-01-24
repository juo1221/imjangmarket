package com.example.imjangmarket.global.utils

import com.example.imjangmarket.global.result.ServiceResult
import org.springframework.transaction.TransactionStatus
import org.springframework.transaction.support.TransactionOperations


fun <T> TransactionOperations.executeWithResult(
     block:(TransactionStatus) -> ServiceResult<T>
): ServiceResult<T> {
     return this.execute { ts ->
          val res = block(ts)
          if(res is ServiceResult.Failure) {
               ts.setRollbackOnly()
          }
          res
     }
}