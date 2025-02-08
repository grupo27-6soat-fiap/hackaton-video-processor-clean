plugins {
    id("org.springframework.boot") version "3.1.0"
    id("io.spring.dependency-management") version "1.1.3"
    id("java")
}

group = "com.fiapgrupo27"
version = "1.0.0"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
}

dependencies {
    // Spring Boot Starter Web para criar APIs REST
    implementation("org.springframework.boot:spring-boot-starter-web")

    // AWS SDK para interagir com o S3
    implementation("software.amazon.awssdk:s3:2.20.148")

    // Lombok para reduzir código boilerplate
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")



    // AWS SDK para S3
    implementation("software.amazon.awssdk:s3:2.20.148")

    // Dependências do AWS SDK para S3 e SQS
    implementation("software.amazon.awssdk:s3:2.20.20")
    implementation("software.amazon.awssdk:sqs:2.20.20")


    // Testes com JUnit
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
