plugins {
    id ("org.springframework.boot") version "3.1.2"
    id ("io.spring.dependency-management") version "1.1.2"
    id ("java")
    id ("jacoco") // Plugin Jacoco
    id ("org.sonarqube") version "3.3" // Plugin SonarQube
}

group = "com.fiapgrupo27"
version = "1.0.0"
java.sourceCompatibility = JavaVersion.VERSION_17
java.targetCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
}

dependencies {
    
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("software.amazon.awssdk:s3:2.20.20")
    implementation("software.amazon.awssdk:sqs:2.20.20")
    implementation("org.springframework.boot:spring-boot-starter-amqp")
    implementation("org.postgresql:postgresql:42.5.1")
    implementation("software.amazon.awssdk:apache-client:2.20.20") 
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.1")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.7.1")
    testImplementation("org.mockito:mockito-core:4.0.0") // Para Mockito
    testImplementation("org.mockito:mockito-junit-jupiter:5.5.0")  // Para integração com JUnit 5

}

tasks.test {
    useJUnitPlatform()
}

jacoco {
    toolVersion = "0.8.9"
}

tasks.withType<Test>().configureEach {
    finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
    dependsOn(tasks.test) // Garante que os testes rodem antes do relatório ser gerado
    reports {
        xml.required.set(true)
        xml.outputLocation.set(file("$buildDir/reports/jacoco/test/jacocoTestReport.xml"))
    }
}

sonarqube {
    properties {
        property ("sonar.projectKey", "grupo27-6soat-fiap_grupo27-hackaton-video-processor-clean")
        property ("sonar.organization", "grupo27-6soat-fiap")
        property ("sonar.host.url", "https://sonarcloud.io")
        property ("sonar.coverage.jacoco.xmlReportPaths", "$buildDir/reports/jacoco/test/jacocoTestReport.xml")
    }
}