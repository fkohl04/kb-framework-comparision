import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.5.4"
	id("io.spring.dependency-management") version "1.0.11.RELEASE"
	kotlin("jvm") version "1.5.21"
	kotlin("plugin.spring") version "1.5.21"
}

repositories {
	mavenCentral()
}

dependencies {
	// Webserver
	implementation("org.springframework.boot:spring-boot-starter-web")
	// Monitoring
	implementation("io.micrometer:micrometer-registry-prometheus")
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	// Security
	implementation("org.springframework.boot:spring-boot-starter-security")
	// Database
	implementation("org.postgresql:postgresql:42.2.23")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	// Misc
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		jvmTarget = "11"
	}
}
