package com.example.imjangmarket.global.config

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.media.Content
import io.swagger.v3.oas.models.media.MediaType
import io.swagger.v3.oas.models.media.Schema
import io.swagger.v3.oas.models.responses.ApiResponse
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springdoc.core.customizers.OpenApiCustomizer
import org.springdoc.core.customizers.OperationCustomizer
import org.springdoc.core.models.GroupedOpenApi
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenApiConfig {
     @Bean
     fun openApi(): OpenAPI {
          return OpenAPI().info(
               Info()
                    .title("임장 마켓 api 명세서")
                    .description("부동산 임장 보고서 거래 플랫폼의 백엔드 API입니다.")
                    .version("v1.0.0")
          )
               .addSecurityItem(SecurityRequirement().addList("bearerAuth"))
               .components(
                    Components()
                         .addSecuritySchemes(
                              "bearerAuth",
                              SecurityScheme()
                                   .type(SecurityScheme.Type.HTTP)
                                   .scheme("bearer")
                                   .bearerFormat("JWT")
                                   .`in`(SecurityScheme.In.HEADER)
                                   .name("Authorization")
                         )
               )
     }

     @Bean
     fun errorResponseCustomizer(): OperationCustomizer {
          return OperationCustomizer { operation, handlerMethod ->
               // 메서드에 @ApiErrorDescription이 붙어 있는지 확인
               val annotation = handlerMethod.getMethodAnnotation(ApiErrorDescription::class.java)

               annotation?.let {
                    val errorClass = it.errorClass.java

                    // 400 Bad Request 응답 정의 생성
                    val apiResponse = io.swagger.v3.oas.models.responses.ApiResponse().apply {
                         description = "비즈니스 로직 에러 (코드 확인 필수)"
                         content = Content().addMediaType(
                              "application/json",
                              MediaType().schema(
                                   // 해당 에러 클래스의 스키마를 참조하도록 설정
                                   Schema<Any>().`$ref`("#/components/schemas/${errorClass.simpleName}")
                              )
                         )
                    }

                    // Swagger Operation에 응답 추가
                    operation.responses.addApiResponse("400", apiResponse)
               }
               operation
          }
     }

     @Bean
     fun globalResponseCustomizer(): OpenApiCustomizer {
          return OpenApiCustomizer { openApi ->
               openApi.paths.values.forEach { pathItem ->
                    pathItem.readOperations().forEach { operation ->
                         val responses = operation.responses
                         // 401 인증 실패 전역 추가
                         responses.addApiResponse("401", ApiResponse().description("인증에 실패했습니다. (JWT 만료 또는 누락)"))
                         // 500 서버 오류 전역 추가
                         responses.addApiResponse("500", ApiResponse().description("서버 내부 오류가 발생했습니다."))
                    }
               }
          }
     }
}