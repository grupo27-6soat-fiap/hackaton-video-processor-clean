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
    // Dependências de teste
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.3")  // JUnit 5 API
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.9.3")  // JUnit 5 Engine
    testImplementation("org.springframework.boot:spring-boot-starter-test") // JUnit 5 e Mockito já inclusos
    testImplementation("org.mockito:mockito-core:4.6.1")
    
    //cucumber
    testImplementation("io.cucumber:cucumber-java:7.13.0")
    testImplementation("io.cucumber:cucumber-spring:7.13.0")
    testImplementation("io.cucumber:cucumber-junit-platform-engine:7.13.0")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation("org.assertj:assertj-core:3.23.1")
    testImplementation("org.junit.platform:junit-platform-suite:1.10.0")
    testImplementation("com.h2database:h2")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
