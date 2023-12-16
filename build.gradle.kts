plugins {
    java
    application
    id("com.github.johnrengelman.shadow") version ("8.1.1")
}

group = "de.rechergg"
version = "v1.0.0"

application {
    mainClass.set("de.rechergg.Launcher")
}

repositories {
    mavenCentral()
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

dependencies {
    implementation(libs.jda)
    implementation(libs.gson)
    implementation(libs.okhttp)
    implementation(libs.slf4j.api)
    implementation(libs.logback.classic)
}

tasks {
    withType<JavaCompile> {
        options.encoding = "UTF-8"
    }

    shadowJar {
        mergeServiceFiles()
        relocate("org.slf4j", "devcord-libs.org.slf4j")
        relocate("ch.qos.logback", "devcord-libs.ch.qos.logback")
        relocate("com.squareup.okhttp3", "devcord-libs.com.squareup.okhttp3")
    }
}

