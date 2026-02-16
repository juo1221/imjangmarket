package com.example.imjangmarket.global.config

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.media.Schema
import io.swagger.v3.oas.models.responses.ApiResponse
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springdoc.core.customizers.OpenApiCustomizer
import org.springdoc.core.models.GroupedOpenApi
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order

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

     @Bean
     @Order(Ordered.LOWEST_PRECEDENCE) // Ensure this customizer runs last
     fun schemaFilterCustomizer(): OpenApiCustomizer {
          return OpenApiCustomizer { openApi ->
               val schemas = openApi.components.schemas ?: return@OpenApiCustomizer

               // 1. Identify top-level schemas
               val topLevelSchemas = schemas.filterKeys { name -> isTopLevel(name) }

               // 2. Inline nested schemas into top-level schemas
               topLevelSchemas.values.forEach { schema ->
                    inlineNestedSchemas(schema, schemas)
               }

               // 3. Remove non-top-level schemas
               schemas.keys.retainAll(topLevelSchemas.keys)
          }
     }

     private fun isTopLevel(name: String): Boolean {
          val topLevelSuffixes = listOf("Request", "Res", "Response", "Dto") // Added "Dto"
          val errorPrefix = "Error_"
          return topLevelSuffixes.any { name.endsWith(it) } || name.startsWith(errorPrefix)
     }

     private fun inlineNestedSchemas(schema: Schema<*>, schemas: Map<String, Schema<*>>) {
          schema.properties?.values?.forEach { property ->
               inlineProperty(property, schemas)
          }
          if (schema.items != null) {
               inlineProperty(schema.items, schemas)
          }
     }

     @Suppress("UNCHECKED_CAST")
     private fun inlineProperty(property: Schema<*>, schemas: Map<String, Schema<*>>) {
          if (property.`$ref` != null) {
               val refName = property.`$ref`.substringAfterLast("/")
               // Only inline if the referenced schema is NOT a top-level schema
               if (!isTopLevel(refName)) {
                    val referencedSchema = schemas[refName]
                    if (referencedSchema != null) {
                         // Cast to Schema<Any> to avoid type mismatch errors when copying properties
                         val mutableProperty = property as Schema<Any>
                         val mutableReferenced = referencedSchema as Schema<Any>

                         mutableProperty.`$ref` = null
                         mutableProperty.type = mutableReferenced.type
                         mutableProperty.properties = mutableReferenced.properties
                         mutableProperty.items = mutableReferenced.items
                         mutableProperty.description = mutableReferenced.description
                         mutableProperty.format = mutableReferenced.format
                         mutableProperty.example = mutableReferenced.example
                         mutableProperty.enum = mutableReferenced.enum
                         mutableProperty.required = mutableReferenced.required
                         
                         // Recursively inline
                         inlineNestedSchemas(mutableProperty, schemas)
                    }
               }
          } else {
               inlineNestedSchemas(property, schemas)
          }
     }

     @Bean
     fun shopApi(): GroupedOpenApi? {
          return GroupedOpenApi.builder()
               .group("shop API")
               .pathsToMatch("/api/shop/**")
               .build()
     }
     @Bean
     fun reportApi(): GroupedOpenApi? {
          return GroupedOpenApi.builder()
               .group("report API")
               .pathsToMatch("/api/report/**")
               .build()
     }
     @Bean
     fun authApi(): GroupedOpenApi? {
          return GroupedOpenApi.builder()
               .group("auth API")
               .pathsToMatch("/api/auth/**")
               .build()
     }
     @Bean
     fun totalApi(): GroupedOpenApi? {
          return GroupedOpenApi.builder()
               .group("total API")
               .pathsToMatch("/api/**")
               .build()
     }
}
