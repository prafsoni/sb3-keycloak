plugins {
    java
    id("org.springframework.boot") version "3.1.3"
    id("io.spring.dependency-management") version "1.1.3"
}

group = "org.pksprojects.sb3-keycloak"
version = "0.0.1-SNAPSHOT"
//name = "sba-service"
java.sourceCompatibility = JavaVersion.VERSION_17

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
    maven { url = uri("https://artifactory-oss.prod.netflix.net/artifactory/maven-oss-candidates") }
}

extra["springBootAdminVersion"] = "3.1.6"
extra["springCloudVersion"] = "2022.0.4"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("de.codecentric:spring-boot-admin-starter-server")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
    implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client")
    implementation("org.springframework.cloud:spring-cloud-starter-config")
    implementation("org.springframework.boot:spring-boot-starter-aop")
//    implementation("org.springframework.cloud:spring-cloud-starter-bootstrap")
//    implementation("io.pivotal.spring.cloud:spring-cloud-services-starter-config-client:4.0.3")
//    implementation("io.pivotal.cfenv:java-cfenv:3.0.0")
//    implementation("io.pivotal.cfenv:java-cfenv-boot-pivotal-scs:3.0.0")
    implementation(project(":sb3-config-client-oauth2-support"))
    implementation(project(":sb3-eureka-client-oauth2-support"))
    implementation("org.springframework.retry:spring-retry")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

dependencyManagement {
    imports {
        mavenBom("de.codecentric:spring-boot-admin-dependencies:${property("springBootAdminVersion")}")
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
