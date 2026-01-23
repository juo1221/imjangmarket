import org.jetbrains.kotlin.gradle.dsl.JvmTarget

val jooqVersion = "3.19.29"

plugins {
    id("org.jetbrains.kotlin.jvm") version "2.2.21"
    id("org.jetbrains.kotlin.plugin.spring") version "2.2.21"
    id("org.springframework.boot") version "4.0.1"
    id("io.spring.dependency-management") version "1.1.7"
    id("nu.studer.jooq") version "9.0"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

extra["jooq.version"] = jooqVersion

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.flywaydb:flyway-database-postgresql")
    implementation("org.springframework.boot:spring-boot-starter-jooq")
    implementation("org.postgresql:postgresql")
    // 실행용 jOOQ 버전 고정
    implementation("org.jooq:jooq:${jooqVersion}")
    implementation("org.jooq:jooq-meta:${jooqVersion}")
    implementation("org.jooq:jooq-codegen:${jooqVersion}")
    // jOOQ 생성기용 드라이버 및 jOOQ 라이브러리 버전 강제 일치
    "jooqGenerator"("org.postgresql:postgresql:42.7.2")
    "jooqGenerator"("org.jooq:jooq-meta:${jooqVersion}")
    "jooqGenerator"("org.jooq:jooq-codegen:${jooqVersion}")
    // Spring Security
    implementation("org.springframework.boot:spring-boot-starter-security")

    // JWT 관련 라이브러리 (0.12.x 버전 기준)
    implementation("io.jsonwebtoken:jjwt-api:0.12.5")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.5")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.5")

    // 테스트를 위한 Security 도움 도구
    // 1. 스프링 부트 테스트 통합 패키지 (JUnit5, AssertJ, Mockito 등 포함)
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    // 2. 스프링 시큐리티 테스트 도구 (인증/인가 흐름 검증에 필수)
    testImplementation("org.springframework.security:spring-security-test")
    // 4. 코틀린용 테스트 지원
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
}
jooq {
    version.set(jooqVersion)
    configurations {
        create("main") {
            jooqConfiguration.apply {
                setLogging(org.jooq.meta.jaxb.Logging.WARN)

                jdbc.apply {
                    driver = "org.postgresql.Driver"
                    url = project.findProperty("DB_URL")?.toString() ?: ""
                    user = project.findProperty("DB_USER")?.toString() ?: ""
                    password = project.findProperty("DB_PASSWORD")?.toString() ?: ""
                }

                generator.apply {
                    name = "org.jooq.codegen.KotlinGenerator"
                    database.apply {
                        name = "org.jooq.meta.postgres.PostgresDatabase"
                        inputSchema = "public"
                        // PG 16 호환성을 위해 추가
                        isIncludeSystemCheckConstraints = false
                    }
                    target.apply {
                        packageName = "com.example.imjangmarket.jooq"
                        directory = "build/generated-sources/jooq"
                    }
                }
            }
        }
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_21)
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}
sourceSets {
    main {
        kotlin { srcDir("build/generated-sources/jooq") }
    }
}

tasks.withType<Test> {
    useJUnitPlatform() // JUnit 5 테스트 엔진을 사용하도록 명시
}