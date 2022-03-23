val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val prometheus_version: String by project
val kodein_version: String by project
val exposed_version: String by project
val hikari_version: String by project

plugins {
    application
    kotlin("jvm") version "1.5.20"
    id("com.github.johnrengelman.shadow") version "7.0.0"
}

group = "demo"
version = "0.0.2"
application {
    mainClass.set("ktor.ApplicationKt")
}

repositories {
    mavenCentral()
}

tasks{
    shadowJar {
        manifest {
            attributes(Pair("Main-Class", "ktor.ApplicationKt"))
        }
    }
}

dependencies {
    // Webserver
    implementation("io.ktor:ktor-server-cio:$ktor_version")
    implementation("io.ktor:ktor-server-core:$ktor_version")
    implementation("io.ktor:ktor-server-host-common:$ktor_version")
    // Security
    implementation("io.ktor:ktor-auth:$ktor_version")
    //Monitoring
    implementation("io.ktor:ktor-metrics-micrometer:$ktor_version")
    implementation("io.micrometer:micrometer-registry-prometheus:$prometheus_version")
    // Dependency injection
    implementation("org.kodein.di:kodein-di-generic-jvm:$kodein_version")
    // Database
    implementation("org.postgresql:postgresql:42.2.23")
    implementation("org.jetbrains.exposed:exposed:$exposed_version")
    implementation("com.zaxxer:HikariCP:$hikari_version")
    //Client dependencies
    implementation("io.ktor:ktor-client-core:$ktor_version")
    implementation("io.ktor:ktor-client-apache:$ktor_version")
    // Misc
    implementation("io.ktor:ktor-jackson:$ktor_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")
}
