plugins {
    kotlin("jvm") version "2.3.20"
    application
}

application {
    mainClass.set("com.xunfos.MainKt")
}

group = "com.xunfos"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}
val exposed_version = "1.3.0"

dependencies {
    implementation("org.jetbrains.exposed:exposed-core:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-r2dbc:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-migration-core:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-migration-r2dbc:$exposed_version")
    implementation("org.testcontainers:postgresql:1.19.8")
    // Implementation surfaces the issue
    implementation("org.postgresql:r2dbc-postgresql:1.0.5.RELEASE")
    
    testImplementation(kotlin("test"))
}

kotlin {
    jvmToolchain(21)
}

tasks.test {
    useJUnitPlatform()
}
